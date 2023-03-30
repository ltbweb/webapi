package com.tek.test.util;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import javax.servlet.http.HttpServletRequest;

public class IPUtil {
    static  final  String UNKNOWN="ukonwn";

    public static String getIpAddr(HttpServletRequest request){
        if(request ==null){
            return UNKNOWN;
        }
        String ip =request.getHeader("x-forwarded-for");
        if(ip ==null || ip.length()==0||UNKNOWN.equalsIgnoreCase(ip)){
            ip=request.getHeader(("Proxy-Client-IP"));
        }
        if(ip==null||ip.length()==0||UNKNOWN.equalsIgnoreCase(ip)){
            ip=request.getHeader(("X-Forwarded-IP"));
        }
        if(ip ==null || ip.length()==0||UNKNOWN.equalsIgnoreCase(ip)){
                ip=request.getHeader(("WL-Proxy-Client-IP"));
        }
        if(ip==null||ip.length()==0||UNKNOWN.equalsIgnoreCase(ip)){
            ip=request.getHeader(("X-Real-IP"));
        }
        if(ip==null||ip.length()==0||UNKNOWN.equalsIgnoreCase(ip)){
            ip=request.getRemoteAddr();
        }
        System.out.println(ip);
        return "0:0:0:0:0:0:0:1".equals(ip)?"127.0.0.1":ip;
    }
}
