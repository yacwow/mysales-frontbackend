package com.duyi.readingweb.controller.product;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.bean.product.BestSellerAndNewProduct;
import com.duyi.readingweb.bean.product.ItemRankingProduct;
import com.duyi.readingweb.bean.product.MustBuyProduct;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.entity.product.ProductImg;
import com.duyi.readingweb.entity.user.Comment;
import com.duyi.readingweb.service.product.ProductImgService;
import com.duyi.readingweb.service.product.ProductService;
import com.duyi.readingweb.service.user.CommentService;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImgService productImgService;

    @RequestMapping(value="/api/itemRanking",method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg getRankingItems(@RequestParam Map<String, Object> params) {
        System.out.println(params);
        List<String> list=new ArrayList<>();
        Set<String> keys= params.keySet();
        for (String key:keys
             ) {
            list.add((String)params.get(key));
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
//    public ResultMsg getRankingItems(HttpServletRequest request) {
//        String params = request.getParameter("data");
//        List<String> list = (List<String>) JSON.parse(params);
//        Map<String, List<ItemRankingProduct>> resMap = new HashMap<>();//最后存放结果的集合
//        for (int i = 0; i < list.size(); i++) {
//            String searchParam = list.get(i);
//            //返回的是swimwearInner-2  2表示secondlevelcategory，1表示firstlevelcategory
//            String[] strings = searchParam.split("-");
//            List<String> list1 = Arrays.asList(strings);//请求的数据的拆分
//            if (Integer.parseInt(list1.get(1)) == 2) {
////                List<Product> products = productService.list(new QueryWrapper<Product>()
////                        .gt("rank", 0).eq("secondlevelcategory", list1.get(0)));
////                products.sort(new Comparator<Product>() {
////                    @Override
////                    public int compare(Product o1, Product o2) {
////                        return o2.getRank() - o1.getRank();
////                    }
////                });
////                //把前七个放进去
////                List<ItemRankingProduct> itemRankingProductList = new ArrayList<>();//存放一个请求属性的查询值
////                for (int j = 0; j < products.size(); j++) {
////                    if (j >= 7) {
////                        break;
////                    }
////                    Integer idProduct = products.get(j).getIdproduct();
//////                    System.out.println(idProduct);
////                    List<ProductImg> productImgList = productImgService.list(new QueryWrapper<ProductImg>().select("bigImgSrc", "first")
////                            .eq("idproduct", idProduct));
////                    String imgSrc = "";
////                    for (int k = 0; k < productImgList.size(); k++) {
////                        if (productImgList.get(k).getFirst() == 1) {
////                            imgSrc = productImgList.get(k).getBigimgsrc();
////                            break;
////                        }
////                    }
////                    if (imgSrc.equals("")) {
////                        imgSrc = productImgList.get(0).getBigimgsrc();
////                    }
////                    ItemRankingProduct itemRankingProduct = new ItemRankingProduct(
////                            products.get(j).getProductdescription(),
////                            products.get(j).getRank(),
////                            products.get(j).getTenpercentoff(),
////                            products.get(j).getSecondonehalf(),
////                            products.get(j).getHref(),
////                            imgSrc
////                    );
////                    System.out.println(itemRankingProduct);
////                    itemRankingProductList.add(itemRankingProduct);
////                }
//////                System.out.println(products);
////                System.out.println(itemRankingProductList);
//                resMap.put(list1.get(0), getRankingItemsUtil(list1, "secondlevelcategory"));
//            } else {
//                resMap.put(list1.get(0), getRankingItemsUtil(list1, "firstlevelcategory"));
//            }
////            System.out.println(resMap);
//        }
//        return ResultMsg.ok().data("itemRanking", resMap);
//    }

    @Autowired
    private CommentService commentService;

    @RequestMapping("/api/bestSellerAndNewProduct/{number}")
    public ResultMsg getBestSellerAndNewProduct(@PathVariable("number") Integer number) {
        List<BestSellerAndNewProduct> bestSellerList = getBestSellerAndNewProductUtil("bestSeller", number);
        List<BestSellerAndNewProduct> newProductList = getBestSellerAndNewProductUtil("newProduct", number);
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("bestSeller", bestSellerList);
        resMap.put("newProduct", newProductList);

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

    @RequestMapping("/api/bestSellerPage/{number}")
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

    @RequestMapping("/api/getRecommendProduct/{number}")
    public ResultMsg getRecommendProduct(@PathVariable("number") Integer number) {
        List<Product> products = productService.list(new QueryWrapper<Product>().select("idProduct", "productDescription", "tenpercentoff",
                "secondOneHalf", "href", "newPrice", "recommend", "newProduct","bigimgsrc","commentnum").isNotNull("recommend"));
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o2.getRecommend() - o1.getRecommend();
            }
        });
        System.out.println(products);
        List<BestSellerAndNewProduct> bestSellerAndNewProducts = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (i >= number) break;
            BestSellerAndNewProduct bestSellerAndNewProduct = new BestSellerAndNewProduct(
                    products.get(i).getProductdescription(),
                    products.get(i).getTenpercentoff(),
                    products.get(i).getSecondonehalf(),
                    products.get(i).getHref(),
                    products.get(i).getBigimgsrc(),
                    products.get(i).getNewprice(),
                    products.get(i).getCommentnum(),
                    products.get(i).getNewproduct()
            );
            bestSellerAndNewProducts.add(bestSellerAndNewProduct);
        }
        return ResultMsg.ok().data("recommend", bestSellerAndNewProducts);
    }

    public List<MustBuyProduct> getMustBuyProduct(String category, Integer num) {
        List<Product> productList = productService.list(new QueryWrapper<Product>().select("idProduct", "productDescription", "tenpercentoff", "secondOneHalf",
                "href", "newPrice", "firstlevelcategoryrank", "newProduct", "bigImgSrc").eq("firstLevelCategory", category));
        productList.sort(new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o2.getFirstlevelcategoryrank() - o1.getFirstlevelcategoryrank();
            }
        });
        List<MustBuyProduct> MustBuyProductList = new ArrayList<>();
        for (int i = 0; i < productList.size(); i++) {
            if (i >= num) break;


            MustBuyProduct mustBuyProduct = new MustBuyProduct(
                    productList.get(i).getProductdescription(),
                    productList.get(i).getTenpercentoff(),
                    productList.get(i).getSecondonehalf(),
                    productList.get(i).getHref(),
                    productList.get(i).getBigimgsrc(),
                    productList.get(i).getNewprice(),
                    productList.get(i).getNewproduct()
            );
            MustBuyProductList.add(mustBuyProduct);
        }
        return MustBuyProductList;
    }

    //getBestSellerAndNewProduct方法的工具方法和getBestSellerPage的betSeller部分
    private List<BestSellerAndNewProduct> getBestSellerAndNewProductUtil(String str, Integer num) {
        List<Product> products = productService.list(new QueryWrapper<Product>().select("idProduct", "productDescription", "tenpercentoff",
                "secondOneHalf", "href", "newPrice", "bestseller", "newProduct", "bigImgSrc","commentNum").eq(str, true));
        products.sort(new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o2.getBestseller() - o1.getBestseller();
            }
        });
//        System.out.println(products);
        List<BestSellerAndNewProduct> bestSellerAndNewProducts = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            if (i >= num) break;
            BestSellerAndNewProduct bestSellerAndNewProduct = new BestSellerAndNewProduct(
                    products.get(i).getProductdescription(),
                    products.get(i).getTenpercentoff(),
                    products.get(i).getSecondonehalf(),
                    products.get(i).getHref(),
                    products.get(i).getBigimgsrc(),
                    products.get(i).getNewprice(),
                    products.get(i).getCommentnum(),
                    products.get(i).getNewproduct()
            );
            bestSellerAndNewProducts.add(bestSellerAndNewProduct);
        }
        return bestSellerAndNewProducts;
    }


    //getRankingItems方法的工具方法
    private List<ItemRankingProduct> getRankingItemsUtil(List<String> list1, String std) {
        List<Product> products = productService.list(new QueryWrapper<Product>().select("productdescription","tenpercentoff"
                ,"secondonehalf","href","bigimgsrc","newprice","secondlevelcategoryrank","firstlevelcategoryrank")
                .eq(std, list1.get(0)));
        if(std.equals("firstlevelcategory")){
            products.sort(new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return o2.getFirstlevelcategoryrank() - o1.getFirstlevelcategoryrank();
                }
            });
        }else{
            products.sort(new Comparator<Product>() {
                @Override
                public int compare(Product o1, Product o2) {
                    return o2.getSecondlevelcategoryrank() - o1.getSecondlevelcategoryrank();
                }
            });
        }

        //把前七个放进去
        List<ItemRankingProduct> itemRankingProductList = new ArrayList<>();//存放一个请求属性的查询值
        for (int j = 0; j < products.size(); j++) {
            if (j >= 7) {
                break;
            }
            ItemRankingProduct itemRankingProduct = new ItemRankingProduct(
                    products.get(j).getProductdescription(),
                    (j + 1),
                    products.get(j).getTenpercentoff(),
                    products.get(j).getSecondonehalf(),
                    products.get(j).getHref(),
                    products.get(j).getBigimgsrc(),
                    products.get(j).getNewprice()
            );
            System.out.println(itemRankingProduct);
            itemRankingProductList.add(itemRankingProduct);
        }
//                System.out.println(products);
        System.out.println(itemRankingProductList);
        return itemRankingProductList;
    }
}
