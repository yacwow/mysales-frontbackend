package com.duyi.readingweb.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Product {
    @TableId(type = IdType.AUTO)
    private Integer idproduct;

    private Integer newprice;

    private Integer marketprice;

    private Integer originprice;

    private Integer tenpercentoff;

    private String productdescription;

    private Integer secondonehalf;

    private String href;
    private String productdetailsize;
    private String productdetaildescription;
    private Integer stock;
    private Integer soldnum;
    private Integer visitednum;
    private Date postedtime;
    private Integer truewishlistnum;
    private Integer fraudwishlistnum;
    private String firstlevelcategory;
    private Integer firstlevelcategoryrank;
    private String secondlevelcategory;
    private Integer secondlevelcategoryrank;
    private String bigimgsrc;
    private Integer commentnum;
    private Integer fraudcommentnum;
    private Integer fraudsoldnum;
    private Integer purchaseprice;
    private String stockstatus;
    private String editstatus;
    private Double productweight;
    private String insidedescription;
    private String author;
    private Date updatetime;
}