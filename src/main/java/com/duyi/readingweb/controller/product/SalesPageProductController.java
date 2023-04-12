package com.duyi.readingweb.controller.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.bean.product.SalesPageProduct;
import com.duyi.readingweb.bean.product.salesPageData.SalesPageBuyMatch;
import com.duyi.readingweb.bean.product.salesPageData.SalesPageComment;
import com.duyi.readingweb.bean.product.salesPageData.SalesPagePicture;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.entity.product.ProductImg;
import com.duyi.readingweb.entity.product.UserProductWishlist;
import com.duyi.readingweb.entity.user.Comment;
import com.duyi.readingweb.service.product.ProductImgService;
import com.duyi.readingweb.service.product.ProductService;
import com.duyi.readingweb.service.product.UserProductWishlistService;
import com.duyi.readingweb.service.user.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

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

    @RequestMapping(value = "/api/salesPageProduct/{productId}", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultMsg getSalesPageProduct(@PathVariable("productId") Integer productId, @RequestParam Map<String, Object> params, HttpServletRequest request) {
        System.out.println(params);
        //根据登录与否判断是否喜欢该商品
        Map<String,Object> wishList=new HashMap<>();
        if (params.isEmpty()) {
            wishList.put("isLogin",false);
        } else {
            String email = (String) params.get("userEmail");
            UserProductWishlist userProductWishlist = userProductWishlistService.getOne(new QueryWrapper<UserProductWishlist>().eq("useremail", email).eq("productid", productId));
            if(userProductWishlist!=null){
                wishList.put("userLove",true);
            }
            wishList.put("isLogin",true);
            wishList.put("userLove",false);
        }
        Product product = productService.getOne(new QueryWrapper<Product>().eq("idProduct", productId));
        List<ProductImg> productImgList = productImgService.list(new QueryWrapper<ProductImg>().eq("idProduct", productId));
        //变成salespagepicture
        List<SalesPagePicture> salesPagePictureList = new ArrayList<>();
        for (int i = 0; i < productImgList.size(); i++) {
            salesPagePictureList.add(new SalesPagePicture(productImgList.get(i).getBigimgsrc(),
                    productImgList.get(i).getSmallimgsrc(),
                    productImgList.get(i).getColor(), productImgList.get(i).getFirst()));
        }
        List<Comment> commentList = commentService.list(new QueryWrapper<Comment>().eq("productid_comment", productId));
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
                .eq("secondlevelcategory", product.getSecondlevelcategory()));
        Collections.shuffle(productsWithSameCategory);
        List<SalesPageBuyMatch> salesPageBuyMatchList = new ArrayList<>();
        for (int i = 0; i < productsWithSameCategory.size(); i++) {
            if (i >= 14) break;
            List<ProductImg> productImgList1 = productImgService.list(new QueryWrapper<ProductImg>().eq("idproduct", productsWithSameCategory.get(i).getIdproduct()));
            salesPageBuyMatchList.add(new SalesPageBuyMatch(
                    productsWithSameCategory.get(i).getTenpercentoff(),
                    productsWithSameCategory.get(i).getSecondonehalf(),
                    productsWithSameCategory.get(i).getHref(),
                    productImgList1.get(0).getBigimgsrc(),
                    productsWithSameCategory.get(i).getNewprice(),
                    productsWithSameCategory.get(i).getMarketprice()

            ));
        }
        SalesPageProduct salesPageProduct = new SalesPageProduct(
                product.getIdproduct(),
                product.getProductdetailsize(),
                product.getProductdetaildescription(),
                product.getNewprice(),
                product.getMarketprice(),
                product.getOriginprice(),
                product.getTenpercentoff(),
                product.getSecondonehalf(),
                product.getProductdescription(),
                product.getHref(),
                product.getTimeseller(),
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
        List<Product> productList = productService.list(new QueryWrapper<Product>().select("href", "productdescription", "newprice", "idproduct", "bigimgsrc"));
        Collections.shuffle(productList);
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


}
