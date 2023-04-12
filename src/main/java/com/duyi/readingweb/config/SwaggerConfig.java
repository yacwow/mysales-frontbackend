package com.duyi.readingweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
*@EnableSwagger2 开启swagger功能
* */
@Configuration
@EnableSwagger2
public class SwaggerConfig {


    /**
     * Docket   摘要
     * 文档类型 DocumentationType.SWAGGER_2
     * 文档选择器：api path
     * select()设置apis()和path()
     * apis表示生成哪些controller的接口----查找controller的requestmapping的接口
     *                              apis(RequestHandlerSelectors.any()) 获取所有的
     *                              apis(RequestHandlerSelectors.basePackage("com.duyi.readingweb.controller"))包下所有的
     * path在查找出来的接口中进行删选
     * */
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.duyi.readingweb.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    /**
     * 自定义文档介绍
     * */
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("springboot-mySales-backend")
                .description("this is simple project")
                .version("1.0")
                .build();
    }
}
