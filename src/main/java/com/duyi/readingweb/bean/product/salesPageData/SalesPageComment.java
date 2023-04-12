package com.duyi.readingweb.bean.product.salesPageData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesPageComment {
    private String name;

    private String commentDetail;

    private Date updatedTime;

    private String imgSrc;

    private Integer rateStar;
}
