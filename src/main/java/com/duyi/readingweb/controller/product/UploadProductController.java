package com.duyi.readingweb.controller.product;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.entity.product.UserProductCartdetail;
import com.duyi.readingweb.entity.product.UserProductWishlist;
import com.duyi.readingweb.service.product.UserProductCartdetailService;
import com.duyi.readingweb.service.product.UserProductWishlistService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class UploadProductController {
    @Autowired
    private UserProductCartdetailService userProductCartdetailService;
    @Autowired
    private UserProductWishlistService userProductWishlistService;

    @RequestMapping(value = "/api/secure/uploadProductListForLogin", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg uploadProductListForLogin(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        System.out.println(params);
        String email = (String) request.getAttribute("email");
        Boolean flag = true;
        Set<String> keyset = params.keySet();

        for (String set : keyset) {
            Map<String, Object> param = (Map<String, Object>) JSON.parse((String) params.get(set));
            Integer productId = (Integer) param.get("productId");
            if (productId == 8888888) continue;
            String color = (String) param.get("color");
            String size = (String) param.get("size");
            Integer amount = (Integer) param.get("amount");
            Boolean thisTime = updateOrSaveUserProductDetail(productId, color, size, email, amount);
            if (thisTime == false) flag = false;

//            UserProductCartdetail userProductCartdetail = userProductCartdetailService.getOne(new QueryWrapper<UserProductCartdetail>().eq("productid_cart", productId)
//                    .eq("productcolor", color).eq("productsize", size).eq("useremail", email));
//
//            UserProductCartdetail newUserProductCartdetail = new UserProductCartdetail();
//
//            newUserProductCartdetail.setUseremail(email);
//            newUserProductCartdetail.setProductidCart(productId);
//            newUserProductCartdetail.setProductcolor(color);
//            newUserProductCartdetail.setProductsize(size);
//            if (userProductCartdetail == null) {
//                newUserProductCartdetail.setProductamount(amount);
//                Boolean thistime = userProductCartdetailService.save(newUserProductCartdetail);
//                if (thistime == false) flag = false;
//            } else {
//                newUserProductCartdetail.setProductamount(amount + userProductCartdetail.getProductamount());
//                Boolean thistime = userProductCartdetailService.update(newUserProductCartdetail, new QueryWrapper<UserProductCartdetail>().eq("productid_cart", productId)
//                        .eq("productcolor", color).eq("productsize", size).eq("useremail", email));
//                if (thistime == false) flag = false;
//            }

        }
        return ResultMsg.ok().result(flag);
    }

    @RequestMapping(value = "/api/secure/uploadProductListAfterLogin", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg uploadProductListAfterLogin(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        System.out.println(params);
        String email = (String) request.getAttribute("email");
        Integer productId = Integer.parseInt((String) params.get("productId"));
        String color = (String) params.get("color");
        String size = (String) params.get("size");
        Integer amount = Integer.parseInt((String) params.get("amount"));
        Boolean thisTime = updateOrSaveUserProductDetail(productId, color, size, email, amount);
        return ResultMsg.ok().result(thisTime);
    }


    @RequestMapping(value = "/api/secure/deleteCartListProduct", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg deleteCartListProduct(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        System.out.println(params);
        String email = (String) request.getAttribute("email");
        Integer productId = Integer.parseInt((String) params.get("productId"));
        String color = (String) params.get("color");
        String size = (String) params.get("size");
        Boolean thisTime = userProductCartdetailService.remove(new QueryWrapper<UserProductCartdetail>().eq("productid_cart", productId)
                .eq("productcolor", color).eq("productsize", size).eq("useremail", email));
        return ResultMsg.ok().result(thisTime);
    }


    @RequestMapping(value = "/api/secure/updateCartListProduct", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg updateCartListProduct(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        System.out.println(params);
        String email = (String) request.getAttribute("email");
        Integer productId = Integer.parseInt((String) params.get("productId"));
        String color = (String) params.get("color");
        String size = (String) params.get("size");
        Integer amount = Integer.parseInt((String) params.get("amount"));
        UserProductCartdetail userProductCartdetail = new UserProductCartdetail();
        userProductCartdetail.setProductamount(amount);
        Boolean thisTime = userProductCartdetailService.update(userProductCartdetail, new QueryWrapper<UserProductCartdetail>().eq("productid_cart", productId)
                .eq("productcolor", color).eq("productsize", size).eq("useremail", email));
        return ResultMsg.ok().result(thisTime);
    }


    @RequestMapping(value = "/api/secure/uploadWishListForLogin", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg uploadWishListForLogin(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        System.out.println(params);
        String email = (String) request.getAttribute("email");
        Set<String> keyset = params.keySet();
        System.out.println(keyset);
        Boolean flag = true;
        for (String set : keyset) {
            Integer param = (Integer) JSON.parse((String) params.get(set));
            UserProductWishlist userProductWishlist = userProductWishlistService.getOne(new QueryWrapper<UserProductWishlist>().eq("productId", param)
                    .eq("useremail", email));
            if (userProductWishlist != null) continue;
            UserProductWishlist newUserProductWishlist = new UserProductWishlist();
            newUserProductWishlist.setProductid(param);
            newUserProductWishlist.setUseremail(email);
            Boolean thisTime = userProductWishlistService.save(newUserProductWishlist);
            if (thisTime == false) flag = false;

        }
        return ResultMsg.ok().result(flag);


    }


    @RequestMapping(value = "/api/secure/updateWishListAfterLogin", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg updateWishListAfterLogin(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        System.out.println(params);
        String email = (String) request.getAttribute("email");
        Integer productId = Integer.parseInt((String) params.get("productId"));
        Boolean delete = Boolean.parseBoolean((String) params.get("delete"));
        System.out.println(delete);
        System.out.println(productId);
        if (delete == true) {
            userProductWishlistService.remove(new QueryWrapper<UserProductWishlist>().eq("productid", productId));
        } else {
            UserProductWishlist userProductWishlist = new UserProductWishlist();
            userProductWishlist.setUseremail(email);
            userProductWishlist.setProductid(productId);
            userProductWishlistService.save(userProductWishlist);
        }
        return ResultMsg.ok().result(true);
    }


    public Boolean updateOrSaveUserProductDetail(Integer productId, String color, String size, String email, Integer amount) {
        Boolean flag = true;
        UserProductCartdetail userProductCartdetail = userProductCartdetailService.getOne(new QueryWrapper<UserProductCartdetail>().eq("productid_cart", productId)
                .eq("productcolor", color).eq("productsize", size).eq("useremail", email));

        UserProductCartdetail newUserProductCartdetail = new UserProductCartdetail();

        newUserProductCartdetail.setUseremail(email);
        newUserProductCartdetail.setProductidCart(productId);
        newUserProductCartdetail.setProductcolor(color);
        newUserProductCartdetail.setProductsize(size);
        if (userProductCartdetail == null) {
            newUserProductCartdetail.setProductamount(amount);
            Boolean thistime = userProductCartdetailService.save(newUserProductCartdetail);
            if (thistime == false) flag = false;
        } else {
            newUserProductCartdetail.setProductamount(amount + userProductCartdetail.getProductamount());
            Boolean thistime = userProductCartdetailService.update(newUserProductCartdetail, new QueryWrapper<UserProductCartdetail>().eq("productid_cart", productId)
                    .eq("productcolor", color).eq("productsize", size).eq("useremail", email));
            if (thistime == false) flag = false;
        }
        return flag;
    }

}
