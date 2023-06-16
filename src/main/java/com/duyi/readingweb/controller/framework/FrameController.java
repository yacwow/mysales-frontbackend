package com.duyi.readingweb.controller.framework;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.entity.couponlist.Couponlist;
import com.duyi.readingweb.entity.framework.Newbanner;
import com.duyi.readingweb.entity.framework.Pickupitem;
import com.duyi.readingweb.entity.framework.Webdata;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.service.couponlist.CouponlistService;
import com.duyi.readingweb.service.framework.NewBannerService;
import com.duyi.readingweb.service.framework.PickupItemService;
import com.duyi.readingweb.service.framework.WebDataService;
import com.duyi.readingweb.service.product.ProductService;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class FrameController {
    private final Integer activeDay = 3;
    private final Integer searchPageSize = 60;
    @Autowired
    private NewBannerService newBannerService;
    @Autowired
    private ProductService productService;
    @Autowired
    private WebDataService webDataService;
    @Autowired
    private PickupItemService pickupItemService;
    @Autowired
    private CouponlistService couponlistService;

    @RequestMapping("/api/newsBanner")
    public ResultMsg getNewsBanner() {
        List<Map<String, Object>> firstLine = newBannerService.listMaps(new QueryWrapper<Newbanner>()
                .select("url", "imgSrc", "showedNum").gt("showedNum", 0).eq("line", 1)
        );
        List<Map<String, Object>> secondLine = newBannerService.listMaps(new QueryWrapper<Newbanner>()
                .select("url", "imgSrc", "showedNum").gt("showedNum", 0).eq("line", 2)
        );
        List<Map<String, Object>> thirdLine = newBannerService.listMaps(new QueryWrapper<Newbanner>()
                .select("url", "imgSrc", "showedNum").gt("showedNum", 0).eq("line", 3)
        );
        Map<String, List<Map<String, Object>>> map = new HashMap<>();
        map.put("firstLine", firstLine);
        map.put("secondLine", secondLine);
        map.put("thirdLine", thirdLine);

        return ResultMsg.ok().data("newBanner", map);
    }

    //    这里的rank不是product的rank
    @RequestMapping("/api/pickUp")
    public ResultMsg getRecommend() {
        List<Pickupitem> pickupItemList = pickupItemService.list(new QueryWrapper<Pickupitem>()
                .gt("expireTime", DateTime.now().toDate()).select("href", "imgsrc", "expiretime", "intro", "rank"));
        pickupItemList.sort(new Comparator<Pickupitem>() {
            @Override
            public int compare(Pickupitem o1, Pickupitem o2) {
                return o2.getRank() - o1.getRank();
            }
        });
//        System.out.println(pickupItemList);
        return ResultMsg.ok().data("pickUp", pickupItemList);
    }


    @RequestMapping(value = "/api/fuzzySearchProduct", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg fuzzySearchProduct(@RequestParam Map<String, Object> params) {
        String value = (String) params.get("value");
        List<Product> productList = productService.list(new QueryWrapper<Product>().select("idproduct", "firstlevelcategory", "productdescription", "secondlevelcategory")
                .like("productdescription", value));
        List<Map<String, Object>> resList = new ArrayList<>();
        Boolean moreThan5 = false;
        for (int i = 0; i < productList.size(); i++) {
            if (i >= 5) {
                moreThan5 = true;
                break;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("idproduct", productList.get(i).getIdproduct());
            map.put("productdescription", productList.get(i).getProductdescription());
            map.put("secondlevelcategory", productList.get(i).getSecondlevelcategory());
            map.put("firstlevelcategory", productList.get(i).getFirstlevelcategory());
            resList.add(map);
        }
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("data", resList);
        resMap.put("moreThan5", moreThan5);
        return ResultMsg.ok().data(resMap);
    }

    @RequestMapping(value = "/api/getMobileSearchedProducts", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg getMobileSearchedProducts(@RequestParam Map<String, Object> params) {
        Integer page = Integer.parseInt((String) params.get("page"));
        String sortType = (String) params.get("sortType");
        String order = (String) params.get("order");
        String searchParams = (String) params.get("searchParams");
        String searchType = (String) params.get("searchType");
        System.out.println(page + sortType + order + searchParams + searchType);
        Integer productNum;
        List<Map<String, Object>> resData = new ArrayList<>();
        List<Product> productList;
        Map<String, Object> searchData = new HashMap<>();
        //每一页的数量 limit 0,60
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("limit ").append((page - 1) * searchPageSize).append(",").append(page * searchPageSize);
        //判断，productDetailDescription或者productDescription
        if (searchType.equals("productDescription")) {
            productNum = (int) productService.count(new QueryWrapper<Product>()
                    .select("idproduct").like("productdescription", searchParams));
            productList = productService.list(new QueryWrapper<Product>()
                    .select("bigimgsrc", "productdescription", "href", "newprice")
                    .like("productdescription", searchParams).last(stringBuilder.toString()));

        } else {
            productNum = (int) productService.count(new QueryWrapper<Product>()
                    .select("idproduct").like("productdetaildescription", searchParams));
            productList = productService.list(new QueryWrapper<Product>()
                    .select("bigimgsrc", "productdescription", "href", "newprice")
                    .like("productdetaildescription", searchParams).last(stringBuilder.toString()));
        }
        for (int i = 0; i < productList.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("imgSrc", productList.get(i).getBigimgsrc());
            map.put("productDescription", productList.get(i).getProductdescription());
            map.put("href", productList.get(i).getHref());
            map.put("price", productList.get(i).getNewprice());
            resData.add(map);
        }
        searchData.put("productNum", productNum);
        searchData.put("resData", resData);

        return ResultMsg.ok().result(true).data(searchData);
    }

    @RequestMapping(value = "/api/getMobileFrequentSearchParam")
    public ResultMsg getMobileFrequentSearchParam() {
        List<Webdata> webdataList = webDataService.list(new QueryWrapper<Webdata>().eq("weburl", "/searchResultShow")
                .select("searchparams"));
        if (webdataList.size() > 0 && (!webdataList.get(0).getSearchparams().equals("") || (webdataList.get(0).getSearchparams() != null))) {
            List<String> list = Arrays.stream(webdataList.get(0).getSearchparams().split(",")).toList();
            return ResultMsg.ok().result(true).data("frequentSearchParamList", list);
        }
        return ResultMsg.ok().result(false);
    }

    @RequestMapping(value = "/api/getExpireTime")
    public ResultMsg getExpireTime() {
        //先求timeseller的expiretime
        List<Webdata> webdataList = webDataService.list(new QueryWrapper<Webdata>()
                .eq("weburl", "/timeseller")
                .select("expiretime").orderByDesc("expiretime"));
        Date expireTime = webdataList.get(0).getExpiretime();
        long targetTime = (expireTime != null) ? expireTime.getTime() : 0L;
        long currentTimeMillis = System.currentTimeMillis();
        if (targetTime == 0L || targetTime < currentTimeMillis) {
            webDataService.update(new UpdateWrapper<Webdata>().eq("weburl", "/timeseller")
                    .set("expiretime", new Date(currentTimeMillis + 1000 * 3600 * 24 * activeDay)));
            targetTime = currentTimeMillis + 1000 * 3600 * 24 * activeDay;
        }
        //再求secondHalfPrice的倒计时
        List<Webdata> webdataListForSecondHalf = webDataService.list(new QueryWrapper<Webdata>()
                .eq("weburl", "/secondHalfPrice")
                .select("expiretime").orderByDesc("expiretime"));
        Date expireTime1 = webdataListForSecondHalf.get(0).getExpiretime();
        long targetTime1 = (expireTime1 != null) ? expireTime1.getTime() : 0L;
        if (targetTime1 == 0L || targetTime1 < currentTimeMillis) {
            webDataService.update(new UpdateWrapper<Webdata>().eq("weburl", "/secondHalfPrice")
                    .set("expiretime", new Date(currentTimeMillis + 1000 * 3600 * 24 * activeDay)));
            targetTime1 = currentTimeMillis + 1000 * 3600 * 24 * activeDay;
        }
        //再求discount的倒计时
        List<Webdata> webdataListForDiscount = webDataService.list(new QueryWrapper<Webdata>()
                .eq("weburl", "/discount")
                .select("expiretime").orderByDesc("expiretime"));
        Date expireTime2 = webdataListForDiscount.get(0).getExpiretime();
        long targetTime2 = (expireTime2 != null) ? expireTime2.getTime() : 0L;
        if (targetTime2 == 0L || targetTime2 < currentTimeMillis) {
            webDataService.update(new UpdateWrapper<Webdata>().eq("weburl", "/discount")
                    .set("expiretime", new Date(currentTimeMillis + 1000 * 3600 * 24 * activeDay)));
            targetTime2 = currentTimeMillis + 1000 * 3600 * 24 * activeDay;
        }
        Map<String, Object> resData = new HashMap<>();
        resData.put("expireTime", targetTime);
        resData.put("secondHalfPriceExpireTime", targetTime1);
        resData.put("discountExpireTime", targetTime2);

        return ResultMsg.ok().result(true).data(resData);
    }

    @ApiOperation("单页的最顶端图片")
    @RequestMapping(value = "/api/getHeadPicturesOfSomePage")
    public ResultMsg getHeadPicturesOfSomePage() {
        Map<String, Object> finalResultMap = new HashMap<>();
        // 哪些需要查询
        String[] webUrls = {"/bestSeller", "/timeseller", "/dailyNew", "/discount"};

        for (String webUrl : webUrls) {
            List<Webdata> webdataList =
                    webDataService.list(
                            new QueryWrapper<Webdata>()
                                    .select( "weburl", "imgsrc")
                                    .eq("weburl", webUrl).gt("rank",0));

            List<Map<String, Object>> resultMapList = new ArrayList<>();

            for (Webdata webdata : webdataList) {
                Map<String, Object> resultMap = new HashMap<>();
                resultMap.put("url", webdata.getWeburl());
                resultMap.put("imgSrc", webdata.getImgsrc());
                resultMapList.add(resultMap);
            }

            finalResultMap.put(webUrl, resultMapList);
        }
        return ResultMsg.ok().result(true).data("headerPictureList", finalResultMap);
    }



}
