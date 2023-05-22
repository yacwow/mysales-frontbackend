package com.duyi.readingweb.bean.product;

import com.duyi.readingweb.bean.product.salesPageData.SalesPageBuyMatch;
import com.duyi.readingweb.bean.product.salesPageData.SalesPageComment;
import com.duyi.readingweb.bean.product.salesPageData.SalesPagePicture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesPageProductMobile {
    private Integer productId;
    private String productDetailSize;
    private String productdetaildescription;
    private Integer newprice;
    private Integer marketprice;
    private Integer originprice;
    private Integer discount;
    private Integer secondonehalf;
    private String productdescription;
    private String href;
    private Integer soldNum;
    private Integer wishListNum;
    private Integer timesale;
    private Map<String,Object> wishList;
    private String firstlevelcategory;
    private String secondlevelcategory;
    private List<SalesPageComment> salesPageComments;
    private List<SalesPagePicture> salesPagePictures;
    private List<Map<String,Object>> salesPageBuyMatches;
}
