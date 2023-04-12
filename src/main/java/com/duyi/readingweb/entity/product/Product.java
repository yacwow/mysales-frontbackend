package com.duyi.readingweb.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer idproduct;

    private Boolean timeseller;

    private Boolean bestseller;

    private Integer newprice;

    private Integer marketprice;

    private Integer originprice;

    private Boolean tenpercentoff;

    private String productdescription;

    private Boolean newproduct;

    private Boolean secondonehalf;

    private String href;

    private String productsize;

    private String productdetailsize;

    private String productdetaildescription;

    private Integer stock;

    private Integer rank;

    private Integer soldnum;

    private Integer recommend;

    private Integer visitednum;

    private Date postedtime;

    private Integer truewishlistnum;

    private Integer fraudwishlistnum;

    private String firstlevelcategory;

    private String secondlevelcategory;

    private Integer mustbuyrate;

    private String bigimgsrc;
    private Integer commentnum;
    private Integer fraudcommentnum;
    private Integer fraudsoldnum;
}