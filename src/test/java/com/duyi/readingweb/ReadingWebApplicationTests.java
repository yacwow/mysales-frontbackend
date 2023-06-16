package com.duyi.readingweb;

import com.duyi.readingweb.util.user.sendEmail.EmailUtil;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class ReadingWebApplicationTests {
    @Resource
    StringEncryptor encryptor;

//    @Test
//    public void encrypt() {
//        String url = encryptor.encrypt("jdbc:mysql://localhost:3306/mysales?characterEncoding=UTF-8&serverTimezone=UTC");
//        String username = encryptor.encrypt("root");
//        String pwd = encryptor.encrypt("Tiancai123~");
//        System.out.println("url = " + url);
//        System.out.println("username = " + username);
//        System.out.println("pwd = " + pwd);
//    }
    @Autowired
    private EmailUtil mailService;
//EmailUtil mailService=new EmailUtil();
    /**
     * 测试发送文本邮件
     */
//    @Test
//    public void sendmail() {
//        mailService.sendSimpleMail("HAOCHENBC@GMAIL.COM", "主题：你好普通邮件", "内容：第一封邮件");
//    }

//    @Test
//    public void sendmailHtml() {
//        mailService.sendHtmlMail("HAOCHENBC@GMAIL.COM", "主题：你好html邮件", "<h1>内容：第一封html邮件</h1>");
//    }

}
