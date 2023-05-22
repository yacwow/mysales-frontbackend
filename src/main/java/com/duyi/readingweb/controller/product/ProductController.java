package com.duyi.readingweb.controller.product;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.bean.product.BestSellerAndNewProduct;
import com.duyi.readingweb.bean.product.ItemRankingProduct;
import com.duyi.readingweb.bean.product.ItemRankingProductForMobile;
import com.duyi.readingweb.bean.product.MustBuyProduct;
import com.duyi.readingweb.entity.eventManagement.SpecialEventDetail;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.entity.product.ProductImg;
import com.duyi.readingweb.entity.user.Comment;
import com.duyi.readingweb.service.eventManagement.SpecialEventDetailService;
import com.duyi.readingweb.service.product.ProductImgService;
import com.duyi.readingweb.service.product.ProductService;
import com.duyi.readingweb.service.user.CommentService;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImgService productImgService;

    @RequestMapping(value = "/api/itemRanking", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg getRankingItems(@RequestParam Map<String, Object> params) {
        System.out.println(params);
        List<String> list = new ArrayList<>();
        Set<String> keys = params.keySet();
        for (String key : keys
        ) {
            list.add((String) params.get(key));
        }
        Map<String, List<ItemRankingProduct>> resMap = new HashMap<>();//最后存放结果的集合
        for (int i = 0; i < list.size(); i++) {
            String searchParam = list.get(i);
            //返回的是swimwearInner-2  2表示secondlevelcategory，1表示firstlevelcategory
            String[] strings = searchParam.split("-");
            List<String> list1 = Arrays.asList(strings);//请求的数据的拆分
            if (Integer.parseInt(list1.get(1)) == 2) {
                resMap.put(list1.get(0), getRankingItemsUtil(list1, "secondlevelcategory"));
            } else {
                resMap.put(list1.get(0), getRankingItemsUtil(list1, "firstlevelcategory"));
            }
        }
        return ResultMsg.ok().data("itemRanking", resMap);
    }

    @RequestMapping(value = "/api/itemRankingForMobile", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg getRankingItemsM(@RequestParam Map<String, Object> params) {
        System.out.println(params);
        List<String> list = new ArrayList<>();
        Set<String> keys = params.keySet();
        for (String key : keys
        ) {
            list.add((String) params.get(key));
        }
        Map<String, List<ItemRankingProductForMobile>> resMap = new HashMap<>();//最后存放结果的集合
        for (int i = 0; i < list.size(); i++) {
            String searchParam = list.get(i);
            //返回的是swimwearInner-2  2表示secondlevelcategory，1表示firstlevelcategory
            String[] strings = searchParam.split("-");
            List<String> list1 = Arrays.asList(strings);//请求的数据的拆分
            if (Integer.parseInt(list1.get(1)) == 2) {
                resMap.put(list1.get(0), getRankingItemsUtilMobile(list1, "secondlevelcategory"));
            } else {
                resMap.put(list1.get(0), getRankingItemsUtilMobile(list1, "firstlevelcategory"));
            }
        }
        return ResultMsg.ok().data("itemRanking", resMap);
    }


    @Autowired
    private CommentService commentService;

    @RequestMapping("/api/bestSellerAndNewProduct/{number}")
    public ResultMsg getBestSellerAndNewProduct(@PathVariable("number") Integer number) {
        List<BestSellerAndNewProduct> bestSellerList = getBestSellerAndNewProductUtil("bestSeller", number);
        List<BestSellerAndNewProduct> newProductList = getBestSellerAndNewProductUtil("newProduct", number);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("bestSeller", bestSellerList);
        resMap.put("dailyNew", newProductList);

        return ResultMsg.ok().data(resMap);
    }

    @RequestMapping("/api/bestSellerAndTimeSeller/{number}")
    public ResultMsg bestSellerAndTimeSeller(@PathVariable("number") Integer number) {
        List<BestSellerAndNewProduct> bestSellerList = getBestSellerAndNewProductUtil("bestSeller", number);
        List<BestSellerAndNewProduct> newProductList = getBestSellerAndNewProductUtil("timeseller", number);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("bestSeller", bestSellerList);
        resMap.put("timeSeller", newProductList);

        return ResultMsg.ok().data(resMap);
    }
    @RequestMapping("/api/bestSellerPageTop")
    public ResultMsg bestSellerPageTop() {
        //只要20个
        List<BestSellerAndNewProduct> bestSeller = getBestSellerAndNewProductUtil("bestSellerTop", 20);
        Map<String, Object> map = new HashMap<>();
        map.put("bestSellerPageTop", bestSeller);
        return ResultMsg.ok().data(map);
    }
    @RequestMapping("/api/bestSellerPageWithNumberParams/{number}")
    public ResultMsg getBestSellerPage(@PathVariable("number") Integer number) {
        List<BestSellerAndNewProduct> bestSeller = getBestSellerAndNewProductUtil("bestSeller", number);
        List<MustBuyProduct> generalGoods = getMustBuyProduct("generalGoods", number);
        List<MustBuyProduct> topGeneral = getMustBuyProduct("topGeneral", number);
        List<MustBuyProduct> dressGeneral = getMustBuyProduct("dressGeneral", number);
        Map<String, Object> map = new HashMap<>();
        map.put("bestSeller", bestSeller);
        map.put("generalGoods", generalGoods);
        map.put("topGeneral", topGeneral);
        map.put("dressGeneral", dressGeneral);
        return ResultMsg.ok().data("bestSellerPage", map);
    }

    @Autowired
    private SpecialEventDetailService specialEventDetailService;

    @RequestMapping("/api/getRecommendProduct/{number}")
    public ResultMsg getRecommendProduct(@PathVariable("number") Integer number) {
        List<SpecialEventDetail> specialEventDetailList = specialEventDetailService.list(new QueryWrapper<SpecialEventDetail>().eq("specialcode", "recommend")
                .select("productid", "categoryRankNum").orderByDesc("categoryRankNum"));
        List<Integer> productIDList = specialEventDetailList.stream()
                .map(SpecialEventDetail::getProductid).limit(number).collect(Collectors.toList());
        List<Product> products = productService.list(new QueryWrapper<Product>().select("idProduct", "productDescription", "tenpercentoff",
                "secondOneHalf", "href", "newPrice", "bigimgsrc", "fraudcommentnum").in("idproduct", productIDList));

        List<BestSellerAndNewProduct> bestSellerAndNewProducts = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (i >= number) break;
            BestSellerAndNewProduct bestSellerAndNewProduct = new BestSellerAndNewProduct(
                    products.get(i).getProductdescription(),
                    products.get(i).getTenpercentoff() == 0 ? 0 : 1,
                    products.get(i).getSecondonehalf(),
                    products.get(i).getHref(),
                    products.get(i).getBigimgsrc(),
                    products.get(i).getNewprice(),
                    products.get(i).getFraudcommentnum(),
                    products.get(i).getTenpercentoff() == 0 ? 0 : 1
            );
            bestSellerAndNewProducts.add(bestSellerAndNewProduct);
        }
        return ResultMsg.ok().data("recommend", bestSellerAndNewProducts);
    }

    public List<MustBuyProduct> getMustBuyProduct(String category, Integer num) {
        List<Product> productList = productService.list(new QueryWrapper<Product>().select("idProduct", "productDescription", "tenpercentoff", "secondOneHalf",
                        "href", "newPrice", "firstlevelcategoryrank", "bigImgSrc", "fraudcommentnum").eq("firstLevelCategory", category)
                .orderByDesc("firstlevelcategoryrank").last("limit 0," + num));

        List<MustBuyProduct> MustBuyProductList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            if (i >= num) break;
            MustBuyProduct mustBuyProduct = new MustBuyProduct(
                    productList.get(i).getProductdescription(),
                    productList.get(i).getTenpercentoff() == 0 ? 0 : 1,
                    productList.get(i).getSecondonehalf(),
                    productList.get(i).getHref(),
                    productList.get(i).getBigimgsrc(),
                    productList.get(i).getNewprice(),
                    productList.get(i).getTenpercentoff() == 0 ? 0 : 1,
                    productList.get(i).getFraudcommentnum()
            );
            MustBuyProductList.add(mustBuyProduct);
        }
        return MustBuyProductList;
    }

    //getBestSellerAndNewProduct方法的工具方法和getBestSellerPage的betSeller部分
    private List<BestSellerAndNewProduct> getBestSellerAndNewProductUtil(String str, Integer num) {
        List<SpecialEventDetail> specialEventDetailList = specialEventDetailService
                .list(new QueryWrapper<SpecialEventDetail>()
                .eq("specialcode", str).select("productid")
                .orderByDesc("categoryRankNum").last("limit 0," + num));
//        if (str.equals("bestSeller")) {
//            specialEventDetailList = specialEventDetailService.list(new QueryWrapper<SpecialEventDetail>()
//                    .eq("specialcode", "bestSeller").select("productid")
//                    .orderByDesc("categoryRankNum").last("limit 0," + num));
//        } else if (str.equals("timeseller")) {
//            specialEventDetailList = specialEventDetailService.list(new QueryWrapper<SpecialEventDetail>()
//                    .eq("specialcode", "timeseller").select("productid")
//                    .orderByDesc("categoryRankNum").last("limit 0," + num));
//        } else if (str.equals("newProduct")) {
//            specialEventDetailList = specialEventDetailService.list(new QueryWrapper<SpecialEventDetail>()
//                    .eq("specialcode", "newProduct").select("productid")
//                    .orderByDesc("categoryRankNum").last("limit 0," + num));
//        }
        List<Integer> productIDList = specialEventDetailList.stream()
                .map(SpecialEventDetail::getProductid).collect(Collectors.toList());
        List<Product> products = productService.list(new QueryWrapper<Product>()
                .select("idProduct", "productDescription", "tenpercentoff", "secondOneHalf", "href", "newPrice",
                        "bigImgSrc", "fraudcommentnum").in("idproduct", productIDList));

        List<BestSellerAndNewProduct> bestSellerAndNewProducts = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (i >= num) break;
            BestSellerAndNewProduct bestSellerAndNewProduct = new BestSellerAndNewProduct(
                    products.get(i).getProductdescription(),
                    products.get(i).getTenpercentoff() == 0 ? 0 : 1,
                    products.get(i).getSecondonehalf(),
                    products.get(i).getHref(),
                    products.get(i).getBigimgsrc(),
                    products.get(i).getNewprice(),
                    products.get(i).getFraudcommentnum(),
                    products.get(i).getTenpercentoff() == 0 ? 0 : 1
            );
            bestSellerAndNewProducts.add(bestSellerAndNewProduct);
        }
        return bestSellerAndNewProducts;
    }


    //getRankingItems方法的工具方法
    private List<ItemRankingProduct> getRankingItemsUtil(List<String> list1, String std) {
        List<Product> products;
        if (std.equals("firstlevelcategory")) {
            products = productService.list(new QueryWrapper<Product>().select("productdescription", "tenpercentoff"
                            , "secondonehalf", "fraudcommentnum", "href", "bigimgsrc", "newprice", "secondlevelcategoryrank", "firstlevelcategoryrank")
                    .eq(std, list1.get(0)).orderByDesc("firstlevelcategoryrank").last("limit 0,7"));
        } else {
            products = productService.list(new QueryWrapper<Product>().select("productdescription", "tenpercentoff"
                            , "secondonehalf", "fraudcommentnum", "href", "bigimgsrc", "newprice", "secondlevelcategoryrank", "firstlevelcategoryrank")
                    .eq(std, list1.get(0)).orderByDesc("secondlevelcategoryrank").last("limit 0,7"));
        }

        //把前几个放进去
        List<ItemRankingProduct> itemRankingProductList = new ArrayList<>();//存放一个请求属性的查询值
        for (int j = 0; j < products.size(); j++) {
            if (j >= 7) {
                break;
            }
            ItemRankingProduct itemRankingProduct = new ItemRankingProduct(
                    products.get(j).getProductdescription(),
                    (j + 1),
                    products.get(j).getTenpercentoff() == 0 ? 0 : 1,
                    products.get(j).getSecondonehalf(),
                    products.get(j).getHref(),
                    products.get(j).getBigimgsrc(),
                    products.get(j).getNewprice(),
                    products.get(j).getFraudcommentnum()
            );
            itemRankingProductList.add(itemRankingProduct);
        }
        return itemRankingProductList;
    }

    private List<ItemRankingProductForMobile> getRankingItemsUtilMobile(List<String> list1, String std) {
        List<Product> products;
        if (std.equals("firstlevelcategory")) {
            products = productService.list(new QueryWrapper<Product>().select("productdescription", "tenpercentoff"
                            , "secondonehalf", "fraudcommentnum", "href", "bigimgsrc", "newprice", "secondlevelcategoryrank", "firstlevelcategoryrank")
                    .eq(std, list1.get(0)).orderByDesc("firstlevelcategoryrank").last("limit 0,9"));
        } else {
            products = productService.list(new QueryWrapper<Product>().select("productdescription", "tenpercentoff"
                            , "secondonehalf", "fraudcommentnum", "href", "bigimgsrc", "newprice", "secondlevelcategoryrank", "firstlevelcategoryrank")
                    .eq(std, list1.get(0)).orderByDesc("secondlevelcategoryrank").last("limit 0,9"));
        }

        //把前几个放进去
        List<ItemRankingProductForMobile> itemRankingProductForMobileList = new ArrayList<>();//存放一个请求属性的查询值
        for (int j = 0; j < products.size(); j++) {
            if (j >= 9) {
                break;
            }
            ItemRankingProductForMobile itemRankingProductForMobile = new ItemRankingProductForMobile(
                    (j + 1),
                    products.get(j).getTenpercentoff() == 0 ? 0 : 1,
                    products.get(j).getSecondonehalf(),
                    products.get(j).getHref(),
                    products.get(j).getBigimgsrc(),
                    products.get(j).getNewprice(),
                    products.get(j).getFraudcommentnum()
            );
            itemRankingProductForMobileList.add(itemRankingProductForMobile);
        }
        return itemRankingProductForMobileList;
    }
}
