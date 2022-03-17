package com.nginx.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

@Controller
public class HostController {

    @RequestMapping("host/test")
    public ResponseEntity<Void> testHost(HttpServletRequest request){
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames.hasMoreElements()){
            String name = headerNames.nextElement();
            System.out.println(name + ":" + request.getHeader(name));
        }
        return ResponseEntity.ok().build();
    }
}
