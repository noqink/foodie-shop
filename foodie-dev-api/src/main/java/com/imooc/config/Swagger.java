package com.imooc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger {

    // swagger-ui地址
    // http://localhost:8088/swagger-ui.html
    // http://localhost:8088/doc.html
    @Bean
    // docket Swagger2 核心配置
    public Docket getDocket(){

        return new Docket(DocumentationType.SWAGGER_2)  // 指定api类型为SWAGGER_2
                .apiInfo(getApiInfo())                  // 用于定义api文档汇总信息
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.imooc.controller"))
                .paths(PathSelectors.any()).build();    // 该controller包下所有请求
    }

    public ApiInfo getApiInfo(){

        return new ApiInfoBuilder()
                .title("电商")
                .contact(new Contact("天天吃货",
                        "https://www.imooc.com",
                        "1138614092@qq.com"))
                .description("电商api")
                .termsOfServiceUrl("https://www.imooc.com")
                .version("1.0.0").build();
    }
}
