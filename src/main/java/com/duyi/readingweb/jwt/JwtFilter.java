package com.duyi.readingweb.jwt;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.interfaces.Claim;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 一定要注意，在入口文件上加注解，让他经过这里！！！
 */
@Slf4j
@WebFilter(filterName = "JwtFilter", urlPatterns = "/api/secure/*")
public class JwtFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) servletRequest;
        final HttpServletResponse response = (HttpServletResponse) servletResponse;

        response.setCharacterEncoding("UTF-8");
        final String token = request.getHeader("authorization");
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
        } else {
//            System.out.println(2);
            if (token == null) {
                Map<String,Object> map=new HashMap<>();
                map.put("result",false);
                response.getWriter().write(JSON.toJSONString(map));
                return;
            }
            System.out.println(2);
            Map<String, Claim> userData = JwtUtil.verifyToken(token);
            if (userData == null) {
                Map<String,Object> map=new HashMap<>();
                map.put("result",false);
                response.getWriter().write(JSON.toJSONString(map));
                return;
            }
//            System.out.println(userData.get("name"));
//            System.out.println(userData.get("email"));
            System.out.println(3);
//            Integer id= userData.get("id").asInt();
            System.out.println(userData);
            String name;
            String email;
            Integer deduction;
            System.out.println(userData.get("name"));
            System.out.println(userData.get("email"));
            System.out.println(userData.get("deduction"));
            try {
                name = userData.get("name").asString();
                email = userData.get("email").asString();
                deduction = userData.get("deduction").asInt();
                System.out.println(name);
                System.out.println(deduction);
                System.out.println("--------------------");
                System.out.println("--------------------");
                System.out.println("--------------------");
                System.out.println("--------------------");
                System.out.println("--------------------");
                request.setAttribute("name", name);
                request.setAttribute("email", email);
                request.setAttribute("token",token);
                request.setAttribute("deduction",deduction);
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                Map<String,Object> map=new HashMap<>();
                map.put("result",false);
                response.getWriter().write(JSON.toJSONString(map));
            }

        }
    }
}

