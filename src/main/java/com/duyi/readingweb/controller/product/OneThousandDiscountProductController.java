package com.duyi.readingweb.controller.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.duyi.readingweb.bean.ResultMsg;
import com.duyi.readingweb.bean.product.OneThousandDiscountProduct;
import com.duyi.readingweb.bean.product.SalesPageProduct;
import com.duyi.readingweb.bean.product.oneThousandDiscountData.OneThousandDiscountPicture;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.entity.product.ProductImg;
import com.duyi.readingweb.service.product.ProductImgService;
import com.duyi.readingweb.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class OneThousandDiscountProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImgService productImgService;

    @RequestMapping("/api/discount")
    public ResultMsg getProducts() {
        Random random = new Random();
        Integer a = random.nextInt(8, 15)*4;
        List<Product> products = productService.list(new QueryWrapper<Product>().select("idproduct", "href", "secondonehalf",
                "tenpercentoff", "newprice", "stock").gt("recommend", 1).last("limit 0" + a));
        List<OneThousandDiscountProduct> oneThousandDiscountProducts=new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            List<ProductImg> productImgList = productImgService.list(new QueryWrapper<ProductImg>()
                    .select("idProduct", "bigImgSrc", "smallImgSrc", "first").eq("idproduct", products.get(i).getIdproduct()));
            productImgList.sort(new Comparator<ProductImg>() {
                @Override
                public int compare(ProductImg o1, ProductImg o2) {
                    return o2.getFirst() - o1.getFirst();
                }
            });
            List<OneThousandDiscountPicture> oneThousandDiscountPictures=new ArrayList<>();
            for (int j = 0; j < productImgList.size(); j++) {
                if(j>=random.nextInt(2,5)) break;
                if (j == 0) {
                    oneThousandDiscountPictures.add(
                           new OneThousandDiscountPicture(productImgList.get(j).getBigimgsrc(),null) );
                } else {
                    oneThousandDiscountPictures.add(
                            new OneThousandDiscountPicture(null,productImgList.get(j).getSmallimgsrc()) );
                }
            }
            oneThousandDiscountProducts.add(new OneThousandDiscountProduct(
                    products.get(i).getNewprice(),
                    products.get(i).getTenpercentoff(),
                    products.get(i).getSecondonehalf(),
                    products.get(i).getHref(),
                    products.get(i).getStock(),
                    oneThousandDiscountPictures
            ));
        }
        return ResultMsg.ok().data("data",oneThousandDiscountProducts);
    }
}
