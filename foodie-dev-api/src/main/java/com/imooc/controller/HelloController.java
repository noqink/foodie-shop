package com.imooc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    @GetMapping(path = "/hello")
    public String hello(){
        logger.info("info:1");
        logger.debug("debug:2");
        logger.error("error:3");
        logger.warn("warn:4");
        return "hello";
    }
}
