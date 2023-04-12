package com.duyi.readingweb.entity.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProductCartdetail {
    @TableId (type = IdType.AUTO)
    private Integer cartdetailid;

    private String useremail;

    private Integer productidCart;

    private Integer productamount;

    private String productcolor;

    private String productsize;


}