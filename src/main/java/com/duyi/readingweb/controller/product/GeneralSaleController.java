package com.duyi.readingweb.controller.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.bean.product.GeneralSale;
import com.duyi.readingweb.entity.eventManagement.SpecialEventDetail;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.entity.product.ProductImg;
import com.duyi.readingweb.service.eventManagement.SpecialEventDetailService;
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
@Autowired
private SpecialEventDetailService specialEventDetailService;


    @RequestMapping("/api/generalSale")
    public ResultMsg getPath(String firstLevelCategory, String secondLevelCategory, String orderType, String orderAsc, Integer page) {
        System.out.println(firstLevelCategory + secondLevelCategory + orderType + orderAsc + page);
        if(page==null){
            page=1;
        }
        Long total;
        List<Product> products ;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("limit ").append((page - 1) * 60).append(",").append(60);
        if(secondLevelCategory==null|| secondLevelCategory.equals("")){
            total=productService.count(new QueryWrapper<Product>().eq("firstlevelcategory",firstLevelCategory));
            if(orderType.equals("recommend")||orderType==null){
                //            根据levelcategoryrank来排序
                products = productService.list(new QueryWrapper<Product>().select("idproduct",  "productdescription",
                                "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "firstlevelcategoryrank", "fraudcommentnum")
                        .orderByDesc("firstlevelcategoryrank").eq("firstlevelcategory",firstLevelCategory).last(stringBuilder.toString()));
            } else if (orderType.equals("newproduct")||orderType.equals("newProduct")) {
                products = productService.list(new QueryWrapper<Product>().select("idproduct", "productdescription",
                                "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "firstlevelcategoryrank", "fraudcommentnum")
                        .eq("firstlevelcategory",firstLevelCategory).orderByDesc("tenpercentoff").last(stringBuilder.toString()));
            }else{
                //根据价格来，那还要分价格高低排序
                if(orderAsc==null||orderAsc.equals("asc")){
                    products = productService.list(new QueryWrapper<Product>().select("idproduct",  "productdescription",
                                    "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "firstlevelcategoryrank", "fraudcommentnum")
                            .eq("firstlevelcategory",firstLevelCategory).orderByAsc("newprice").last(stringBuilder.toString()));
                }else{
                    products = productService.list(new QueryWrapper<Product>().select("idproduct","productdescription",
                                    "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "firstlevelcategoryrank", "fraudcommentnum")
                            .eq("firstlevelcategory",firstLevelCategory).orderByDesc("newprice").last(stringBuilder.toString()));
                }
            }
        }else{
            total=productService.count(new QueryWrapper<Product>().eq("firstlevelcategory",firstLevelCategory)
                    .eq("secondlevelcategory",secondLevelCategory));
            //有二级目录
            if(orderType.equals("recommend")||orderType==null){
                //根据secondlevelcategoryrank来排序
                products = productService.list(new QueryWrapper<Product>().select("idproduct",  "productdescription",
                                "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "firstlevelcategoryrank", "fraudcommentnum")
                        .orderByDesc("secondlevelcategoryrank").eq("firstlevelcategory",firstLevelCategory)
                        .eq("secondlevelcategory",secondLevelCategory).last(stringBuilder.toString()));
            } else if (orderType.equals("newproduct")||orderType.equals("newProduct")) {
                products = productService.list(new QueryWrapper<Product>().select("idproduct","productdescription",
                                "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "firstlevelcategoryrank", "fraudcommentnum")
                        .eq("firstlevelcategory",firstLevelCategory).orderByDesc("tenpercentoff")
                        .eq("secondlevelcategory",secondLevelCategory).last(stringBuilder.toString()));
            }else{
                //根据价格来，那还要分价格高低排序
                if(orderAsc==null||orderAsc.equals("asc")){
                    products = productService.list(new QueryWrapper<Product>().select("idproduct", "productdescription",
                                    "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "firstlevelcategoryrank", "fraudcommentnum")
                            .eq("firstlevelcategory",firstLevelCategory).orderByAsc("newprice")
                            .eq("secondlevelcategory",secondLevelCategory).last(stringBuilder.toString()));
                }else{
                    products = productService.list(new QueryWrapper<Product>().select("idproduct",  "productdescription",
                                    "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "firstlevelcategoryrank", "fraudcommentnum")
                            .eq("firstlevelcategory",firstLevelCategory).orderByDesc("newprice")
                            .eq("secondlevelcategory",secondLevelCategory).last(stringBuilder.toString()));
                }
            }
        }

        List<GeneralSale> resProducts = getGeneralSaleFromProduct(products);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("product", resProducts);
        return ResultMsg.ok().data(map);
    }

    @RequestMapping("api/generalQuery")
    public ResultMsg getProduct(String param, String orderType, String orderAsc, Integer page) {
        if(page==null){
            page=1;
        }
        List<Product> products;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("limit ").append((page - 1) * 60).append(",").append(60);
        Long total = productService.count(new QueryWrapper<Product>().like("productdescription", param));
        if(orderType.equals("recommend")||orderType==null){
            //            根据levelcategoryrank来排序
            products = productService.list(new QueryWrapper<Product>().select("idproduct",  "productdescription",
                            "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "firstlevelcategoryrank", "fraudcommentnum")
                    .orderByDesc("firstlevelcategoryrank").like("productdescription", param).last(stringBuilder.toString()));
        } else if (orderType.equals("newproduct")||orderType.equals("newProduct")) {
            products = productService.list(new QueryWrapper<Product>().select("idproduct", "productdescription",
                            "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "firstlevelcategoryrank", "fraudcommentnum")
                    .like("productdescription", param).orderByDesc("tenpercentoff").last(stringBuilder.toString()));
        }else{
            //根据价格来，那还要分价格高低排序
            if(orderAsc==null||orderAsc.equals("asc")){
                products = productService.list(new QueryWrapper<Product>().select("idproduct",  "productdescription",
                                "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "firstlevelcategoryrank", "fraudcommentnum")
                        .like("productdescription", param).orderByAsc("newprice").last(stringBuilder.toString()));
            }else{
                products = productService.list(new QueryWrapper<Product>().select("idproduct",  "productdescription",
                                "newprice", "href", "tenpercentoff", "secondonehalf", "bigimgsrc", "firstlevelcategoryrank", "fraudcommentnum")
                        .like("productdescription", param).orderByDesc("newprice").last(stringBuilder.toString()));
            }
        }

        List<GeneralSale> resProducts = getGeneralSaleFromProduct(products);
        Map<String, Object> map = new HashMap<>();
//        if (total > 60) total = 60L;
        map.put("total", total);
        map.put("product", resProducts);
        return ResultMsg.ok().data(map);
    }

    public List<GeneralSale> getGeneralSaleFromProduct(List<Product> products) {
        List<GeneralSale> generalSales = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            GeneralSale generalSale = new GeneralSale(
                    products.get(i).getTenpercentoff()==0?0:1,
                    products.get(i).getSecondonehalf(),
                    products.get(i).getHref(),
                    products.get(i).getBigimgsrc(),
                    products.get(i).getNewprice(),
                    products.size()-i,
                    products.get(i).getFraudcommentnum(),
                    products.get(i).getProductdescription(),
                    products.get(i).getTenpercentoff()==0?0:1,
                    products.get(i).getIdproduct());
            generalSales.add(generalSale);
        }
        return generalSales;
    }
}
