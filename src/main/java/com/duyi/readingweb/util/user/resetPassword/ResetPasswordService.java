package com.duyi.readingweb.util.user.resetPassword;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.entity.user.User;
import com.duyi.readingweb.service.user.UserService;
import com.duyi.readingweb.util.user.sendEmail.EmailService;
import com.duyi.readingweb.util.user.sendEmail.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResetPasswordService {

    private static final long EXPIRE_TOKEN_AFTER_MINUTES = 32;
    @Autowired
    private EmailService emailUtil;
    @Autowired
    private UserService userService;

    public ResultMsg forgotPassword(String email) {
        List<User> users = userService.list(new QueryWrapper<User>().eq("email", email));
        if (users == null) {
            return ResultMsg.error().result(false).message("email not exist");
        }
        if (users.size() >= 2) {
            return ResultMsg.error().result(false).message("email has been used");
        }
        User user = users.get(0);
        String firstname = user.getFirstname();

//
        String token = generateToken();
        user.setToken(token);
        user.setTokencreationdate(LocalDateTime.now());
        System.out.println(user);
        Boolean res = userService.updateById(user);
        if (res) {
            emailUtil.sendHtmlMail(email, "パスワードをリセットします",
                    "<div style=\" border-bottom: 1px dotted black; width: 500px; padding-bottom: 10px \">\n" +
                            "        <p>こんにちは！ " + firstname + "：</p>\n" +
                            "        <p>登録アカウントのパスワードを再設定します。</p>\n" +
                            "        <p>この操作に心当たりがない場合、本メールは無視してください。</p>\n" +
                            "        <p>パスワードをリセットするには下記のリンクをクリックします。</p>\n" +
                            "        <p>このリンクは30分で切れますので、至急パスワードの変更をお願いします。</p>\n" +
                            "        <a href=\"http://localhost:8000/account/reset?token=" + token + "\">パスワードを変更します</a>\n" +
                            "      </div>\n" +
                            "      <div>\n" +
                            "        <p>本メールは自動返信メールです。ご返信をご遠慮してください。</p>\n" +
                            "      </div>");
            return ResultMsg.ok().result(true);
        } else {
            return ResultMsg.error().result(false).message("error in resetpasswordservice line34");
        }
    }

    public ResultMsg resetPassword(String token, String password) {
        try {
            User user = userService.getOne(new QueryWrapper<User>().eq("token", token));
            user.getToken();
            LocalDateTime tokenCreationDate = user.getTokencreationdate();
            if (isTokenExpired(tokenCreationDate)) {
                return ResultMsg.error().result(false).code(100).message("token expired");
            }
           String encodedPassword= DigestUtils.md5DigestAsHex(password.getBytes());
            user.setUpassword(encodedPassword);
            user.setToken(null);
            user.setTokencreationdate(null);
            userService.update(user, new QueryWrapper<User>().eq("userid", user.getUserid()));
            return ResultMsg.ok().result(true).message("successfully updated");
        } catch (Exception e) {

            return ResultMsg.error().result(false).code(100).message("invalid token");
        }

    }

    /**
     * Generate unique token. You may add multiple parameters to create a strong
     * token.
     *
     * @return unique token
     */
    private String generateToken() {
        StringBuilder token = new StringBuilder();
        return token.append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString()).toString();
    }

    /**
     * Check whether the created token expired or not.
     *
     * @param tokenCreationDate
     * @return true or false
     */
    private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        System.out.println(tokenCreationDate);
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES;
    }
}