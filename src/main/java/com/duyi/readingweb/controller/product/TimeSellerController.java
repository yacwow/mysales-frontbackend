package com.duyi.readingweb.controller.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.bean.product.TimeSeller;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.entity.product.ProductImg;
import com.duyi.readingweb.service.product.ProductImgService;
import com.duyi.readingweb.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Predicate;

@RestController
public class TimeSellerController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/api/timeSeller")
    public ResultMsg getTimeSeller() {
        String[] strings = new String[]{"topGeneral", "dressGeneral", "bottomGeneral", "outerGeneral", "setUp", "generalShoes"};
        List<Product> totalProduct = productService.list(new QueryWrapper<Product>().select("firstlevelCategory", "idproduct", "secondOneHalf", "href",
                        "marketPrice", "newPrice", "stock", "recommend","bigimgsrc","timeseller").gt("timeseller", 0));
        totalProduct.sort(new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o2.getTimeseller()-o1.getTimeseller();
            }
        });
        Map<String, Object> resMap = new HashMap<>();
        for (int i = 0; i < strings.length; i++) {
            int finalI = i;
            List<Product> res = totalProduct.stream().filter(new Predicate<Product>() {
                @Override
                public boolean test(Product product) {
                    return product.getFirstlevelcategory().equals(strings[finalI]);
                }
            }).toList();
            List<TimeSeller> timeSellers = getTimeSeller(res);
            resMap.put(strings[finalI], timeSellers);
        }
        List<Product>  restProducts= totalProduct.stream().filter(new Predicate<Product>() {
            @Override
            public boolean test(Product product) {
                return !Arrays.asList(strings).contains(product.getFirstlevelcategory());
            }
        }).toList();
        List<TimeSeller> restTimeSellers = getTimeSeller(restProducts);
        resMap.put("other",restTimeSellers);
        return ResultMsg.ok().data(resMap);
    }

    @Autowired
    private ProductImgService productImgService;

    //根据productList的集合找到timeseller集合
    private List<TimeSeller> getTimeSeller(List<Product> productList) {
        List<TimeSeller> productWithImgSrcList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            TimeSeller timeSeller = new TimeSeller(productList.get(i).getSecondonehalf(),
                    productList.get(i).getHref(), productList.get(i).getBigimgsrc(), productList.get(i).getMarketprice(), productList.get(i).getNewprice(),
                    productList.get(i).getStock(), productList.get(i).getRecommend());
            productWithImgSrcList.add(timeSeller);
        }

        return productWithImgSrcList;
    }

}
