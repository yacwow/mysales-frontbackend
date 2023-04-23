package com.duyi.readingweb.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer idproduct;

    private Integer timeseller;
    private Integer bestseller;
    private Integer newprice;

    private Integer marketprice;

    private Integer originprice;

    private Boolean tenpercentoff;

    private String productdescription;

    private Integer newproduct;
    private Boolean secondonehalf;

    private String href;

    private String productsize;

    private String productdetailsize;

    private String productdetaildescription;

    private Integer stock;


    private Integer soldnum;

    private Integer recommend;

    private Integer visitednum;

    private Date postedtime;

    private Integer truewishlistnum;

    private Integer fraudwishlistnum;

    private String firstlevelcategory;
    private Integer firstlevelcategoryrank;
    private String secondlevelcategory;
    private Integer secondlevelcategoryrank;
//    private Integer mustbuyrate;

    private String bigimgsrc;
    private Integer commentnum;
    private Integer fraudcommentnum;
    private Integer fraudsoldnum;
    private Integer purchaseprice;
    private String stockstatus;
    private String editstatus;
}