package com.tek.test.Filter;

import com.tek.test.Entity.User;
import com.tek.test.util.Csv;
import com.tek.test.util.DateUtils;
import com.tek.test.util.IPUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebFilter(filterName = "EncodingFilter",urlPatterns = "/*")
@Component
public class TokenFilter implements Filter {

    @Value("${spring.tempPath}")
    private String path;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        req.setCharacterEncoding("utf-8");//设置请求的编码 接受到的请求参数不会乱码。
        resp.setCharacterEncoding("utf-8");//设置响应的编码，响应的数据不会出现乱码。
        HttpServletRequest servletRequest = (HttpServletRequest) req;
        HttpServletResponse servletResponse = (HttpServletResponse) resp;

        if("/".equals(servletRequest.getRequestURI()) ){
            Date day=new Date();
            filterChain.doFilter(req,resp);
            User us=new User();
            us.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            us.setTime(DateUtils.dateToString(day,"yyyy-MM-dd HH:mm:ss"));
            us.setStatus(true);
            us.setIp(IPUtil.getIpAddr(servletRequest));
            List<String> list =new ArrayList<>();
            list.add(us.toString());
            Csv.writeDataListToCsv("",list,path,true);
        }else {
            //放行 flag 表示是否登录状态 true 表示正常登录  false 表示登录状态异常
            List<User> users = null;
            try {
                users = getUsers();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(users.size() == 0){
                servletResponse.sendRedirect("/login");
                return;
            }
            User user = users.get(0);
            boolean flag = user.isStatus();
            if(!flag){
                servletResponse.sendRedirect("/login");
            }else {
                filterChain.doFilter(req,resp);
            }
        }
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

    @Override
    public void destroy() {

    }
}
