package com.duyi.readingweb.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProductWishlist {
    @TableId(type = IdType.AUTO)
    private Integer wishlistid;

    private String useremail;

    private Integer productid;

}