package com.duyi.readingweb.controller.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.bean.product.DailyNewProduct;
import com.duyi.readingweb.entity.eventManagement.SpecialEventDetail;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.service.eventManagement.SpecialEventDetailService;
import com.duyi.readingweb.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class DailyNewProductController {
    @Autowired
    private ProductService productService;
    @RequestMapping("/api/dailyNew")
    public ResultMsg getDailyNew(String params, Integer page) {
        System.out.println("in1");
        StringBuilder stringBuilder = new StringBuilder();
        if (page == null) {
            page = 1;
        }
        stringBuilder.append("limit ").append((page - 1) * 60).append(" , 60");
        Long number;
        List<DailyNewProduct> dailyNewProducts = new ArrayList<>();
        List<Product> products ;
        if (params == null || params.equals("ALL")) {
            number = productService.count(new QueryWrapper<Product>()
                    .gt("tenpercentoff", 0));
            //根据productidlist找到product信息
            products = productService.list(new QueryWrapper<Product>()
                    .select("productdescription", "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "marketprice")
                    .gt("tenpercentoff", 0).orderByDesc("tenpercentoff").last(stringBuilder.toString()));
        } else if (params.equals("bagGoods")) {
            //secondlevel category
            number = productService.count(new QueryWrapper<Product>().gt("tenpercentoff", 0).eq("secondlevelcategory", params));

            products = productService.list(new QueryWrapper<Product>()
                    .select("productdescription", "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "marketprice")
                    .gt("tenpercentoff", 0).eq("secondlevelcategory", params)
                    .orderByDesc("tenpercentoff").last(stringBuilder.toString()));
        } else {
            //firstlevel category
            number = productService.count(new QueryWrapper<Product>().gt("tenpercentoff", 0).eq("firstlevelcategory", params));

            products = productService.list(new QueryWrapper<Product>()
                    .select("productdescription", "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "marketprice")
                    .gt("tenpercentoff", 0).eq("firstlevelcategory", params)
                    .orderByDesc("tenpercentoff").last(stringBuilder.toString()));
        }

        for (int i = 0; i < products.size(); i++) {
            dailyNewProducts.add(new DailyNewProduct(products.get(i).getProductdescription(),
                    products.get(i).getTenpercentoff()==0?0:1,
                    products.get(i).getSecondonehalf(),
                    products.get(i).getHref(),
                    products.get(i).getBigimgsrc(),
                    products.get(i).getNewprice(),
                    products.get(i).getMarketprice()));
        }
        Map<String, Object> map = new HashMap<>();
        map.put("total", number);
        map.put("dailyNewProduct", dailyNewProducts);


        return ResultMsg.ok().data(map);
    }
}
