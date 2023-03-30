package com.tek.test.controller;

import com.tek.test.Entity.User;
import com.tek.test.util.Csv;
import com.tek.test.util.DateUtils;
import com.tek.test.util.IPUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("user")
public class UserController {


    private Map<String,User> userMap = new HashMap<>();

    @Value("${spring.tempPath}")
    private String path;
    

    @RequestMapping("/reset/{userName}/{status}")
    String reset(@PathVariable String userName,@PathVariable String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User)authentication.getPrincipal();
        Collection<GrantedAuthority> authorities = user.getAuthorities();
        AtomicBoolean flag = new AtomicBoolean(false);
        authorities.forEach(key ->{
            if(key.getAuthority().contains("admin")){
                flag.set(true);

            }
        });
        if(flag.get()){
            List<String> dataList = new ArrayList<>();
            User us=new User();
            us.setUsername(userName);
            us.setStatus(Boolean.valueOf( status));
            us.setTime(DateUtils.dateToString(new Date(),"yyyy-MM-dd HH:mm:ss"));
            dataList.add(us.toString());
            Csv.writeDataListToCsv("",dataList,path,true);
            return "reset success";
        }else {
            return "no permission";
        }

    }



    @RequestMapping("/info")
    String getIpInfo(HttpServletRequest request) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        List<User> users = null;
        try {
            users =getUsers();
        }catch (Exception e){

        }
        if(CollectionUtils.isEmpty(users)){
            return "";
        }
        if(users.size() == 1){
            return "";
        }
        if (users.size() > 2) {
            return users.get(1).getIp();
        }
        return "";
    }


    private List<User> getUsers() throws ParseException {
        List<String> strings = Csv.readFromCsv(path);
        String thisName = SecurityContextHolder.getContext().getAuthentication().getName();
        List<User> users = new ArrayList<>();
        for (String str :strings){
            String[] split = str.split(",");
            String userName = split[0];
            String time = split[1];
            String ip = split[2];
            String status = split[3];
            User user = new User(userName, time,ip,Boolean.valueOf(status));
            if(userName.equals(thisName)){
                users.add(user);
            }
        }
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User stu1, User stu2) {
                return stu2.getTime().compareTo(stu1.getTime());
            }
        });
        return users;
    }

    private Date getDate(String time) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        }catch (Exception e){
            System.out.println("error");
            return new Date();
        }
    }
}
