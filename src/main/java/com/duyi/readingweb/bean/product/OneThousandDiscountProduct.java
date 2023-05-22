package com.duyi.readingweb.bean.product;

import com.duyi.readingweb.bean.product.oneThousandDiscountData.OneThousandDiscountPicture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneThousandDiscountProduct {
    private Integer newprice;
    private Integer discount;

    private Integer secondonehalf;
    private String href;
    private Integer stockNum;
    private List<OneThousandDiscountPicture> salesPagePictures;
}
