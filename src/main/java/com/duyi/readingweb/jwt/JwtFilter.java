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
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
////        System.out.println("in filter");
//        final HttpServletRequest request=(HttpServletRequest)req;
//        final HttpServletResponse response = (HttpServletResponse) resp;
//
//        response.setCharacterEncoding("UTF-8");
//        final String token=request.getHeader("authorization");
////        System.out.println(111111);
//        if("OPTIONS".equals(request.getMethod())){
//            response.setStatus(HttpServletResponse.SC_OK);
//            chain.doFilter(request,response);
//        }else{
////            System.out.println(2);
//            if(token==null){
//                response.getWriter().write("no token");
//                return;
//            }
////            System.out.println(2);
//            Map<String, Claim> userData=JwtUtil.verifyToken(token);
////            System.out.println(userData);
//            if(userData==null){
//                response.getWriter().write("not vailid token");
//                return;
//            }
////            System.out.println(3);
//            Integer id= userData.get("id").asInt();
//            String name=userData.get("name").asString();
//            String password=userData.get("password").asString();
//            request.setAttribute("id",id);
//            request.setAttribute("name",name);
//            request.setAttribute("password",password);
//            chain.doFilter(request,response);
//        }
//    }

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
//            System.out.println(2);
            Map<String, Claim> userData = JwtUtil.verifyToken(token);
//            System.out.println(userData);
            if (userData == null) {
                Map<String,Object> map=new HashMap<>();
                map.put("result",false);
                response.getWriter().write(JSON.toJSONString(map));
                return;
            }
//            System.out.println(userData.get("name"));
//            System.out.println(userData.get("email"));
//            System.out.println(3);
//            Integer id= userData.get("id").asInt();
            String name;
            String email;
            try {
                name = userData.get("name").asString();
                email = userData.get("email").asString();
                request.setAttribute("name", name);
                request.setAttribute("email", email);
                request.setAttribute("token",token);
                filterChain.doFilter(request, response);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                response.getWriter().write("not vailid token");
            }

//            request.setAttribute("id",id);

//            System.out.println("in33333");

        }
    }
}
