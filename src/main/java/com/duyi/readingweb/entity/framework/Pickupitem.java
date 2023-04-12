package com.duyi.readingweb.entity.framework;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Pickupitem {
    @TableId
    private Integer pickupItemId;

    private String href;

    private String imgSrc;

    private Long expireTime;

    private String intro;
    private Integer rank;

}