package com.duyi.readingweb.controller.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.bean.product.TimeSeller;
import com.duyi.readingweb.entity.eventManagement.SpecialEventDetail;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.entity.product.ProductImg;
import com.duyi.readingweb.service.eventManagement.SpecialEventDetailService;
import com.duyi.readingweb.service.product.ProductImgService;
import com.duyi.readingweb.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
public class TimeSellerController {

    @Autowired
    private ProductService productService;
    @Autowired
    private SpecialEventDetailService specialEventDetailService;

    @RequestMapping("/api/timeSeller")
    public ResultMsg getTimeSeller() {
        String[] strings = new String[]{"topGeneral", "dressGeneral", "bottomGeneral", "outerGeneral", "setUp", "generalShoes"};
        List<SpecialEventDetail> specialEventDetailList = specialEventDetailService.list(new QueryWrapper<SpecialEventDetail>()
                .eq("specialcode", "timeseller").select("productid", "categoryRankNum").orderByDesc("categoryRankNum"));
        List<Integer> productIDList = specialEventDetailList.stream()
                .map(SpecialEventDetail::getProductid).collect(Collectors.toList());
        List<Product> totalProduct = productService.list(new QueryWrapper<Product>()
                .select("firstlevelCategory", "idproduct", "secondOneHalf", "href",
                        "marketPrice", "newPrice", "stock", "bigimgsrc")
                .in("idproduct", productIDList));
        // create a LinkedHashMap with productID as key and Product object as value
        Map<Integer, Product> productMap = new LinkedHashMap<>();
        for (Product product : totalProduct) {
            productMap.put(product.getIdproduct(), product);
        }

        // sort totalProduct according to the order of productID values in specialEventDetailList
        List<Map<String,Object>> sortedMapList=new ArrayList<>();
        int index=specialEventDetailList.size()+1;
        for (SpecialEventDetail specialEventDetail : specialEventDetailList) {
            int productId = specialEventDetail.getProductid();
            Product product = productMap.get(productId);
            Map<String,Object> map=new HashMap<>();
            map.put("product",product);
            map.put("index",index--);
            sortedMapList.add(map);
        }

        Map<String, Object> resMap = new HashMap<>();
        for (int i = 0; i < strings.length; i++) {
            int finalI = i;
            List<Map<String,Object>> res = sortedMapList.stream().filter(new Predicate<Map<String,Object>>() {
                @Override
                public boolean test(Map<String,Object> map) {
                    return ((Product)map.get("product")).getFirstlevelcategory().equals(strings[finalI]);
                }
            }).toList();
            List<TimeSeller> timeSellers = getTimeSeller(res);
            resMap.put(strings[finalI], timeSellers);
        }
        // 找出 generalShoes
        List<Map<String,Object>> restMap = sortedMapList.stream().filter(new Predicate<Map<String,Object>>() {
            @Override
            public boolean test(Map<String,Object> map) {
                return !Arrays.asList(strings).contains(((Product)map.get("product")).getFirstlevelcategory());
            }
        }).toList();
        List<TimeSeller> restTimeSellers = getTimeSeller(restMap);
        resMap.put("other", restTimeSellers);
        return ResultMsg.ok().data(resMap);
    }



    private List<TimeSeller> getTimeSeller(List<Map<String,Object>> resMapList) {
        List<TimeSeller> productWithImgSrcList = new ArrayList<>();
        for (int i = 0; i < resMapList.size(); i++) {
            Product product=(Product)resMapList.get(i).get("product");
            Integer index=(Integer)resMapList.get(i).get("index");
            TimeSeller timeSeller = new TimeSeller(product.getSecondonehalf(),
                    product.getHref(), product.getBigimgsrc(), product.getMarketprice(), product.getNewprice(),
                    product.getStock(), index);
            productWithImgSrcList.add(timeSeller);
        }

        return productWithImgSrcList;
    }
//

}
