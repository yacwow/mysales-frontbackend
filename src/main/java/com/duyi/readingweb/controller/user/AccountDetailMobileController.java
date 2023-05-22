package com.duyi.readingweb.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.bean.product.user.UserDetail;
import com.duyi.readingweb.entity.couponlist.Couponlist;
import com.duyi.readingweb.entity.invoice.InvoiceAddress;
import com.duyi.readingweb.entity.invoice.InvoiceProductProductdetail;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.entity.product.UserProductWishlist;
import com.duyi.readingweb.entity.invoice.Invoice;
import com.duyi.readingweb.entity.user.Pointhistory;
import com.duyi.readingweb.entity.user.User;
import com.duyi.readingweb.service.couponlist.CouponlistService;
import com.duyi.readingweb.service.invoice.InvoiceAddressService;
import com.duyi.readingweb.service.invoice.InvoiceProductProductdetailService;
import com.duyi.readingweb.service.product.ProductService;
import com.duyi.readingweb.service.product.UserProductWishlistService;
import com.duyi.readingweb.service.invoice.InvoiceService;
import com.duyi.readingweb.service.user.PointhistoryService;
import com.duyi.readingweb.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class AccountDetailMobileController {
    @Autowired
    private UserService userService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private UserProductWishlistService userProductWishlistService;
    @Autowired
    private ProductService productService;
    @Autowired
    private InvoiceProductProductdetailService invoiceProductProductdetailService;
    @Autowired
    private InvoiceAddressService invoiceAddressService;
    @Autowired
    private CouponlistService couponlistService;
    @Autowired
    private PointhistoryService pointhistoryService;

    @RequestMapping("/api/secure/getMobileDashboardInfo")
    private ResultMsg getMobileDashboardInfo(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        //一个获取积分，名字，
        User user = userService.getOne(new QueryWrapper<User>()
                .select("firstName", "lastName", "deduction", "userid", "level")
                .eq("email", email));
        //查找paid，delivery，cancel的该用户的list数量， unpaid不用，那个有具体查询
        Integer paidInvoice = (int) invoiceService.count(new QueryWrapper<Invoice>().eq("userid", user.getUserid())
                .eq("paymentstatus", "paid"));
        Integer cancelledInvoice = (int) invoiceService.count(new QueryWrapper<Invoice>().eq("userid", user.getUserid())
                .eq("paymentstatus", "cancelled"));
        Integer deliveryInvoice = (int) invoiceService.count(new QueryWrapper<Invoice>().eq("userid", user.getUserid())
                .eq("paymentstatus", "delivery"));
        //根据userid获取所有未付款的invoice信息，invoiceid，total，
        List<Invoice> invoiceList = invoiceService.list(new QueryWrapper<Invoice>()
                .eq("userid", user.getUserid())
                .select("invoiceid", "paymentamount").eq("paymentstatus", "unpaid"));
        List<Map<String, Object>> invoiceListWithPicture = new ArrayList<>();
        //根据invoiceid获取当前的invoice有几件以及productid
        for (int i = 0; i < invoiceList.size(); i++) {
            List<InvoiceProductProductdetail> invoiceProductProductdetailList = invoiceProductProductdetailService
                    .list(new QueryWrapper<InvoiceProductProductdetail>()
                            .eq("invoiceid_productdetail", invoiceList.get(i).getInvoiceid())
                            .select("productid_productdetail", "amount", "imgsrc"));
            //根据productid获取衣服的图片，最多获取三件衣服的图片即可
            Integer amount = 0;
            List<String> pictureList = new ArrayList<>();
            for (int j = 0; j < invoiceProductProductdetailList.size(); j++) {
                amount += invoiceProductProductdetailList.get(j).getAmount();
                if (j < 3) {

                    pictureList.add(invoiceProductProductdetailList.get(j).getImgsrc());
                }

            }

            Map<String, Object> oneInvoice = new HashMap<>();
            oneInvoice.put("invoiceId", invoiceList.get(i).getInvoiceid());
            oneInvoice.put("paymentAmount", invoiceList.get(i).getPaymentamount());
            oneInvoice.put("imgSrcList", pictureList);
            oneInvoice.put("totalNum", amount);
            invoiceListWithPicture.add(oneInvoice);
        }
        //还要查出该用户当前拥有的限时折扣
        List<Couponlist> couponlists = couponlistService.list(new QueryWrapper<Couponlist>()
                .select("discountAmount", "codenumber", "applyAmount", "startDate", "expireDate", "idcouponList")
                .eq("whocanapply", 0)
                .le("startDate", new Date()).ge("expireDate", new Date()));
        //变个格式
        List<Map<String,Object>> couponList=new ArrayList<>();
        for (int i = 0; i < couponlists.size(); i++) {
            Map<String,Object> map= new HashMap<>();
            map.put("discountAmount",couponlists.get(i).getDiscountamount());
            map.put("codeNumber",couponlists.get(i).getCodenumber());
            map.put("applyAmount",couponlists.get(i).getApplyamount());
            map.put("startDate",couponlists.get(i).getStartdate());
            map.put("expireDate",couponlists.get(i).getExpiredate());
            map.put("id",couponlists.get(i).getIdcouponlist());
            couponList.add(map);
        }

        Map<String, Object> resMap = new HashMap<>();
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("deduction", user.getDeduction());
        userInfo.put("firstName", user.getFirstname());
        userInfo.put("lastName", user.getLastname());
        userInfo.put("level", user.getLevel());
        userInfo.put("paidInvoice", paidInvoice);
        userInfo.put("deliveryInvoice", deliveryInvoice);
        userInfo.put("cancelledInvoice", cancelledInvoice);

//        resMap.put("invoiceNumber",num);
        resMap.put("userInfo", userInfo);
        resMap.put("couponList", couponList);
        resMap.put("invoiceListWithPicture", invoiceListWithPicture);
        return ResultMsg.ok().result(true).data(resMap);
    }

//    @RequestMapping("/api/secure/getFavoriteProduct")
//    private ResultMsg getFavoriteProduct(HttpServletRequest request) {
//        String email = (String) request.getAttribute("email");
//        //一个获取积分，名字，一个获取一共有几个invoice
//        List<UserProductWishlist> userProductWishlistList = userProductWishlistService.list(new QueryWrapper<UserProductWishlist>().eq("useremail", email));
//        List<Map<String, Object>> resMap = new ArrayList<>();
//        for (int i = 0; i < userProductWishlistList.size(); i++) {
//            Product product = productService.getOne(new QueryWrapper<Product>().eq("idproduct", userProductWishlistList.get(i).getProductid())
//                    .select("bigimgsrc", "href", "productDescription", "newprice"));
//            if (product == null) continue;
//            Map<String, Object> map = new HashMap<>();
//            map.put("pid", userProductWishlistList.get(i).getProductid());
//            map.put("imgSrc", product.getBigimgsrc());
//            map.put("href", product.getHref());
//            map.put("description", product.getProductdescription());
//            map.put("price", product.getNewprice());
//            resMap.add(map);
//        }
//
//        return ResultMsg.ok().result(true).data("serverSideData", resMap);
//    }
//
//    @RequestMapping(value = "/api/secure/deleteWishListByProductId", method = {RequestMethod.GET, RequestMethod.POST})
//    private ResultMsg deleteWishListByProductId(@RequestParam Map<String, Object> params, HttpServletRequest request) {
//        String email = (String) request.getAttribute("email");
//        Integer productId = Integer.parseInt((String) params.get("productId"));
//        System.out.println(productId);
//        //一个获取积分，名字，一个获取一共有几个invoice
//        Boolean flag = userProductWishlistService.remove(new QueryWrapper<UserProductWishlist>().
//                eq("useremail", email).eq("productid", productId));
//        System.out.println(flag);
//        return ResultMsg.ok().result(true).result(flag);
//    }
//
//
//    @RequestMapping("/api/secure/getOrderInvoiceHistory")
//    private ResultMsg getOrderInvoiceHistory(HttpServletRequest request) {
//        String email = (String) request.getAttribute("email");
//        //先找到userid，然后找到invoice，每个invoice找到下面的product组成集合，还要找到invoice-product-cartlist的具体大小尺寸数量
//        User user = userService.getOne(new QueryWrapper<User>().eq("email", email));
//        List<Invoice> invoiceList = invoiceService.list(new QueryWrapper<Invoice>().eq("userid", user.getUserid())
//                .select("invoiceid", "paymentamount", "orderdate", "paymentstatus"));
//        List<Map<String, Object>> InvoiceDataList = new ArrayList<>();
//        for (int i = 0; i < invoiceList.size(); i++) {
//            List<InvoiceProductProductdetail> invoiceProductProductdetailList = invoiceProductProductdetailService.list(new QueryWrapper<InvoiceProductProductdetail>()
//                    .eq("invoiceid_productdetail", invoiceList.get(i).getInvoiceid()));
//            Map<String, Object> InvoiceData = new HashMap<>();
//            List<Map<String, Object>> productDataList = new ArrayList<>();
//            for (int j = 0; j < invoiceProductProductdetailList.size(); j++) {
//                Product product = productService.getOne(new QueryWrapper<Product>().eq("idproduct", invoiceProductProductdetailList.get(j).getProductidProductdetail())
//                        .select("productdescription", "newprice", "bigimgsrc", "href"));
//                Map<String, Object> productData = new HashMap<>();
//                productData.put("href", product.getHref());
//                productData.put("description", product.getProductdescription());
//                productData.put("price", product.getNewprice());
//                productData.put("imgSrc", product.getBigimgsrc());
//                productData.put("size", invoiceProductProductdetailList.get(j).getProductsize());
//                productData.put("color", invoiceProductProductdetailList.get(j).getProductcolor());
//                productData.put("amount", invoiceProductProductdetailList.get(j).getAmount());
//                productDataList.add(productData);
//            }
//            InvoiceData.put("invoiceNum", invoiceList.get(i).getInvoiceid());
//            InvoiceData.put("paidStatus", invoiceList.get(i).getPaymentstatus());
//            InvoiceData.put("invoiceDate", invoiceList.get(i).getOrderdate());
//            InvoiceData.put("totalPayment", invoiceList.get(i).getPaymentamount());
//            InvoiceData.put("productData", productDataList);
//            InvoiceDataList.add(InvoiceData);
//        }
//
//
//        return ResultMsg.ok().result(true).data("serverSideData", InvoiceDataList);
//    }
//
//    @RequestMapping("/api/secure/getOneDetailInvoice/{invoiceNum}")
//    private ResultMsg getOneDetailInvoice(HttpServletRequest request, @PathVariable("invoiceNum") Integer invoiceNum) {
////        String email = (String) request.getAttribute("email");
//        //先找到userid，然后找到invoice，每个invoice找到下面的product组成集合，还要找到invoice-product-cartlist的具体大小尺寸数量
//        Invoice invoice = invoiceService.getOne(new QueryWrapper<Invoice>().eq("invoiceid", invoiceNum));
//        if (invoice == null) {
//            return ResultMsg.ok().result(false);
//        }
//        List<InvoiceProductProductdetail> invoiceProductProductdetailList = invoiceProductProductdetailService.list(new QueryWrapper<InvoiceProductProductdetail>()
//                .eq("invoiceid_productdetail", invoiceNum));
//        Map<String, Object> willpassdata = new HashMap<>();
//        List<Map<String, Object>> productDataList = new ArrayList<>();
//        for (int j = 0; j < invoiceProductProductdetailList.size(); j++) {
//            Product product = productService.getOne(new QueryWrapper<Product>().eq("idproduct", invoiceProductProductdetailList.get(j).getProductidProductdetail())
//                    .select("productdescription", "newprice", "bigimgsrc", "href"));
//            Map<String, Object> productData = new HashMap<>();
//            productData.put("href", product.getHref());
//            productData.put("description", product.getProductdescription());
//            productData.put("price", product.getNewprice());
//            productData.put("imgSrc", product.getBigimgsrc());
//            productData.put("size", invoiceProductProductdetailList.get(j).getProductsize());
//            productData.put("color", invoiceProductProductdetailList.get(j).getProductcolor());
//            productData.put("amount", invoiceProductProductdetailList.get(j).getAmount());
//            productDataList.add(productData);
//        }
//        willpassdata.put("invoiceNum", invoice.getInvoiceid());
//        willpassdata.put("paidStatus", invoice.getPaymentstatus());
//        willpassdata.put("productData", productDataList);
//
//        Map<String, Object> invoiceMessage = new HashMap<>();
//        invoiceMessage.put("invoiceNum", invoice.getInvoiceid());
//        invoiceMessage.put("invoiceTime", invoice.getOrderdate());
//        invoiceMessage.put("paymentMethod", invoice.getPaymentmethod());
//        invoiceMessage.put("deliveryFeeDesc", invoice.getDeliverymethod());
//        invoiceMessage.put("originPrice", invoice.getOriginprice());
//        invoiceMessage.put("timelyDiscount", invoice.getTimelydiscount());
//        invoiceMessage.put("secondHalf", invoice.getSecondhalfdiscount());
//        invoiceMessage.put("discount", invoice.getNormaldiscount());
//        invoiceMessage.put("pointDiscount", invoice.getUsedpoint());
//        invoiceMessage.put("priceAfterDiscount", invoice.getPriceafterdiscount());
//        invoiceMessage.put("deliveryFee", invoice.getDeliveryfee());
//        invoiceMessage.put("totalAmount", invoice.getPaymentamount());
//
//
//        Map<String, Object> userInfo = new HashMap<>();
//        userInfo.put("fullName", invoice.getFirstname() + invoice.getLastname());
//        userInfo.put("email", invoice.getUpdateemail());
//        userInfo.put("phone", invoice.getMobilephone());
//        userInfo.put("postcode", invoice.getPostcode());
//        userInfo.put("country", invoice.getCountry());
//        userInfo.put("province", invoice.getProvince());
//        userInfo.put("city", invoice.getCity());
//        userInfo.put("detailAddress", invoice.getDetailaddress());
//        Map<String, Object> res = new HashMap<>();
//        res.put("userInfo", userInfo);
//        res.put("invoiceMessage", invoiceMessage);
//        res.put("willpassdata", willpassdata);
//        return ResultMsg.ok().result(true).data(res);
//
//    }
//
//
//    @RequestMapping("/api/secure/repayment/{invoiceNumber}")
//    private ResultMsg repaymentData(HttpServletRequest request, @PathVariable("invoiceNumber") Integer number) {
//        String email = (String) request.getAttribute("email");
//        //先找到userid，然后找到invoice，每个invoice找到下面的product组成集合，还要找到invoice-product-cartlist的具体大小尺寸数量
//        User user = userService.getOne(new QueryWrapper<User>().eq("email", email).select("userid"));
//        Invoice invoice = invoiceService.getOne(new QueryWrapper<Invoice>().eq("userid", user.getUserid())
//                .eq("invoiceid", number));
//
//        if (invoice != null) {
//            return ResultMsg.ok().result(true).data("serverSideData", invoice);
//        } else {
//            return ResultMsg.ok().result(true).code(404);
//        }
//    }
//
//    @RequestMapping("/api/secure/readyToPay/{invoiceNum}/{paymentMethod}")
//    private ResultMsg fianalCheckBeforePay(HttpServletRequest request, @PathVariable("invoiceNum") Integer number, @PathVariable("paymentMethod") String paymentMethod) {
//        String email = (String) request.getAttribute("email");
//        //先找到userid，然后找到invoice，每个invoice找到下面的product组成集合，还要找到invoice-product-cartlist的具体大小尺寸数量
//        User user = userService.getOne(new QueryWrapper<User>().eq("email", email).select("userid"));
//        Invoice invoice = invoiceService.getOne(new QueryWrapper<Invoice>().eq("userid", user.getUserid())
//                .eq("invoiceid", number));
//
//        if (invoice != null) {
//            return ResultMsg.ok().result(true).data("serverSideData", invoice);
//        } else {
//            return ResultMsg.ok().result(true).code(404);
//        }
//    }
//
//    @RequestMapping(value = "/api/secure/updateUserBasicInfo", method = {RequestMethod.POST, RequestMethod.GET})
//    private ResultMsg updateUserBasicInfo(@RequestParam Map<String, Object> params, HttpServletRequest request) {
//        String tokenEmail = (String) request.getAttribute("email");
//        String email = (String) params.get("email");
//        String firstName = (String) params.get("firstName");
//        String lastName = (String) params.get("lastName");
//        String newPassword = (String) params.get("newPassword");
//        String oldPassword = (String) params.get("oldPassword");
//        //如果email和token的email一样，那就是修改，先验证加密后的密码然后修改
//        if (tokenEmail.equals(email)) {
//            User user = userService.getOne(new QueryWrapper<User>().eq("email", tokenEmail));
//            String oldEncrypassword = DigestUtils.md5DigestAsHex(oldPassword.getBytes());
//            if (oldEncrypassword.equals(user.getUpassword())) {
//                //修改
//                String newEncryPwd = DigestUtils.md5DigestAsHex(newPassword.getBytes());
//                User user1 = new User();
//                user1.setUpassword(newEncryPwd);
//                user1.setLastname(lastName);
//                user1.setFirstname(firstName);
//                Boolean flag = userService.update(user1, new QueryWrapper<User>().eq("email", email));
//                return ResultMsg.ok().result(flag);
//            } else {
//                //告知密码错误
//                return ResultMsg.ok().result(true).code(404);
//            }
//        } else {//如果不一样，那就在录入一份
//            //先看有没有一样的邮箱
//            User user2 = userService.getOne(new QueryWrapper<User>().eq("email", email));
//            if (user2 != null) {
//                //有一样的，告知不行
//                return ResultMsg.ok().result(true).code(400);
//            } else {
//                String newEncryPwd = DigestUtils.md5DigestAsHex(newPassword.getBytes());
//                User user1 = new User();
//                user1.setUpassword(newEncryPwd);
//                user1.setLastname(lastName);
//                user1.setFirstname(firstName);
//                user1.setEmail(email);
//                Boolean flag = userService.save(user1);
//                return ResultMsg.ok().result(true);
//            }
//        }
//    }
//
//    @RequestMapping(value = "/api/secure/invoiceAddressList")
//    private ResultMsg invoiceAddressList(HttpServletRequest request) {
//        String tokenEmail = (String) request.getAttribute("email");
//        List<InvoiceAddress> userAddressList = invoiceAddressService.list(new QueryWrapper<InvoiceAddress>().eq("email", tokenEmail));
//        List<UserDetail> userDetailList = new ArrayList<>();
//        Boolean flag = false;
//        if (userAddressList.size() >= 1) flag = true;
//        for (int i = 0; i < userAddressList.size(); i++) {
//            userDetailList.add(new UserDetail(
//                    userAddressList.get(i).getDetailaddress(),
//                    userAddressList.get(i).getCountry(),
//                    userAddressList.get(i).getProvince(),
//                    userAddressList.get(i).getCity(),
//                    userAddressList.get(i).getArea(),
//                    userAddressList.get(i).getPostcode(),
//                    userAddressList.get(i).getMobilephone(),
//                    userAddressList.get(i).getPhone(),
//                    userAddressList.get(i).getFirstname(),
//                    userAddressList.get(i).getLastname(),
//                    userAddressList.get(i).getUpdateemail(),
//                    userAddressList.get(i).getIduserAddress()
//            ));
//        }
//        return ResultMsg.ok().result(flag).data("invoiceAddressList", userDetailList);
//    }
//
//    @RequestMapping(value = "/api/secure/getUserPointHistory")
//    private ResultMsg getUserPointHistory(HttpServletRequest request) {
//        List<Couponlist> couponlists = couponlistService.list(new QueryWrapper<Couponlist>().eq("whocanapply", 0));
//        String tokenEmail = (String) request.getAttribute("email");
//        User user = userService.getOne(new QueryWrapper<User>().eq("email", tokenEmail).select("userid", "deduction"));
//        //开始计算所有的unpaid的invoice的总payment值，用于显示在有效等待point上
//        List<Invoice> invoiceList = invoiceService.list(new QueryWrapper<Invoice>().eq("paymentstatus", "unpaid").eq("userid", user.getUserid()).select("paymentamount"));
//        Integer amount = 0;
//        for (int i = 0; i < invoiceList.size(); i++) {
//            amount += invoiceList.get(i).getPaymentamount();
//        }
//        //开始获取所有的pointhistory
//        List<Pointhistory> pointerlist = pointhistoryService.list(new QueryWrapper<Pointhistory>().eq("email", tokenEmail));
//
//        Map<String, Object> res = new HashMap<>();
//        res.put("couponlists", couponlists);
//        res.put("amount", amount);
//        res.put("pointerlist", pointerlist);
//        res.put("deduction", user.getDeduction());
//        return ResultMsg.ok().result(true).data(res);
//    }
//
//
//    @RequestMapping(value = "/api/secure/getInitialValue/{invoiceAddressId}")
//    private ResultMsg getInitialValue(HttpServletRequest request, @PathVariable String invoiceAddressId) {
//        InvoiceAddress invoiceAddress = invoiceAddressService.getOne(new QueryWrapper<InvoiceAddress>()
//                .eq("iduser_address", invoiceAddressId).select("iduser_address", "detailaddress", "country", "province", "city",
//                        "area", "postcode", "email", "updateemail", "firstname", "lastname", "mobilephone", "phone"));
//        if (invoiceAddress == null) {
//            return ResultMsg.ok().result(false);
//        }
//        UserDetail userDetail = new UserDetail(
//                invoiceAddress.getDetailaddress(),
//                invoiceAddress.getCountry(),
//                invoiceAddress.getProvince(),
//                invoiceAddress.getCity(),
//                invoiceAddress.getArea(),
//                invoiceAddress.getPostcode(),
//                invoiceAddress.getMobilephone(),
//                invoiceAddress.getPhone(),
//                invoiceAddress.getFirstname(),
//                invoiceAddress.getLastname(),
//                invoiceAddress.getUpdateemail(),
//                invoiceAddress.getIduserAddress()
//        );
//        Map<String, Object> initialValue = new HashMap<>();
//        initialValue.put("userDetail", userDetail);
//
//        return ResultMsg.ok().result(true).data(initialValue);
//    }


}
