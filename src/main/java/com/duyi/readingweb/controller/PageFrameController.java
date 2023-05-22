package com.duyi.readingweb.controller;

import com.duyi.readingweb.bean.ResultMsg;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class PageFrameController {

    @RequestMapping("/api/getNavline")
    public ResultMsg getNavline(){
        return null;
    }
}
