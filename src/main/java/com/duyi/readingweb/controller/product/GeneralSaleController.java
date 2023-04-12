package com.duyi.readingweb.controller.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.bean.product.GeneralSale;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.entity.product.ProductImg;
import com.duyi.readingweb.service.product.ProductImgService;
import com.duyi.readingweb.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@RestController
public class GeneralSaleController {
    @Autowired
    private ProductService productService;

    @RequestMapping("/api/generalSale")
    public ResultMsg getPath(String firstLevelCategory, String secondLevelCategory, String orderType, String orderAsc, Integer page) {
        System.out.println(firstLevelCategory + secondLevelCategory + orderType + orderAsc + page);
        Long total;
        List<Product> products = new ArrayList<>();
        //没有的时候赋值
        if (orderAsc == null) orderAsc = "desc";
        if (orderType == null) orderType = "recommend";
        if (page == null) page = 1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("limit ").append((page - 1) * 60).append(",").append(60);
        //全局搜索
        if (firstLevelCategory == null || firstLevelCategory.equals("")) {
            total = productService.count(new QueryWrapper<Product>().eq(orderType, 1));
            if (orderAsc.equals("asc")) {
                products = productService.list(new QueryWrapper<Product>().select("idproduct", "newproduct", "productdescription", "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "commentnum", "recommend")
                        .eq(orderType, 1).orderByAsc("newprice").last(stringBuilder.toString()));
            } else {
                products = productService.list(new QueryWrapper<Product>().select("idproduct", "newproduct", "productdescription", "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "commentnum", "recommend")
                        .eq(orderType, 1).orderByDesc("newprice").last(stringBuilder.toString()));
            }
        }//单个category的搜索
        else if (secondLevelCategory == null || secondLevelCategory.equals("")) {
            total = productService.count(new QueryWrapper<Product>().eq("firstLevelCategory", firstLevelCategory).eq(orderType, 1));
            if (orderAsc.equals("asc")) {
                products = productService.list(new QueryWrapper<Product>().select("idproduct", "newproduct", "productdescription", "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "commentnum", "recommend")
                        .eq("firstLevelCategory", firstLevelCategory).eq(orderType, 1).orderByAsc("newprice").last(stringBuilder.toString()));
            } else {
                products = productService.list(new QueryWrapper<Product>().select("idproduct", "newproduct", "productdescription", "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "commentnum", "recommend")
                        .eq("firstLevelCategory", firstLevelCategory).eq(orderType, 1).orderByDesc("newprice").last(stringBuilder.toString()));
            }
        }//两个category的搜索
        else {
            total = productService.count(new QueryWrapper<Product>().eq("firstLevelCategory", firstLevelCategory).eq("secondLevelCategory", secondLevelCategory).eq(orderType, 1));
            if (orderAsc.equals("asc")) {
                products = productService.list(new QueryWrapper<Product>().select("idproduct", "newproduct", "productdescription", "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "commentnum", "recommend")
                        .eq("firstLevelCategory", firstLevelCategory).eq("secondLevelCategory", secondLevelCategory).eq(orderType, 1).orderByAsc("newprice").last(stringBuilder.toString()));
            } else {
                products = productService.list(new QueryWrapper<Product>().select("idproduct", "newproduct", "productdescription", "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "commentnum", "recommend")
                        .eq("firstLevelCategory", firstLevelCategory).eq("secondLevelCategory", secondLevelCategory).eq(orderType, 1).orderByDesc("newprice").last(stringBuilder.toString()));
            }
        }
        List<GeneralSale> resProducts = getGeneralSaleFromProduct(products);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("product", resProducts);
        return ResultMsg.ok().data(map);
    }


    @RequestMapping("api/generalQuery")
    public ResultMsg getProduct(String param,Integer page) {
        Long total = productService.count(new QueryWrapper<Product>().like("productdescription", param));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("limit ").append(0).append(",").append(60);
        List<Product> products = productService.list(new QueryWrapper<Product>().select("idproduct", "newproduct", "productdescription", "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "commentnum", "recommend")
                .like("productdescription", param).last(stringBuilder.toString()));
        List<GeneralSale> resProducts = getGeneralSaleFromProduct(products);
        Map<String, Object> map = new HashMap<>();
        if(total>60) total=60L;
        map.put("total", total);
        map.put("product", resProducts);
        return ResultMsg.ok().data(map);
    }

    public List<GeneralSale> getGeneralSaleFromProduct(List<Product> products) {
        List<GeneralSale> generalSales = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            GeneralSale generalSale = new GeneralSale(products.get(i).getTenpercentoff(), products.get(i).getSecondonehalf(), products.get(i).getHref(),
                    products.get(i).getBigimgsrc(), products.get(i).getNewprice(), products.get(i).getRecommend(),
                    products.get(i).getCommentnum(), products.get(i).getProductdescription(), products.get(i).getNewproduct(),
                    products.get(i).getIdproduct());
            generalSales.add(generalSale);
        }
        return generalSales;
    }
}
