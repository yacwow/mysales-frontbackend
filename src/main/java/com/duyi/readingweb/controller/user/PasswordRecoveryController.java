package com.duyi.readingweb.controller.user;

import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.util.user.resetPassword.ResetPasswordService;
import com.duyi.readingweb.util.user.sendEmail.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PasswordRecoveryController {

@Autowired
private ResetPasswordService resetPasswordService;
    @RequestMapping("/api/recoveryPassword")
    public ResultMsg getEmail(@RequestParam("email") String email) {
        System.out.println(email);
       return resetPasswordService.forgotPassword(email);
    }

    @RequestMapping("/api/resetPassword/{token}/{password}")
    public ResultMsg resetPwd(@PathVariable("token")String token,@PathVariable("password")String password){
        System.out.println(token);
        System.out.println(password);
        return resetPasswordService.resetPassword(token,password);
    }
}
