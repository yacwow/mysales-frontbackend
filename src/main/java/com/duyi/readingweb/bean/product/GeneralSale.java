package com.duyi.readingweb.bean.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GeneralSale {
    private Integer discount;
    private Integer secondOneHalf;
    private String href;
    private String imgSrc;
    private Integer price;
    private Integer recommend;
    private Integer commentNum;
    private String productDescription;
    private Integer newProduct;
    private Integer idProduct;
}
