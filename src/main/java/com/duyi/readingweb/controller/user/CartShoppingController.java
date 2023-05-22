package com.duyi.readingweb.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.bean.product.CartListProduct;
import com.duyi.readingweb.bean.product.LuckyBag;
import com.duyi.readingweb.entity.couponlist.Couponlist;
import com.duyi.readingweb.entity.eventManagement.SpecialEventDetail;
import com.duyi.readingweb.entity.invoice.PaymentinfoCustomerside;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.entity.product.UserProductCartdetail;
import com.duyi.readingweb.entity.user.User;
import com.duyi.readingweb.service.couponlist.CouponlistService;
import com.duyi.readingweb.service.eventManagement.SpecialEventDetailService;
import com.duyi.readingweb.service.invoice.PaymentinfoCustomersideService;
import com.duyi.readingweb.service.product.ProductService;
import com.duyi.readingweb.service.product.UserProductCartdetailService;
import com.duyi.readingweb.service.user.UserService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RestController
public class CartShoppingController {

    @Autowired
    private CouponlistService couponlistService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserProductCartdetailService userProductCartdetailService;

    @Autowired
    private PaymentinfoCustomersideService paymentinfoCustomersideService;

    @RequestMapping("/api/secure/getCustomerInfo")
    private ResultMsg getCustomerInfo(HttpServletRequest request) {
        List<Couponlist> timelyCouponlistList = couponlistService.list(new QueryWrapper<Couponlist>().eq("whocanapply", 0).lt("startdate", LocalDateTime.now()).gt("expireDate", LocalDateTime.now()));
        List<Couponlist> normalCouponlistList = couponlistService.list(new QueryWrapper<Couponlist>().eq("whocanapply", 1).lt("startdate", LocalDateTime.now()).gt("expireDate", LocalDateTime.now()));
        List<User> userList = userService.list(new QueryWrapper<User>().eq("email", request.getAttribute("email")));
        Integer deduction = userList.get(0).getDeduction();
        List<Map<String, Object>> timeCoupon = new ArrayList<>();
        timelyCouponlistList.sort(new Comparator<Couponlist>() {
            @Override
            public int compare(Couponlist o1, Couponlist o2) {
                return o1.getApplyamount() - o2.getApplyamount();
            }
        });
        normalCouponlistList.sort(new Comparator<Couponlist>() {
            @Override
            public int compare(Couponlist o1, Couponlist o2) {
                return o1.getApplyamount() - o2.getApplyamount();
            }
        });
        for (int i = 0; i < timelyCouponlistList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("amount", timelyCouponlistList.get(i).getDiscountamount());
            map.put("target", timelyCouponlistList.get(i).getApplyamount());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
            String startDate = simpleDateFormat.format(timelyCouponlistList.get(i).getStartdate());
            String expireDate = simpleDateFormat.format(timelyCouponlistList.get(i).getExpiredate());
            String date = new StringBuilder().append(startDate).append("-").append(expireDate).toString();
            map.put("date", date);
            map.put("couponNum", timelyCouponlistList.get(i).getCodenumber());
            timeCoupon.add(map);
        }
        List<List<Number>> normalList = new ArrayList<>();
        List<Number> list1 = new ArrayList<>();
        List<Number> list2 = new ArrayList<>();
        for (int i = 0; i < normalCouponlistList.size(); i++) {
            list1.add(normalCouponlistList.get(i).getApplyamount());
            list2.add(normalCouponlistList.get(i).getDiscountamount());
        }
        normalList.add(list1);
        normalList.add(list2);
        //这里把luckyBag传回去，不然太难在其他地方找了
        Product product = productService.getOne(new QueryWrapper<Product>().eq("idproduct", 8888888));
        LuckyBag luckyBag = new LuckyBag(product.getHref(), product.getBigimgsrc(),
                product.getNewprice(), product.getProductdescription(), product.getIdproduct(),
                product.getSecondonehalf(), "ランダムです", "ランダムです", 1);
        Map<String, Object> map = new HashMap<>();
        map.put("limit", deduction);
        map.put("timelySale", timeCoupon);
        map.put("normalDiscount", normalList);
        map.put("luckBag", luckyBag);


        return ResultMsg.ok().result(true).data("data", map);
    }

    @Autowired
    private SpecialEventDetailService specialEventDetailService;

    @RequestMapping("/api/secure/getCartListInfo")
    private ResultMsg getCartListInfo(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
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


    @RequestMapping(value = "/api/secure/updatePaymentAmountFromUserSide", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg updatePaymentAmountFromUserSide(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        System.out.println(params);
        Integer countNum = Integer.parseInt((String) params.get("countNum"));
        Integer discount = Integer.parseInt((String) params.get("discount"));
        Integer getPoint = Integer.parseInt((String) params.get("getPoint"));
        Integer paymentAmount = Integer.parseInt((String) params.get("paymentAmount"));
        Integer secondHalfDiscount = Integer.parseInt((String) params.get("secondHalfDiscount"));
        Integer timelyDiscount = Integer.parseInt((String) params.get("timelyDiscount"));
        Integer total = Integer.parseInt((String) params.get("total"));
        Integer usedPoint = Integer.parseInt((String) params.get("usedPoint"));
        Integer deliveryAmount = Integer.parseInt((String) params.get("deliveryAmount"));
        String email = (String) request.getAttribute("email");
        PaymentinfoCustomerside paymentinfoCustomerside = new PaymentinfoCustomerside();
        paymentinfoCustomerside.setCountnum(countNum);
        paymentinfoCustomerside.setGetpoint(getPoint);
        paymentinfoCustomerside.setDiscount(discount);
        paymentinfoCustomerside.setPaymentamount(paymentAmount);
        paymentinfoCustomerside.setSecondhalfdiscount(secondHalfDiscount);
        paymentinfoCustomerside.setTimelydiscount(timelyDiscount);
        paymentinfoCustomerside.setTotal(total);
        paymentinfoCustomerside.setUsedpoint(usedPoint);
        paymentinfoCustomerside.setDeliveryamount(deliveryAmount);
        paymentinfoCustomerside.setUseremail(email);
        paymentinfoCustomerside.setExpiredate(new Date(new Date().getTime() + 3600 * 1000 * 24 * 3));
        PaymentinfoCustomerside paymentinfoCustomerside1 = paymentinfoCustomersideService.getOne(new QueryWrapper<PaymentinfoCustomerside>().eq("useremail", email));
        Boolean thisTime;
        if (paymentinfoCustomerside1 == null) {
            thisTime = paymentinfoCustomersideService.save(paymentinfoCustomerside);
        } else {
            thisTime = paymentinfoCustomersideService.update(paymentinfoCustomerside, new QueryWrapper<PaymentinfoCustomerside>().eq("useremail", email));
        }


        return ResultMsg.ok().result(thisTime);
    }

//    @RequestMapping("/api/secure/getCartInfoForMobile")
//    private ResultMsg getCartInfoForMobile(HttpServletRequest request) {
//        String email = (String) request.getAttribute("email");
//        //跟pc端不一样，我全部都在这里计算，计算完了直接给数据传递回去，每次增减也是这样
//        List<UserProductCartdetail> userProductCartdetails = userProductCartdetailService
//                .list(new QueryWrapper<UserProductCartdetail>().eq("useremail", email));
//        List<CartListProduct> cartListProductList = new ArrayList<>();
//        for (int i = 0; i < userProductCartdetails.size(); i++) {
//            CartListProduct cartListProduct = new CartListProduct();
//            Product product = productService.getOne(new QueryWrapper<Product>()
//                    .select("href", "bigimgsrc", "newprice", "idproduct", "secondonehalf", "productdescription")
//                    .eq("idproduct", userProductCartdetails.get(i).getProductidCart()));
//            SpecialEventDetail specialEventDetail = specialEventDetailService.getOne(new QueryWrapper<SpecialEventDetail>()
//                    .select("productid").eq("specialcode", "timeseller")
//                    .eq("productid", product.getIdproduct()));
//            cartListProduct.setAmount(userProductCartdetails.get(i).getProductamount());
//            cartListProduct.setColor(userProductCartdetails.get(i).getProductcolor());
//            cartListProduct.setSize(userProductCartdetails.get(i).getProductsize());
//
//            cartListProduct.setHref(product.getHref());
//            cartListProduct.setImgSrc(product.getBigimgsrc());
//            cartListProduct.setPrice(product.getNewprice());
//            cartListProduct.setProductId(product.getIdproduct());
//            cartListProduct.setSecondOneHalf(product.getSecondonehalf());
//            cartListProduct.setProductDescription(product.getProductdescription());
//            if (specialEventDetail != null) {
//                cartListProduct.setTimesale(1);
//            } else {
//                cartListProduct.setTimesale(0);
//            }
//
//            cartListProductList.add(cartListProduct);
//        }
//        return null;
//    }

}
