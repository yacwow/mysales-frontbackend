package com.duyi.readingweb.bean.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartListProduct {



    private Integer amount;
    private String color;
    private String size;
    private Integer productId;
    private String href;
    private String imgSrc;
    private Integer price;

    private Boolean secondOneHalf;

    private String productDescription;

    private Boolean timesale;


}
