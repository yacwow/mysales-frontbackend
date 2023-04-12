package com.duyi.readingweb.bean.product.salesPageData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesPagePicture {
    private String bigimgsrc;

    private String smallimgsrc;

    private String color;
    private Integer first;
}
