package com.duyi.readingweb.controller.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.service.product.ProductService;
import net.bytebuddy.matcher.FilterableList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class SecondHalfController {
    @Autowired
    private ProductService productService;

    @RequestMapping("/api/getSecondHalfPageData/{page}")
    public ResultMsg getSecondHalfPageData(@PathVariable("page") Integer page) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("limit ").append((page - 1) * 60).append(",60");
        List<Product> productList = productService.list(new QueryWrapper<Product>().eq("secondonehalf", 1)
                .select("bigimgsrc","href","tenpercentoff","newprice","originprice","stock","productdescription")
                .last(stringBuilder.toString()));
        List<Map<String, Object>> secondHalfPageData = new ArrayList<>();
        Integer number=0;
        if(page==1){
            number  =(int)productService.count(new QueryWrapper<Product>().eq("secondonehalf", 1));
        }

        for (int i = 0; i < productList.size(); i++) {
            Map<String, Object> secondHalfPage=new HashMap<>();
            secondHalfPage.put("imgSrc",productList.get(i).getBigimgsrc());
            secondHalfPage.put("href",productList.get(i).getHref());
            secondHalfPage.put("discount",productList.get(i).getTenpercentoff());
            secondHalfPage.put("currentPrice",productList.get(i).getNewprice());
            secondHalfPage.put("originPrice",productList.get(i).getOriginprice());
            secondHalfPage.put("stockNum",productList.get(i).getStock());
            secondHalfPage.put("description",productList.get(i).getProductdescription());
            secondHalfPageData.add(secondHalfPage);
        }
        Map<String,Object> resMap=new HashMap<>();
        resMap.put("secondHalfPageData",secondHalfPageData);
        resMap.put("number",number);
        if(page==1){
            return ResultMsg.ok().result(true).data(resMap);
        }else{
            return ResultMsg.ok().result(true).data("secondHalfPageData",secondHalfPageData);
        }

    }
}
