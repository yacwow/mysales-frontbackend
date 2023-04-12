package com.duyi.readingweb.controller.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.bean.product.DailyNewProduct;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

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
        List<Product> products=new ArrayList<>();
        if (params == null || params.equals("ALL")) {
            System.out.println("in1");
            //找所有的newproduct
            number = productService.count(new QueryWrapper<Product>().eq("newproduct", 1));
            products = productService.list(new QueryWrapper<Product>().select("recommend","productdescription", "newprice",
                            "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "marketprice").eq("newproduct", 1)
                    .last(stringBuilder.toString()));
        } else if (params.equals("bagGoods")) {
            System.out.println("in2");
            //secondlevel category
            number = productService.count(new QueryWrapper<Product>().eq("newproduct", 1).eq("secondlevelcategory",params));
            products = productService.list(new QueryWrapper<Product>().select("recommend","productdescription", "newprice",
                            "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "marketprice").eq("newproduct", 1).eq("secondlevelcategory",params)
                    .last(stringBuilder.toString()));
        } else {
            //firstlevel category
            number = productService.count(new QueryWrapper<Product>().eq("newproduct", 1).eq("firstlevelcategory",params));
            products = productService.list(new QueryWrapper<Product>().select("recommend","productdescription", "newprice",
                            "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "marketprice").eq("newproduct", 1).eq("firstlevelcategory",params)
                    .last(stringBuilder.toString()));
        }
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                if(o1.getRecommend()==null) return -1;
                return o2.getRecommend()-o1.getRecommend();
            }
        });
        for (int i = 0; i < products.size(); i++) {
            dailyNewProducts.add(new DailyNewProduct(products.get(i).getProductdescription(),products.get(i).getTenpercentoff(),
                    products.get(i).getSecondonehalf(),products.get(i).getHref(),products.get(i).getBigimgsrc(),products.get(i).getNewprice(),products.get(i).getMarketprice()));
        }
        Map<String,Object> map =new HashMap<>();
        map.put("total",number);
        map.put("dailyNewProduct",dailyNewProducts);


        return ResultMsg.ok().data(map);
    }
}
