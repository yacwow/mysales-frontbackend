package com.duyi.readingweb.bean.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BestSellerAndNewProduct {
    private String productDescription;
    private Integer discount;
    private Integer secondOneHalf;
    private String href;
    private String imgSrc;
    private Integer price;
    private Integer commentNumber;
    private Integer newProduct;

}
