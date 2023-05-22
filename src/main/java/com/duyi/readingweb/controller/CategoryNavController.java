package com.duyi.readingweb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.entity.CategoryManagement;
import com.duyi.readingweb.entity.eventManagement.SpecialEvent;
import com.duyi.readingweb.service.CategoryManagementService;
import com.duyi.readingweb.service.eventManagement.SpecialEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CategoryNavController {
    @Autowired
    private CategoryManagementService categoryManagementService;
    @Autowired
    private SpecialEventService specialEventService;

    @RequestMapping(value = "/api/getCategoryNav")
    private ResultMsg getCategoryNav() {
        List<CategoryManagement> categoryManagementList = categoryManagementService
                .list(new QueryWrapper<CategoryManagement>()
                        .select("title", "firstlevelcategory", "secondlevelcategory", "href", "categoryimgurl")
                        .isNotNull("secondlevelcategory")
                        .ne("secondlevelcategory", ""));
        Map<String, List<CategoryManagement>> result = categoryManagementList.stream()
                .collect(Collectors.groupingBy(CategoryManagement::getFirstlevelcategory));
        Map<String, Object> resMap = new HashMap<>();
        for (Map.Entry<String, List<CategoryManagement>> entry : result.entrySet()) {
            String firstlevelcategory = entry.getKey();
            List<CategoryManagement> secondlevelcategories = entry.getValue();
            // 对每个firstlevelcategory和对应的secondlevelcategories进行处理
//            System.out.println(secondlevelcategories);
            List<Object> list = new ArrayList<>();
            for (int i = 0; i < secondlevelcategories.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("secondLevel", secondlevelcategories.get(i).getSecondlevelcategory());
                map.put("title", secondlevelcategories.get(i).getTitle());
                map.put("href", secondlevelcategories.get(i).getHref());
                map.put("categoryImg", secondlevelcategories.get(i).getCategoryimgurl());
                list.add(map);
            }
            List<CategoryManagement> categoryManagements = categoryManagementService
                    .list(new QueryWrapper<CategoryManagement>()
                            .select("title", "firstlevelcategory", "href")
                            .eq("firstlevelcategory", firstlevelcategory)
                            .isNull("secondlevelcategory").or().eq("secondlevelcategory", ""));
            Map<String,Object> firstMap=new HashMap<>();
            firstMap.put("list",list);
            firstMap.put("title",categoryManagements.get(0).getTitle());
            firstMap.put("href",categoryManagements.get(0).getHref());

            resMap.put(firstlevelcategory, firstMap);
        }
        //然后对specialevent的newProduct进行搜索
        SpecialEvent specialEvent = specialEventService.getOne(new QueryWrapper<SpecialEvent>()
                .eq("specialcode", "newProduct"));
        Map<String, Object> special = new HashMap<>();
        special.put("secondLevel", "newProduct");
        special.put("title", "人気新作");
        special.put("href", "/dailyNew");
        special.put("categoryImg", specialEvent.getImgurl());
        List<Object> newProduct = new ArrayList<>();
        newProduct.add(special);
        Map<String,Object> newProductMap=new HashMap<>();
        newProductMap.put("list",newProduct);
        newProductMap.put("title","人気新作");
        newProductMap.put("href","/dailyNew");
        resMap.put("newProduct", newProductMap);
        return ResultMsg.ok().result(true).data(resMap);
    }
}
