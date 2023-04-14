package com.duyi.readingweb.bean.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MustBuyProduct {
    private String productDescription;
    private Boolean discount;
    private Boolean secondOneHalf;
    private String href;
    private String imgSrc;
    private Integer price;
    private Integer newProduct;
}
