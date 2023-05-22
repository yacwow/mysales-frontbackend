package com.duyi.readingweb.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.entity.eventManagement.SpecialEventDetail;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.entity.product.UserProductCartdetail;
import com.duyi.readingweb.entity.product.UserProductWishlist;
import com.duyi.readingweb.service.eventManagement.SpecialEventDetailService;
import com.duyi.readingweb.service.product.ProductService;
import com.duyi.readingweb.service.product.UserProductCartdetailService;
import com.duyi.readingweb.service.product.UserProductWishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.duyi.readingweb.bean.ResultCode.NotEXIT;

@RestController
public class WishListController {
    @Autowired
    private UserProductWishlistService userProductWishlistService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserProductCartdetailService userProductCartdetailService;

    @RequestMapping("/api/secure/getMyWishList")
    private ResultMsg getMyWishList(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        List<UserProductWishlist> userProductWishlistList = userProductWishlistService
                .list(new QueryWrapper<UserProductWishlist>()
                        .eq("useremail", email).select("productid"));
        if(userProductWishlistList.size()==0){
            return ResultMsg.ok().result(true).code(NotEXIT);
        }
        List<Integer> products = userProductWishlistList.stream()
                .map(userProductWishlist -> userProductWishlist.getProductid())
                .collect(Collectors.toList());
        List<Product> productList = productService.list(new QueryWrapper<Product>().in("idproduct", products)
                .select("idproduct", "href", "bigimgsrc", "newprice", "productdescription", "firstlevelcategory"));
        List<Map<String, Object>> wishList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            List<UserProductCartdetail> userProductCartdetailList = userProductCartdetailService
                    .list(new QueryWrapper<UserProductCartdetail>()
                            .eq("useremail", email).eq("productid_cart", productList.get(i).getIdproduct()));
            if (userProductCartdetailList.size() > 0) {
                map.put("inCart", true);
            } else {
                map.put("inCart", false);
            }
            map.put("href", productList.get(i).getHref());
            map.put("productId", productList.get(i).getIdproduct());
            map.put("bigImgSrc", productList.get(i).getBigimgsrc());
            map.put("price", productList.get(i).getNewprice());
            map.put("productDescription", productList.get(i).getProductdescription());
            map.put("firstLevelCategory", productList.get(i).getFirstlevelcategory());
            wishList.add(map);
        }
        return ResultMsg.ok().result(true).data("wishList", wishList);
    }


    @RequestMapping("/api/secure/deleteOneWishList/{productId}")
    private ResultMsg deleteOneWishList(HttpServletRequest request, @PathVariable String productId) {
        String email = (String) request.getAttribute("email");
        userProductWishlistService.remove(new QueryWrapper<UserProductWishlist>()
                .eq("productid", productId).eq("useremail", email));
        return ResultMsg.ok().result(true);
    }

    @Autowired
    private SpecialEventDetailService specialEventDetailService;

    @RequestMapping("/api/secure/bestSellerWhenWishListEmpty")
    private ResultMsg bestSellerWhenWishListEmpty() {
        List<SpecialEventDetail> specialEventDetailList = specialEventDetailService
                .list(new QueryWrapper<SpecialEventDetail>().eq("specialcode", "bestSeller")
                        .select("productid").orderByDesc("categoryRankNum")
                        .last("limit 0,18"));
        List<Integer> productIds = specialEventDetailList.stream().map(specialEventDetail -> specialEventDetail.getProductid())
                .collect(Collectors.toList());
     List<Product> productList=  productService.list(new QueryWrapper<Product>().in("idproduct",productIds)
                .select("newprice","marketprice","bigimgsrc","href"));
     List<Map<String,Object>> list=new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            Map<String,Object> map=new HashMap<>();
            map.put("price",productList.get(i).getNewprice());
            map.put("href",productList.get(i).getHref());
            map.put("marketPrice",productList.get(i).getMarketprice());
            map.put("imgSrc",productList.get(i).getBigimgsrc());
            list.add(map);
        }
        return ResultMsg.ok().result(true).data("bestSellerData",list);
    }
}
