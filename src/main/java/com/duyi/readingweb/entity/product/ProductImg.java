package com.duyi.readingweb.entity.product;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImg {
    @TableId
    private Integer idproductimg;

    private Integer idproduct;

    private String bigimgsrc;

    private String smallimgsrc;

    private String color;

    private Integer first;

}