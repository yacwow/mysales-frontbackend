package com.duyi.readingweb.controller.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.bean.product.SalesPageProduct;
import com.duyi.readingweb.bean.product.SalesPageProductMobile;
import com.duyi.readingweb.bean.product.salesPageData.SalesPageBuyMatch;
import com.duyi.readingweb.bean.product.salesPageData.SalesPageComment;
import com.duyi.readingweb.bean.product.salesPageData.SalesPagePicture;
import com.duyi.readingweb.entity.eventManagement.SpecialEventDetail;
import com.duyi.readingweb.entity.framework.Webdata;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.entity.product.ProductImg;
import com.duyi.readingweb.entity.product.UserProductWishlist;
import com.duyi.readingweb.entity.user.Comment;
import com.duyi.readingweb.entity.user.UserVisitedProduct;
import com.duyi.readingweb.service.eventManagement.SpecialEventDetailService;
import com.duyi.readingweb.service.eventManagement.SpecialEventService;
import com.duyi.readingweb.service.framework.WebDataService;
import com.duyi.readingweb.service.product.ProductImgService;
import com.duyi.readingweb.service.product.ProductService;
import com.duyi.readingweb.service.product.UserProductWishlistService;
import com.duyi.readingweb.service.user.CommentService;
import com.duyi.readingweb.service.user.UserVisitedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class SalesPageProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImgService productImgService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserProductWishlistService userProductWishlistService;
    @Autowired
    private SpecialEventDetailService specialEventDetailService;
    @Autowired
    private UserVisitedProductService userVisitedProductService;
@Autowired
private WebDataService webDataService;
    @RequestMapping(value = "/api/salesPageProduct/{productId}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg getSalesPageProduct(@PathVariable("productId") Integer productId,
                                         @RequestParam Map<String, Object> params, HttpServletRequest request) {
        //访问次数增加了一次
        productService.update(new UpdateWrapper<Product>().eq("idproduct", productId)
                .setSql("visitednum=visitednum+1"));
        //单独的一个人的访问
        if (!params.isEmpty()) {
            String email = (String) params.get("userEmail");
            UserVisitedProduct userVisitedProduct = userVisitedProductService
                    .getOne(new QueryWrapper<UserVisitedProduct>()
                            .select("count").eq("useremail", email)
                            .eq("productid", productId));
            if (userVisitedProduct == null) {
                //新增一个
                UserVisitedProduct newUserVisitedProduct = new UserVisitedProduct();
                newUserVisitedProduct.setProductid(productId);
                newUserVisitedProduct.setUseremail(email);
                newUserVisitedProduct.setCount(1);
                userVisitedProductService.save(newUserVisitedProduct);
            } else {
                //count+1
                userVisitedProductService.update(new UpdateWrapper<UserVisitedProduct>()
                        .setSql("count=count+1").eq("useremail", email)
                        .eq("productid", productId));
            }
        }
        //根据登录与否判断是否喜欢该商品
        Map<String, Object> wishList = new HashMap<>();
        if (params.isEmpty()) {
            wishList.put("isLogin", false);
        } else {
            String email = (String) params.get("userEmail");
            UserProductWishlist userProductWishlist = userProductWishlistService
                    .getOne(new QueryWrapper<UserProductWishlist>().eq("useremail", email)
                            .eq("productid", productId));
            if (userProductWishlist != null) {
                wishList.put("userLove", true);
            }
            wishList.put("isLogin", true);
            wishList.put("userLove", false);
        }
        Product product = productService.getOne(new QueryWrapper<Product>().eq("idProduct", productId));
        List<ProductImg> productImgList = productImgService.list(new QueryWrapper<ProductImg>()
                .select("bigimgsrc", "smallimgsrc", "color", "first").eq("idProduct", productId));
        //变成salespagepicture
        List<SalesPagePicture> salesPagePictureList = new ArrayList<>();
        for (int i = 0; i < productImgList.size(); i++) {
            salesPagePictureList.add(new SalesPagePicture(productImgList.get(i).getBigimgsrc(),
                    productImgList.get(i).getSmallimgsrc(),
                    productImgList.get(i).getColor(), productImgList.get(i).getFirst()));
        }
        List<Comment> commentList = commentService.list(new QueryWrapper<Comment>()
                .select("name", "commentdetail", "updatedtime", "imgsrc", "ratestar")
                .eq("productid_comment", productId));
        //变成salespagecomment
        List<SalesPageComment> salesPageCommentList = new ArrayList<>();
        for (int i = 0; i < commentList.size(); i++) {
            salesPageCommentList.add(new SalesPageComment(commentList.get(i).getName(),
                    commentList.get(i).getCommentdetail(),
                    commentList.get(i).getUpdatedtime(),
                    commentList.get(i).getImgsrc(),
                    commentList.get(i).getRatestar()
            ));
        }
        //找二级目录相同的商品，随机发出去14个
        List<Product> productsWithSameCategory = productService.list(new QueryWrapper<Product>()
                .select("tenpercentoff", "secondonehalf", "href", "bigimgsrc", "newprice", "marketprice", "idproduct")
                .eq("secondlevelcategory", product.getSecondlevelcategory())
                .orderByDesc("RAND()")
                .last("LIMIT 14"));
//        Collections.shuffle(productsWithSameCategory);
        List<SalesPageBuyMatch> salesPageBuyMatchList = new ArrayList<>();
        for (int i = 0; i < productsWithSameCategory.size(); i++) {
            if (i >= 14) break;
            List<ProductImg> productImgList1 = productImgService.list(new QueryWrapper<ProductImg>()
                    .select("bigimgsrc").eq("idproduct", productsWithSameCategory.get(i).getIdproduct()));
            salesPageBuyMatchList.add(new SalesPageBuyMatch(
                    productsWithSameCategory.get(i).getTenpercentoff() == 0 ? 0 : 1,
                    productsWithSameCategory.get(i).getSecondonehalf(),
                    productsWithSameCategory.get(i).getHref(),
                    productImgList1.get(0).getBigimgsrc(),
                    productsWithSameCategory.get(i).getNewprice(),
                    productsWithSameCategory.get(i).getMarketprice()

            ));
        }
        SpecialEventDetail specialEventDetail = specialEventDetailService.getOne(new QueryWrapper<SpecialEventDetail>()
                .select("productid").eq("specialcode", "timeseller").eq("productid", productId));
        SalesPageProduct salesPageProduct = new SalesPageProduct(
                product.getIdproduct(),
                product.getProductdetailsize(),
                product.getProductdetaildescription(),
                product.getNewprice(),
                product.getMarketprice(),
                product.getOriginprice(),
                product.getTenpercentoff() == 0 ? 0 : 1,
                product.getSecondonehalf(),
                product.getProductdescription(),
                product.getHref(),
                product.getFraudsoldnum(),
                product.getFraudwishlistnum(),
                specialEventDetail == null ? 0 : 1,
                wishList,
                product.getFirstlevelcategory(),
                product.getSecondlevelcategory(),
                salesPageCommentList,
                salesPagePictureList,
                salesPageBuyMatchList
        );
        return ResultMsg.ok().data("salesPageProduct", salesPageProduct);
    }

    /**
     * 这个函数以后根据客户不同提供不同搜索结果，现在就和原来的差不多凑合凑合
     */
    @RequestMapping("/api/salesPageProduct/bestSeller")
    public ResultMsg getBestSeller() {
        List<Product> productList = productService.list(new QueryWrapper<Product>()
                .select("href", "productdescription", "newprice", "idproduct", "bigimgsrc").orderByDesc("RAND()")
                .last("LIMIT 14"));
//        Collections.shuffle(productList);
        List<Map<String, Object>> res = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            if (i >= 14) break;
            Map<String, Object> map = new HashMap<>();
            map.put("imgSrc", productList.get(i).getBigimgsrc());
            map.put("productDescription", productList.get(i).getProductdescription());
            map.put("price", productList.get(i).getNewprice());
            map.put("href", productList.get(i).getHref());
            res.add(map);
        }
        return ResultMsg.ok().data("newsBanner", res);
    }

    @RequestMapping(value = "/api/salesPageProductMobile/{productId}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg getSalesPageProductMobile(@PathVariable("productId") Integer productId, @RequestParam Map<String, Object> params, HttpServletRequest request) {
        System.out.println(productId);
        //访问次数增加了一次
        productService.update(new UpdateWrapper<Product>().eq("idproduct", productId)
                .setSql("visitednum=visitednum+1"));
        //单独的一个人的访问
        if (!params.isEmpty()) {
            String email = (String) params.get("userEmail");
            UserVisitedProduct userVisitedProduct = userVisitedProductService
                    .getOne(new QueryWrapper<UserVisitedProduct>()
                            .select("count").eq("useremail", email)
                            .eq("productid", productId));
            if (userVisitedProduct == null) {
                //新增一个
                UserVisitedProduct newUserVisitedProduct = new UserVisitedProduct();
                newUserVisitedProduct.setProductid(productId);
                newUserVisitedProduct.setUseremail(email);
                newUserVisitedProduct.setCount(1);
                userVisitedProductService.save(newUserVisitedProduct);
            } else {
                //count+1
                userVisitedProductService.update(new UpdateWrapper<UserVisitedProduct>()
                        .setSql("count=count+1").eq("useremail", email)
                        .eq("productid", productId));
            }
        }
        //根据登录与否判断是否喜欢该商品
        Map<String, Object> wishList = new HashMap<>();
        if (params.isEmpty()) {
            wishList.put("isLogin", false);
        } else {
            String email = (String) params.get("userEmail");
            UserProductWishlist userProductWishlist = userProductWishlistService
                    .getOne(new QueryWrapper<UserProductWishlist>().eq("useremail", email)
                            .eq("productid", productId));
            if (userProductWishlist != null) {
                wishList.put("userLove", true);
            } else {
                wishList.put("userLove", false);
            }
            wishList.put("isLogin", true);

        }
        Product product = productService.getOne(new QueryWrapper<Product>().eq("idProduct", productId));
        List<ProductImg> productImgList = productImgService.list(new QueryWrapper<ProductImg>()
                .select("bigimgsrc", "smallimgsrc", "color", "first").eq("idProduct", productId));
        //变成salespagepicture
        List<SalesPagePicture> salesPagePictureList = new ArrayList<>();
        for (int i = 0; i < productImgList.size(); i++) {
            salesPagePictureList.add(new SalesPagePicture(productImgList.get(i).getBigimgsrc(),
                    productImgList.get(i).getSmallimgsrc(),
                    productImgList.get(i).getColor(), productImgList.get(i).getFirst()));
        }
        List<Comment> commentList = commentService.list(new QueryWrapper<Comment>()
                .select("name", "commentdetail", "updatedtime", "imgsrc", "ratestar")
                .eq("productid_comment", productId));
        //变成salespagecomment
        List<SalesPageComment> salesPageCommentList = new ArrayList<>();
        for (int i = 0; i < commentList.size(); i++) {
            salesPageCommentList.add(new SalesPageComment(commentList.get(i).getName(),
                    commentList.get(i).getCommentdetail(),
                    commentList.get(i).getUpdatedtime(),
                    commentList.get(i).getImgsrc(),
                    commentList.get(i).getRatestar()
            ));
        }
        //找二级目录相同的新品商品，随机发出去8个
        List<Product> productsWithSameCategory = productService.list(new QueryWrapper<Product>()
                .select("tenpercentoff", "secondonehalf", "href", "bigimgsrc", "newprice", "marketprice", "idproduct")
                .eq("secondlevelcategory", product.getSecondlevelcategory()).orderByDesc("tenpercentoff")
                .orderByDesc("RAND()").ne("idproduct",productId)
                .last("LIMIT 18"));
//        Collections.shuffle(productsWithSameCategory);
        List<Map<String, Object>> salesPageBuyMatchList = new ArrayList<>();
        for (int i = 0; i < productsWithSameCategory.size(); i++) {
            if (i >= 17) break;
            List<ProductImg> productImgList1 = productImgService.list(new QueryWrapper<ProductImg>()
                    .select("bigimgsrc").eq("idproduct", productsWithSameCategory.get(i).getIdproduct())
                    .orderByDesc("first").last("limit 1"));
            Map<String, Object> salesPageBuyMath = new HashMap<>();
            salesPageBuyMath.put("href", productsWithSameCategory.get(i).getHref());
            salesPageBuyMath.put("bigImgSrc", productImgList1.get(0).getBigimgsrc());
            salesPageBuyMatchList.add(salesPageBuyMath);
        }
        //是不是timeseller的东西
        SpecialEventDetail specialEventDetail = specialEventDetailService.getOne(new QueryWrapper<SpecialEventDetail>()
                .select("productid").eq("specialcode", "timeseller").eq("productid", productId));


        //后面需要加时间的限定，redis之后吧

        //        if(specialEventDetail!=null){
//            webDataService.list(new QueryWrapper<Webdata>().select("expiretime")
//                    .orderByDesc("expiretime").eq("weburl","/timeseller"));
//        }
//        // 哪些需要查询
//        String[] webUrls = {"/bestSeller", "/timeseller", "/dailyNew", "/discount"};

        SalesPageProductMobile salesPageProduct = new SalesPageProductMobile(
                product.getIdproduct(),
                product.getProductdetailsize(),
                product.getProductdetaildescription(),
                product.getNewprice(),
                product.getMarketprice(),
                product.getOriginprice(),
                product.getTenpercentoff() == 0 ? 0 : 1,
                product.getSecondonehalf(),
                product.getProductdescription(),
                product.getHref(),
                product.getFraudsoldnum(),
                product.getFraudwishlistnum(),
                specialEventDetail == null ? 0 : 1,
                wishList,
                product.getFirstlevelcategory(),
                product.getSecondlevelcategory(),
                salesPageCommentList,
                salesPagePictureList,
                salesPageBuyMatchList
        );
        return ResultMsg.ok().data("salesPageProduct", salesPageProduct);
    }


    @RequestMapping(value = "/api/salesPageProductRelatedList/{productId}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg salesPageProductRelatedList(@PathVariable("productId") Integer productId, @RequestParam Map<String, Object> params, HttpServletRequest request) {
        //先找到timeseller的相关信息，先取出四十个，然后随机拿出其中的十四个
        List<SpecialEventDetail> specialEventDetailList = specialEventDetailService
                .list(new QueryWrapper<SpecialEventDetail>().select("productid")
                        .eq("specialcode", "timeseller").ne("productid",productId)
                        .orderByDesc("categoryRankNum").last("limit 40"));
        List<Integer> timeSellerProductIdList = specialEventDetailList.stream()
                .map(SpecialEventDetail::getProductid).collect(Collectors.toList());
        //随机取出14个
        List<Integer> selectedTimeSellerProductIds = new ArrayList<>();
        Random rand = new Random();
        while (selectedTimeSellerProductIds.size() < 14 && !timeSellerProductIdList.isEmpty()) {
            int randomIndex = rand.nextInt(timeSellerProductIdList.size());
            Integer selectedProductId = timeSellerProductIdList.get(randomIndex);
            selectedTimeSellerProductIds.add(selectedProductId);
            timeSellerProductIdList.remove(randomIndex);
        }
        List<Product> timeSellerProductList = productService.list(new QueryWrapper<Product>()
                .select("secondonehalf", "href", "bigimgsrc", "marketprice", "newprice")
                .in("idproduct", selectedTimeSellerProductIds));


        //然后是新品的信息，最好是非同类的，因为同类我已经有展示了,先取出四十个，然后随机拿出其中的十四个
        Product targetProduct = productService.getOne(new QueryWrapper<Product>().eq("idproduct", productId)
                .select("secondlevelcategory"));
        List<Product> top40NewProducts = productService.list(new QueryWrapper<Product>()
                .select("secondonehalf", "href", "bigimgsrc", "marketprice", "newprice")
                .orderByDesc("tenpercentoff")
                .notIn("secondlevelcategory", targetProduct.getSecondlevelcategory()).last("limit 40"));
        List<Product> selectedNewProducts = new ArrayList<>();
        Random random = new Random();
        while (selectedNewProducts.size() < 14 && !top40NewProducts.isEmpty()) {
            int index = random.nextInt(top40NewProducts.size());
            Product newProduct = top40NewProducts.get(index);
            selectedNewProducts.add(newProduct);
            top40NewProducts.remove(index);
        }


        //然后是经常浏览的产品
        //如果已经登录了，就给登录后记录的信息，等下单独创建一张表，不满15就用推荐来填充
        //没有登录，就纯纯的按照推荐的商品来选择15个
        List<Product> recentVisitedProduct;
        if (!params.isEmpty()) {
            //已经登录了
            String email = (String) params.get("userEmail");
            List<UserVisitedProduct> userVisitedProductList = userVisitedProductService
                    .list(new QueryWrapper<UserVisitedProduct>().select("count", "productid")
                            .orderByDesc("count").eq("useremail", email));
            Integer size = userVisitedProductList.size();
            if (size >= 15) {
                List<Integer> newProductIdList = userVisitedProductList.stream()
                        .map(UserVisitedProduct::getProductid)
                        .limit(15)
                        .collect(Collectors.toList());
                recentVisitedProduct = productService.list(new QueryWrapper<Product>()
                        .select("href", "bigimgsrc", "newprice")
                        .in("idproduct", newProductIdList));
            } else {
                Integer limit = 15 - size;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("limit 0,");
                stringBuilder.append(limit);
                //还要去recommend里面取最大的几个
                List<SpecialEventDetail> specialEventDetailList1 = specialEventDetailService
                        .list(new QueryWrapper<SpecialEventDetail>().select("productid")
                                .eq("specialcode", "recommend").orderByDesc("categoryRankNum")
                                .last(stringBuilder.toString()));
                List<Integer> newProductIdList = Stream.concat(
                                specialEventDetailList1.stream().map(SpecialEventDetail::getProductid),
                                userVisitedProductList.stream().map(UserVisitedProduct::getProductid))
                        .distinct().collect(Collectors.toList());
                recentVisitedProduct = productService.list(new QueryWrapper<Product>()
                        .select("href", "bigimgsrc", "newprice")
                        .in("idproduct", newProductIdList));
            }
        } else {
            //没有登录
            List<SpecialEventDetail> specialEventDetailList1 = specialEventDetailService
                    .list(new QueryWrapper<SpecialEventDetail>().select("productid")
                            .eq("specialcode", "recommend").orderByDesc("categoryRankNum")
                            .last("limit 0,15"));
            List<Integer> recommendProductIds = specialEventDetailList1.stream().map(specialEventDetail -> specialEventDetail.getProductid())
                    .collect(Collectors.toList());
            recentVisitedProduct = productService.list(new QueryWrapper<Product>()
                    .select("href", "bigimgsrc", "newprice")
                    .in("idproduct", recommendProductIds));
        }
        Map<String, Object> resMap = new HashMap<>();
        List<Map<String, Object>> recentVisitedProductList = new ArrayList<>();
        for (int i = 0; i < recentVisitedProduct.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("href", recentVisitedProduct.get(i).getHref());
            map.put("bigImgSrc", recentVisitedProduct.get(i).getBigimgsrc());
            map.put("price", recentVisitedProduct.get(i).getNewprice());
            recentVisitedProductList.add(map);
        }
        List<Map<String, Object>> selectedNewProductList = getProductInfo(selectedNewProducts);
        List<Map<String, Object>> selectedTimeSellerProductList = getProductInfo(timeSellerProductList);
        resMap.put("recentVisitedProductList", recentVisitedProductList);
        resMap.put("selectedNewProductList", selectedNewProductList);
        resMap.put("selectedTimeSellerProductList", selectedTimeSellerProductList);
        return ResultMsg.ok().result(true).data(resMap);
    }

    @RequestMapping(value = "/api/getSalesPageRelatedProductMobile/{productId}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg getSalesPageRelatedProductMobile(@PathVariable("productId") Integer productId) {
        //获取产品的productdetailsize，img，不同颜色的img，filter好了再过去，product price
        Product product = productService.getOne(new QueryWrapper<Product>().eq("idproduct", productId)
                .select("productdetailsize", "idproduct", "bigimgsrc", "newprice"));
        List<ProductImg> productImgList = productImgService.list(new QueryWrapper<ProductImg>()
                .eq("idproduct", productId)
                .select("bigimgsrc", "color", "first").orderByDesc("first"));
        List<Map<String, Object>> resultList = new ArrayList<>();
        Set<String> colorSet = new HashSet<>();

        for (ProductImg productImg : productImgList) {
            String color = productImg.getColor();
            String bigImgSrc = productImg.getBigimgsrc();

            if (!colorSet.contains(color)) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("color", color);
                resultMap.put("bigImgSrc", bigImgSrc);
                resultList.add(resultMap);
                colorSet.add(color);
            }
        }
        Map<String,Object> productData=new HashMap<>();
        productData.put("productId",product.getIdproduct());
        productData.put("productDetailSize",product.getProductdetailsize());
        productData.put("bigImgSrc",product.getBigimgsrc());
        productData.put("newPrice",product.getNewprice());
        productData.put("colorPicture",resultList);
        return ResultMsg.ok().result(true).data(productData);
    }

    private List<Map<String, Object>> getProductInfo(List<Product> timeSellerProductList) {
        List<Map<String, Object>> selectedTimeSellerProductList = new ArrayList<>();
        for (int i = 0; i < timeSellerProductList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("href", timeSellerProductList.get(i).getHref());
            map.put("bigImgSrc", timeSellerProductList.get(i).getBigimgsrc());
            map.put("price", timeSellerProductList.get(i).getNewprice());
            map.put("secondOneHalf", timeSellerProductList.get(i).getSecondonehalf() > 0 ? 1 : 0);
            map.put("originPrice", timeSellerProductList.get(i).getMarketprice());
            selectedTimeSellerProductList.add(map);
        }
        return selectedTimeSellerProductList;
    }

}
