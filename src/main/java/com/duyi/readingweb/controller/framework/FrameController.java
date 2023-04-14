package com.duyi.readingweb.controller.framework;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.entity.framework.Newbanner;
import com.duyi.readingweb.entity.framework.Pickupitem;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.service.framework.NewBannerService;
import com.duyi.readingweb.service.framework.PickupItemService;
import com.duyi.readingweb.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class FrameController {
    @Autowired
    private NewBannerService newBannerService;
    @Autowired
    private ProductService productService;

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


    @Autowired
    private PickupItemService pickupItemService;
//    这里的rank不是product的rank
    @RequestMapping("/api/pickUp")
    public ResultMsg getRecommend() {
        List<Pickupitem> pickupItemList = pickupItemService.list(new QueryWrapper<Pickupitem>()
                .gt("expireTime", new Date().getTime()).select("href", "imgSrc", "expireTime", "intro", "rank"));
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
        List<Product> productList = productService.list(new QueryWrapper<Product>().select("idproduct","firstlevelcategory","productdescription","recommend","secondlevelcategory")
                .like("productdescription", value));
        productList.sort(new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o2.getRecommend()-o1.getRecommend();
            }
        });
        List<Map<String,Object>> resList=new ArrayList<>();
        Boolean moreThan5=false;
        for (int i = 0; i < productList.size(); i++) {
            if(i>=5) {
                moreThan5=true;
                break;
            }
            Map<String,Object> map=new HashMap<>();
            map.put("idproduct",productList.get(i).getIdproduct());
            map.put("productdescription",productList.get(i).getProductdescription());
            map.put("secondlevelcategory",productList.get(i).getSecondlevelcategory());
            map.put("firstlevelcategory",productList.get(i).getFirstlevelcategory());
            resList.add(map);
        }
        Map<String,Object> resMap=new HashMap<>();
        resMap.put("data",resList);
        resMap.put("moreThan5",moreThan5);
        return ResultMsg.ok().data(resMap);
    }
}
