package com.duyi.readingweb.controller;

import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.entity.Producttype;
import com.duyi.readingweb.service.ProducttypeService;
import com.duyi.readingweb.service.imple.ProducttypeServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StringUtils;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PageFrameController {
    @Autowired
//    private ProducttypeService producttypeService;
    @RequestMapping("/api/getNavline")
    public ResultMsg getNavline(){
        return null;
    }
}
