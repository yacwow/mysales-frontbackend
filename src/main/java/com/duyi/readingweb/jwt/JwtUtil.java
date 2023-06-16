package com.duyi.readingweb.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.duyi.readingweb.entity.user.User;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    //秘钥，到时候改
    private static final String SECRET = "my_secret";

    //    过期时间
    private static final Long EXPIRATION = 180000000L;

    //    生成token
    public static String createToken(User user) {
        Date expireDate = new Date(System.currentTimeMillis() + EXPIRATION * 1000);
        Map<String, Object> map = new HashMap<>();
        map.put("alg", "HS256");
        map.put("typ", "jwt");//头部信息
        System.out.println(user);
        String lastName=user.getLastname();
        String firstName=user.getFirstname();
        String email=user.getEmail();
        Integer deduction=user.getDeduction();
        String token = JWT.create()
                .withHeader(map)
//                .withClaim("id", user.getUserid())
                .withClaim("name", lastName+","+firstName)
                .withClaim("email", email)
                .withClaim("deduction",deduction)
                .withExpiresAt(expireDate)
                .withIssuedAt(new Date())
                .sign(Algorithm.HMAC256(SECRET));
        return token;
    }

    //    解析token
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
            jwt = verifier.verify(token);
        } catch (Exception e) {
//            logger.error(e.getMessage());
            logger.error("token解码异常");
            return null;
        }
        return jwt.getClaims();
    }
}
