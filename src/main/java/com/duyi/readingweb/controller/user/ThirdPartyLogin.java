package com.duyi.readingweb.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.entity.user.User;
import com.duyi.readingweb.jwt.JwtUtil;
import com.duyi.readingweb.service.user.UserService;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.gson.GsonFactory;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookException;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

@RestController
public class ThirdPartyLogin {
    private final Integer CREATE_NEW_ACCOUNT = 22222;
    private static final String FBAPP_ID = "789101305868271";
    private static final String FBAPP_SECRET = "deb7114b1bd8eda363b0121f5ceff7e2";
    @Autowired
    private UserService userService;

    @ApiOperation("谷歌三方登录后端代码")
    @RequestMapping(value = "/api/googleThirdPartyLogin", method = {RequestMethod.GET, RequestMethod.POST})
    private ResultMsg googleThirdPartyLogin(@RequestParam Map<String, Object> params) {
        String idTokenString = (String) params.get("idTokenString");
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier
                .Builder(new NetHttpTransport(), new GsonFactory())
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList("21212525140-297e8up9odcvgt8kturk7j80qij69boq.apps.googleusercontent.com"))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build();
        // (Receive idTokenString by HTTPS POST)
        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString);
        } catch (GeneralSecurityException e) {
            return ResultMsg.ok().result(false).message(e.getMessage());
            //            throw new RuntimeException(e);
        } catch (IOException e) {
            return ResultMsg.ok().result(false).message(e.getMessage());
            //            throw new RuntimeException(e);
        }
        if (idToken != null) {
            Payload payload = idToken.getPayload();
            System.out.println(payload);
            // Print user identifier
//            String userId = payload.getSubject();
//            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
//            String pictureUrl = (String) payload.get("picture");
//            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            // Use or store profile information
            // ...
            if (emailVerified == true) {
                //验证email我们有没有，有就走login同套路，没有就返回null
                User user = userService.getOne(new QueryWrapper<User>().eq("email", email));
                if (user != null) {
                    String token = JwtUtil.createToken(user);
                    user.setLastlogin(DateTime.now().toDate());
                    userService.update(user, new QueryWrapper<User>().eq("userid", user.getUserid()));
                    return ResultMsg.ok().result(true).data("token", token);
                } else {
                    //创建一个账户给他，别再麻烦他注册了,返回密码，密码是名字+随机四位数
                  String password=  createUserAccount(name,email,givenName,familyName);
                    return ResultMsg.ok().result(true).code(CREATE_NEW_ACCOUNT).data("password",password);
                }
            } else {
                return ResultMsg.ok().result(false).message("Invalid ID token.");
            }

        } else {
            return ResultMsg.ok().result(false).message("Invalid ID token.");
        }

    }

    @ApiOperation("FB三方登录后端代码")
    @RequestMapping(value = "/api/faceBookThirdPartyLogin", method = {RequestMethod.GET, RequestMethod.POST})
    private ResultMsg faceBookThirdPartyLogin(@RequestParam Map<String, Object> params) {
        String FBTokenString = (String) params.get("FBTokenString");
        FacebookClient facebookClient = new DefaultFacebookClient(FBTokenString, FBAPP_SECRET, Version.LATEST);
        try {
            com.restfb.types.User user = facebookClient.fetchObject("me", com.restfb.types.User.class,
                    Parameter.with("fields", "id,name,email"));
            System.out.println(user);
            String email=user.getEmail();
            User user1 = userService.getOne(new QueryWrapper<User>().eq("email", email));
            if (user1 != null) {
                String token = JwtUtil.createToken(user1);
                user1.setLastlogin(DateTime.now().toDate());
                userService.update(user1, new QueryWrapper<User>().eq("userid", user1.getUserid()));
                return ResultMsg.ok().result(true).data("token", token);
            } else {

                return ResultMsg.ok().result(false).code(CREATE_NEW_ACCOUNT);
            }
        } catch (FacebookException e) {
            return ResultMsg.error().result(false).message(e.getMessage());
        }
    }



    public String createUserAccount(String name, String email,String givenName,String familyName){
        // 生成随机四位数字
        Random random = new Random();
        int randomNum = random.nextInt(9000) + 1000;
        String password = name + randomNum;
        String digestPassword= DigestUtils.md5DigestAsHex(password.getBytes());
        User user1=new User();
        user1.setEmail(email);
        user1.setFirstname(givenName);
        user1.setLastname(familyName);
        user1.setUpassword(digestPassword);
        user1.setLastlogin(DateTime.now().toDate());
        user1.setDeduction(1000);
        user1.setAdvertise(false);
        user1.setLevel("VIP1");
        userService.save(user1);
        return password;
    }
}
