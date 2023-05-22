package com.duyi.readingweb.bean.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LuckyBag {
    private String href;
    private String imgSrc;
    private Integer price;
    private String title;
    private Integer productId;
    private Integer secondHalf;
    private String size;
    private String color;
    private Integer amount;
}
