package com.duyi.readingweb.controller.user;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.bean.product.user.UserDetail;
import com.duyi.readingweb.entity.couponlist.Couponlist;
import com.duyi.readingweb.entity.invoice.InvoiceProductProductdetail;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.entity.product.UserProductCartdetail;
import com.duyi.readingweb.entity.invoice.Invoice;
import com.duyi.readingweb.entity.user.User;
import com.duyi.readingweb.entity.invoice.InvoiceAddress;
import com.duyi.readingweb.service.couponlist.CouponlistService;
import com.duyi.readingweb.service.invoice.InvoiceProductProductdetailService;
import com.duyi.readingweb.service.invoice.PaymentinfoCustomersideService;
import com.duyi.readingweb.service.product.ProductService;
import com.duyi.readingweb.service.product.UserProductCartdetailService;
import com.duyi.readingweb.service.invoice.InvoiceService;
import com.duyi.readingweb.service.invoice.InvoiceAddressService;
import com.duyi.readingweb.service.user.UserService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
public class CheckOutController {
    @Autowired
    private UserService userService;
    @Autowired
    private InvoiceAddressService invoiceAddressService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CouponlistService couponlistService;
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private UserProductCartdetailService userProductCartdetailService;
    @Autowired
    private InvoiceProductProductdetailService invoiceProductProductdetailService;
    @Autowired
    private PaymentinfoCustomersideService paymentinfoCustomersideService;

    //user的快递地址，和本人地址还不太一样
    @RequestMapping("/api/secure/checkOut")
    private ResultMsg getCheckOutInfo(HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        User user = userService.getOne(new QueryWrapper<User>().select("firstname", "lastname").eq("email", email));
        String firstName = user.getFirstname();
        String lastName = user.getLastname();
        List<InvoiceAddress> userAddressList = invoiceAddressService.list(new QueryWrapper<InvoiceAddress>().eq("email", email));
        List<UserDetail> userDetailList = new ArrayList<>();
        Boolean flag = false;
        if (userAddressList.size() >= 1) flag = true;
        for (int i = 0; i < userAddressList.size(); i++) {
            userDetailList.add(new UserDetail(
                    userAddressList.get(i).getDetailaddress(),
                    userAddressList.get(i).getCountry(),
                    userAddressList.get(i).getProvince(),
                    userAddressList.get(i).getCity(),
                    userAddressList.get(i).getArea(),
                    userAddressList.get(i).getPostcode(),
                    userAddressList.get(i).getMobilephone(),
                    userAddressList.get(i).getPhone(),
                    userAddressList.get(i).getFirstname(),
                    userAddressList.get(i).getLastname(),
                    userAddressList.get(i).getUpdateemail(),
                    userAddressList.get(i).getIduserAddress()

            ));
        }
        Map<String, Object> resMap = new HashMap<>();
        //没有完整数据的时候，只给前三个
        Map<String, Object> tableValue = new HashMap<>();
        tableValue.put("email", email);
        tableValue.put("firstName", firstName);
        tableValue.put("lastName", lastName);
        resMap.put("tableValue", tableValue);
        resMap.put("fullAddress", flag);
        resMap.put("address", userDetailList);

        return ResultMsg.ok().result(true).data(resMap);
    }

    //创建user的invoice，需要各种验证，原则上怎么都通过，然后有异常的写入一个异常sql，到时候查看
    @RequestMapping(value = "/api/secure/createInvoice", method = {RequestMethod.GET, RequestMethod.POST})
    private ResultMsg createInvoice(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        Map<String, Object> userinfo = (Map<String, Object>) JSON.parse((String) params.get("userInfo"));
        System.out.println(userinfo);
        Boolean needUpdateAddress = (Boolean) JSON.parse((String) params.get("needUpdateAddress"));
        System.out.println(needUpdateAddress);
        String updateEmail = (String) userinfo.get("email");
        String updatedetailaddress = (String) userinfo.get("detailAddress");
        String firstName = (String) userinfo.get("firstName");
        String lastName = (String) userinfo.get("lastName");
        String mobilePhone = (String) userinfo.get("mobilePhone");
        String updatecountry = (String) userinfo.get("country");
        String updateprovince = (String) userinfo.get("province");
        String updatecity = (String) userinfo.get("city");
        String updatearea = (String) userinfo.get("area");
        System.out.println(userinfo.get("postcode"));
        Integer postcode = (Integer) userinfo.get("postcode");
        //把用户的信息存入到用户使用地址表里面（跟用户实际地址可以完全不同）,根据前端传入的是否需要更新
        if (needUpdateAddress) {
            InvoiceAddress invoiceAddress = new InvoiceAddress();
            invoiceAddress.setEmail(email);
            invoiceAddress.setFirstname(firstName);
            invoiceAddress.setLastname(lastName);
            invoiceAddress.setUpdateemail(updateEmail);
            invoiceAddress.setMobilephone(mobilePhone);
            invoiceAddress.setCity(updatecity);
            invoiceAddress.setArea(updatearea);
            invoiceAddress.setPostcode(postcode);
            invoiceAddress.setDetailaddress(updatedetailaddress);
            invoiceAddress.setCountry(updatecountry);
            invoiceAddress.setProvince(updateprovince);
            System.out.println(invoiceAddress);
            invoiceAddressService.save(invoiceAddress);
        }
        Map<String, Object> updatedInfo = (Map<String, Object>) JSON.parse((String) params.get("updatedInfo"));
        Integer deliveryMethodId = Integer.parseInt((String) params.get("deliveryMethodId"));
        String paymentMethod = (String) params.get("paymentMethod");
        System.out.println(userinfo);
        System.out.println(updatedInfo);
        System.out.println(deliveryMethodId);
        System.out.println(paymentMethod);
        //1 先验证传过来的数据对不对，如果数字不对就可能是被修改过了，要自己弄个提示数据库
        Map<String, Object> paymentDetail = (Map<String, Object>) updatedInfo.get("paymentDetail");
        Integer uncheckedCountNum = (Integer) paymentDetail.get("countNum");
        Integer uncheckedGetPoint = (Integer) paymentDetail.get("getPoint");
        Integer uncheckedTotal = (Integer) paymentDetail.get("total");
        Integer uncheckedSecondHalfDiscount = (Integer) paymentDetail.get("secondHalfDiscount");
        Integer uncheckedDiscount = (Integer) paymentDetail.get("discount");
        Integer uncheckedTimelyDiscount = (Integer) paymentDetail.get("timelyDiscount");
        Integer uncheckedPaymentAmount = (Integer) paymentDetail.get("paymentAmount");

        Object usedPointObj = paymentDetail.get("usedPoint");
        int uncheckedUsedPoint;
        if (usedPointObj instanceof Integer) {
            uncheckedUsedPoint = (int) usedPointObj;
        } else if (usedPointObj instanceof String) {
            try {
                uncheckedUsedPoint = Integer.parseInt((String) usedPointObj);
            } catch (NumberFormatException e) {
                // 处理解析错误的情况
                uncheckedUsedPoint = 0; // 默认值或其他适当处理方式
            }
        } else {
            uncheckedUsedPoint = 0; // 默认值或其他适当处理方式
        }
        Integer uncheckedDeliveryAmount = (Integer) paymentDetail.get("deliveryAmount");

        //1.1遍历productInfo,获取各种数据
        Integer countNum = 0;
        Integer total = 0;
        Integer secondHalfDiscount = 0;
        Integer luckBag = 0;
        List<Map<String, Object>> productInfo = (List<Map<String, Object>>) updatedInfo.get("productInfo");
        for (int i = 0; i < productInfo.size(); i++) {
            Integer pid = (Integer) productInfo.get(i).get("pid");
            if (pid == 8888888) {
                luckBag++;
                countNum++;
                continue;
            }
            Product product = productService.getOne(new QueryWrapper<Product>().eq("idproduct", pid)
                    .select("secondonehalf", "tenpercentoff", "newprice"));
            if ((Integer) productInfo.get(i).get("amount") >= 2 && product.getSecondonehalf() > 0) {
                secondHalfDiscount += (int) Math.floor(product.getNewprice() / 2);
            }
            total += product.getNewprice() * (Integer) productInfo.get(i).get("amount");
            countNum += (Integer) productInfo.get(i).get("amount");
        }


        //1.2然后判断paymentdetail里面有没有discount和timelydiscount，如果有就要去搜couponlist
        Integer discount = 0;
        if (uncheckedDiscount > 0) {
            List<Couponlist> couponlists = couponlistService.list(new QueryWrapper<Couponlist>().eq("whocanapply", 1));
            couponlists.sort(new Comparator<Couponlist>() {
                @Override
                public int compare(Couponlist o1, Couponlist o2) {
                    return o1.getApplyamount() - o2.getApplyamount();
                }
            });
            Integer i = 0;
            for (; i < couponlists.size(); i++) {
                if (total < couponlists.get(i).getApplyamount()) {
                    break;
                }
            }
            if (i == 0) {
                discount = 0;
            } else {
                discount = couponlists.get(i - 1).getDiscountamount();
            }
        }

        Integer timelyDiscount = 0;
        if (uncheckedTimelyDiscount > 0) {
            List<Couponlist> couponlists = couponlistService.list(new QueryWrapper<Couponlist>().eq("whocanapply", 0)
                    .gt("expiredate", LocalDateTime.now()).lt("startdate", LocalDateTime.now()));
            couponlists.sort(new Comparator<Couponlist>() {
                @Override
                public int compare(Couponlist o1, Couponlist o2) {
                    return o1.getApplyamount() - o2.getApplyamount();
                }
            });
            Integer i = 0;
            for (; i < couponlists.size(); i++) {
                if (total < couponlists.get(i).getApplyamount()) {
                    break;
                }
            }
            if (i == 0) {
                timelyDiscount = 0;
            } else {
                timelyDiscount = couponlists.get(i - 1).getDiscountamount();
            }
        }

        //1.3传入的uncheckedUsedPoint 如果大于0，判断是不是大于我们的允许值
        Integer usedPoint = 0;
        User user = userService.getOne(new QueryWrapper<User>().eq("email", email).select("deduction", "userid"));
        if (uncheckedUsedPoint > 0) {
            usedPoint = Math.min(user.getDeduction() + (int) Math.floor(total / 20), uncheckedUsedPoint);
        }

        //1.4然后判断paymentdetail里面有没有deliveryamount来做一次判断 折扣后价格大于9999
        Integer deliveryAmount = 0;
        String deliveryMethod;
        Integer paymentBeforeDeliveryFee = total - secondHalfDiscount - timelyDiscount - discount - usedPoint;
        if (deliveryMethodId == 1) {
            //只有1 需要判断折扣后价格，其他的就直接赋值了
            deliveryMethod = "normal";
            if (paymentBeforeDeliveryFee > 9999) {
                //啥事都不用做，就是0
            } else {
                deliveryAmount = 953;
            }
        } else if (deliveryMethodId == 2) {
            deliveryMethod = "ems";
            deliveryAmount = 1598;
        } else {
            deliveryMethod = "iexpress";
            deliveryAmount = 1980;
        }
        //1.5按照总折扣后价格总数判断是否该送礼品
        Integer trueLuckyBag = 0;
        if (paymentBeforeDeliveryFee >= 10000 && paymentBeforeDeliveryFee < 20000) {
            trueLuckyBag = 1;
        } else if (paymentBeforeDeliveryFee >= 20000 && paymentBeforeDeliveryFee < 30000) {
            trueLuckyBag = 2;
        } else if (paymentBeforeDeliveryFee >= 30000) {
            trueLuckyBag = 3;
        }
        //1.6 getPoint不用验证，他传入再大都无所谓，我只算我的
        Integer getPoint = (int) Math.floor(total / 20);
        //1.7总数
        Integer priceAfterDiscount = total - secondHalfDiscount - discount - timelyDiscount - usedPoint;
        Integer paymentAmount = priceAfterDiscount + deliveryAmount;
        System.out.println(countNum);
        System.out.println(total);
        System.out.println(secondHalfDiscount);
        System.out.println("discount");
        System.out.println(discount);
        System.out.println(timelyDiscount + "timelyDiscount");
        System.out.println(usedPoint + "usedPoint");
        System.out.println(deliveryAmount + "deliveryAmount");
        System.out.println(trueLuckyBag + "trueLuckyBag");
        System.out.println(getPoint + "getPoint");
        System.out.println(paymentDetail);
        System.out.println(paymentAmount + "paymentAmount");
        //1.8是时候进入判断阶段了，如果判断有问题，但是总数大于30000并差距在10000以内就先放行。但是我们要放入数据库每天都查看
        //现在先不做这个
        //2注入一个invoice并获取invoice的id，

        Invoice invoice = new Invoice();
        Date dateTime = new DateTime().toDate();
        System.out.println(dateTime);
        invoice.setUserid(user.getUserid());
        invoice.setOrderdate(dateTime);
        invoice.setPaymentmethod(paymentMethod);
        invoice.setDeliverymethod(deliveryMethod);
        invoice.setOriginprice(total);
        invoice.setNormaldiscount(discount);
        invoice.setTimelydiscount(timelyDiscount);
        invoice.setSecondhalfdiscount(secondHalfDiscount);
        invoice.setUsedpoint(usedPoint);
        invoice.setPriceafterdiscount(priceAfterDiscount);
        invoice.setDeliveryfee(deliveryAmount);
        invoice.setPaymentamount(paymentAmount);
        invoice.setPaymentstatus("unpaid");
        invoice.setTips((String) userinfo.get("tips"));
        invoice.setMobilephone(mobilePhone);
        invoice.setUpdateemail(updateEmail);
        invoice.setDetailaddress(updatedetailaddress);
        invoice.setFirstname(firstName);
        invoice.setLastname(lastName);
        invoice.setCountry(updatecountry);
        invoice.setProvince(updateprovince);
        invoice.setCity(updatecity);
        invoice.setArea(updatearea);
        invoice.setPostcode(postcode);
        System.out.println(invoice.getInvoiceid());
        System.out.println(productInfo + "productInfo");
        System.out.println("------------------------------");
        System.out.println("------------------------------");
        System.out.println("------------------------------");
        System.out.println("------------------------------");
        System.out.println("------------------------------");
        System.out.println("------------------------------");
        System.out.println("------------------------------");
        System.out.println(invoice);
        Boolean successSave = invoiceService.save(invoice);
        //用这个invoiceid来创建一个invoice包含的货物数量，大小，颜色表invoice_productdetail表（多对多表）
        Integer invoiceId = invoice.getInvoiceid();
        if (successSave) {
            //多对多，贼烦
            for (int i = 0; i < productInfo.size(); i++) {
                InvoiceProductProductdetail invoiceProductProductdetail = new InvoiceProductProductdetail();
                invoiceProductProductdetail.setAmount((Integer) productInfo.get(i).get("amount"));
                invoiceProductProductdetail.setInvoiceidProductdetail(invoiceId);
                invoiceProductProductdetail.setProductidProductdetail((Integer) productInfo.get(i).get("pid"));
                invoiceProductProductdetail.setProductcolor((String) productInfo.get(i).get("color"));
                invoiceProductProductdetail.setProductsize((String) productInfo.get(i).get("size"));
                invoiceProductProductdetail.setImgsrc((String) productInfo.get(i).get("imgSrc"));
                invoiceProductProductdetail.setOrderdate(dateTime);

                invoiceProductProductdetailService.save(invoiceProductProductdetail);
            }
        }
        //把该用户的user-product-cartlist给删掉，没用了
        userProductCartdetailService.remove(new QueryWrapper<UserProductCartdetail>().eq("useremail", email));

        //返回invoiceid，价格    跳转到具体的收款页面，目前代收应该是同一个页面
        Map<String, Object> map = new HashMap<>();
        map.put("invoiceId", invoiceId);
        map.put("amount", paymentAmount);
        return ResultMsg.ok().result(true).data(map);

    }


    //修改invoice的地址，理论上包括了account某张update表，也是该请求
    @RequestMapping(value = "/api/secure/needUpdateOrAdd", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg updateOrAddAddress(@RequestParam Map<String, Object> params, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        System.out.println(params);
        String updateEmail = (String) params.get("email");
        String updatedetailaddress = (String) params.get("detailAddress");
        String firstName = (String) params.get("firstName");
        String lastName = (String) params.get("lastName");
        String mobilePhone = (String) params.get("mobilePhone");
        String phone = (String) params.get("phone");
        String updatecountry = (String) params.get("country");
        String updateprovince = (String) params.get("province");
        String updatecity = (String) params.get("city");
        String updatearea = (String) params.get("area");
        Integer postcode = Integer.valueOf((String) params.get("postcode"));
        Integer invoiceId = Integer.valueOf((String) params.get("invoiceId"));
        InvoiceAddress invoiceAddress = new InvoiceAddress();
        invoiceAddress.setEmail(email);
        invoiceAddress.setFirstname(firstName);
        invoiceAddress.setLastname(lastName);
        invoiceAddress.setUpdateemail(updateEmail);
        invoiceAddress.setMobilephone(mobilePhone);
        invoiceAddress.setCity(updatecity);
        invoiceAddress.setArea(updatearea);
        invoiceAddress.setPhone(phone);
        invoiceAddress.setPostcode(postcode);
        invoiceAddress.setDetailaddress(updatedetailaddress);
        invoiceAddress.setCountry(updatecountry);
        invoiceAddress.setProvince(updateprovince);
        Boolean flag = false;
        System.out.println(invoiceId);
        System.out.println(invoiceId > 0);
        if (invoiceId > 0) {
            //这是修改，根据iduser_address
            flag = invoiceAddressService.update(invoiceAddress,new QueryWrapper<InvoiceAddress>().eq("iduser_address",invoiceId));
        } else {
            flag = invoiceAddressService.save(invoiceAddress);
        }
        if (flag) {
            return ResultMsg.ok().result(true);
        } else {
            return ResultMsg.error().result(false);
        }
    }

    //删除invoice的地址
    @RequestMapping(value = "/api/secure/deleteOneproduct/{invoiceId}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg deleteOneproduct(@PathVariable String invoiceId) {

        Boolean flag = invoiceAddressService.removeById(invoiceId);
        return ResultMsg.ok().result(flag);
    }


}
