package com.duyi.readingweb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.entity.product.UserProductCartdetail;
import com.duyi.readingweb.entity.product.UserProductWishlist;
import com.duyi.readingweb.entity.invoice.Invoice;
import com.duyi.readingweb.entity.user.User;
import com.duyi.readingweb.jwt.JwtUtil;
import com.duyi.readingweb.service.product.UserProductCartdetailService;
import com.duyi.readingweb.service.product.UserProductWishlistService;
import com.duyi.readingweb.service.invoice.InvoiceService;
import com.duyi.readingweb.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Slf4j
@RestController
@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
public class LoginController {

    @RequestMapping(value = "/api/login", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg login(@RequestParam Map<String, Object> params, HttpServletRequest request) {

        System.out.println(params);


        String email = (String) params.get("email");
//        String password = (String) params.get("params[password]");
        String password = DigestUtils.md5DigestAsHex(((String) params.get("password")).getBytes());
        System.out.println(params.get("email"));
        System.out.println(params.get("password"));

        User user = userService.getOne(new QueryWrapper<User>().eq("email", email));
        if (user != null && user.getUpassword() != null && user.getUpassword().equals(password)) {
            String token = JwtUtil.createToken(user);
            user.setLastlogin(DateTime.now().toDate());
            userService.update(user, new QueryWrapper<User>().eq("userid", user.getUserid()));
            return ResultMsg.ok().data("token", token);
        } else {
            System.out.println("in selse");
            return ResultMsg.error().result(false);
        }


    }

    @Autowired
    private UserService userService;
    @Autowired
    private UserProductWishlistService userProductWishlistService;
    @Autowired
    private UserProductCartdetailService userProductCartdetailService;
    @Autowired
    private InvoiceService invoiceService;

    @RequestMapping(value = "/api/register", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg register(@RequestParam Map<String, String> params) {
        String email = params.get("email");
        String firstName = params.get("firstName");
        String password = DigestUtils.md5DigestAsHex(params.get("password").getBytes());
        String lastName = params.get("lastName");
        List<User> list = userService.list(new QueryWrapper<User>().eq("email", email));
        if (list.size() > 0) return ResultMsg.error().message("invalid email");
        User user = new User();
        user.setEmail(email);
        user.setFirstname(firstName);
        user.setLastname(lastName);
        user.setUpassword(password);
        user.setLastlogin(DateTime.now().toDate());
        user.setDeduction(1000);
        try {
            userService.save(user);
            String token = JwtUtil.createToken(user);
            return ResultMsg.ok().data("token", token);
//            return ResultMsg.ok().result(true);
        } catch (Exception e) {
            return ResultMsg.error().message("invalid email");
        }
    }

    @RequestMapping("/api/secure/getUserInfo")
    public ResultMsg login(HttpServletRequest request, HttpServletResponse response) {
        String userName = request.getAttribute("name").toString();
        String email = request.getAttribute("email").toString();
        String token = request.getAttribute("token").toString();
        Map<String, Object> map = new HashMap<>();
        map.put("userName", userName);
        map.put("email", email);
        map.put("token", token);
        return ResultMsg.ok().result(true).data("data", map);
    }


    @RequestMapping("/api/secure/queryNumberOfWishAndProductAndInvoiceNum")
    public ResultMsg queryNumberOfWishAndProductAndInvoiceNum(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        Integer wishNum = (int) userProductWishlistService.count(new QueryWrapper<UserProductWishlist>().eq("useremail", email));
        List<UserProductCartdetail> productList = userProductCartdetailService.list(new QueryWrapper<UserProductCartdetail>()
                .select("productamount").eq("useremail", email));
        Integer productNum = 0;
        for (int i = 0; i < productList.size(); i++) {
            productNum += productList.get(i).getProductamount();
        }
        User user = userService.getOne(new QueryWrapper<User>().eq("email", email));
        Integer invoiceNum = (int) invoiceService.count(new QueryWrapper<Invoice>().eq("userid", user.getUserid()));
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("wishNum", wishNum);
        resMap.put("productNum", productNum);
        resMap.put("invoiceNum", invoiceNum);
        return ResultMsg.ok().result(true).data(resMap);
    }

}
