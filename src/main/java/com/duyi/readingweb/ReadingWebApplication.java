package com.duyi.readingweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@MapperScan("com.duyi.readingweb.mapper")
@ServletComponentScan(basePackages = "com.duyi.readingweb")
public class ReadingWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReadingWebApplication.class, args);
    }

}
