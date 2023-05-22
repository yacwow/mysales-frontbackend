package com.duyi.readingweb.controller.product;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.bean.product.CartListProduct;
import com.duyi.readingweb.entity.eventManagement.SpecialEventDetail;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.entity.product.UserProductCartdetail;
import com.duyi.readingweb.entity.product.UserProductWishlist;
import com.duyi.readingweb.service.eventManagement.SpecialEventDetailService;
import com.duyi.readingweb.service.product.ProductService;
import com.duyi.readingweb.service.product.UserProductCartdetailService;
import com.duyi.readingweb.service.product.UserProductWishlistService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class UploadProductController {
    @Autowired
    private UserProductCartdetailService userProductCartdetailService;
    @Autowired
    private UserProductWishlistService userProductWishlistService;
    @Autowired
    private ProductService productService;

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

    @Autowired
    private SpecialEventDetailService specialEventDetailService;

    @RequestMapping(value = "/api/secure/modifyCartListAfterLogin", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg modifyCartListAfterLogin(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        System.out.println(params);
        String email = (String) request.getAttribute("email");
        Integer productId = Integer.parseInt((String) params.get("productId"));
        String color = (String) params.get("color");
        String preColor = (String) params.get("preColor");
        String size = (String) params.get("size");
        String preSize = (String) params.get("preSize");
        Integer amount = Integer.parseInt((String) params.get("amount"));
        Integer preAmount = Integer.parseInt((String) params.get("preAmount"));
        //这个纯纯的是修改了，把原来的color和size的那一行改成新的size color和amount，然后返回整个客户的productlist
        userProductCartdetailService.update(new UpdateWrapper<UserProductCartdetail>()
                .set("productcolor", color).set("productsize", size).set("productAmount", amount)
                .eq("productid_cart", productId)
                .eq("productcolor", preColor).eq("productsize", preSize).eq("useremail", email));
        List<UserProductCartdetail> userProductCartdetails = userProductCartdetailService
                .list(new QueryWrapper<UserProductCartdetail>().eq("useremail", email));
        List<CartListProduct> cartListProductList = new ArrayList<>();
        for (int i = 0; i < userProductCartdetails.size(); i++) {
            CartListProduct cartListProduct = new CartListProduct();
            Product product = productService.getOne(new QueryWrapper<Product>()
                    .select("href", "bigimgsrc", "newprice", "idproduct", "secondonehalf", "productdescription")
                    .eq("idproduct", userProductCartdetails.get(i).getProductidCart()));
            SpecialEventDetail specialEventDetail = specialEventDetailService.getOne(new QueryWrapper<SpecialEventDetail>()
                    .select("productid").eq("specialcode", "timeseller")
                    .eq("productid", product.getIdproduct()));
            cartListProduct.setAmount(userProductCartdetails.get(i).getProductamount());
            cartListProduct.setColor(userProductCartdetails.get(i).getProductcolor());
            cartListProduct.setSize(userProductCartdetails.get(i).getProductsize());

            cartListProduct.setHref(product.getHref());
            cartListProduct.setImgSrc(product.getBigimgsrc());
            cartListProduct.setPrice(product.getNewprice());
            cartListProduct.setProductId(product.getIdproduct());
            cartListProduct.setSecondOneHalf(product.getSecondonehalf());
            cartListProduct.setProductDescription(product.getProductdescription());
            if (specialEventDetail != null) {
                cartListProduct.setTimesale(1);
            } else {
                cartListProduct.setTimesale(0);
            }

            cartListProductList.add(cartListProduct);
        }

        return ResultMsg.ok().result(true).data("data", cartListProductList);
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
            userProductWishlistService.remove(new QueryWrapper<UserProductWishlist>().eq("productid", productId)
                    .eq("useremail", email));
            productService.update(new UpdateWrapper<Product>().eq("idproduct", productId)
                    .setSql("truewishlistnum=truewishlistnum-1").setSql("fraudwishlistnum=fraudwishlistnum-1"));
        } else {
            UserProductWishlist userProductWishlist = new UserProductWishlist();
            userProductWishlist.setUseremail(email);
            userProductWishlist.setProductid(productId);
            userProductWishlistService.save(userProductWishlist);
            productService.update(new UpdateWrapper<Product>().eq("idproduct", productId)
                    .setSql("truewishlistnum=truewishlistnum+1").setSql("fraudwishlistnum=fraudwishlistnum+1"));
        }
        //返回当前用户还有几个喜欢的产品
        Integer count = (int) userProductWishlistService.count(new QueryWrapper<UserProductWishlist>()
                .eq("useremail", email));
        return ResultMsg.ok().result(true).data("count", count);
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
