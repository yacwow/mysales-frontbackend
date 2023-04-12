package com.duyi.readingweb.entity.couponlist;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Couponlist {
    @TableId
    private Integer idcouponlist;

    private Integer discountamount;

    private String codenumber;

    private Integer applyamount;
    private Date startdate;

    private Date expiredate;

    private Integer discountpercent;

    private Integer whocanapply;
    private String coupontype;

}