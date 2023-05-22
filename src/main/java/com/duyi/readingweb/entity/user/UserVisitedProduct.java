package com.duyi.readingweb.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVisitedProduct {
    @TableId(value = "iduser_visited_product", type = IdType.AUTO)
    private Long iduserVisitedProduct;

    private String useremail;

    private Integer productid;

    private Integer count;


}