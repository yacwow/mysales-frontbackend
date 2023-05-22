package com.duyi.readingweb.bean.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeSeller {
    private Integer secondOneHalf;
    private String href;
    private String imgSrc;
    private Integer marketPrice;
    private Integer price;
    private Integer stockNum;
    private Integer recommend;
}
