package com.duyi.readingweb.entity.framework;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;

@Data
public class Pickupitem {
    @TableId(type = IdType.AUTO)
    private Integer pickupitemid;

    private String href;

    private String imgsrc;

    private Date expiretime;

    private String intro;
    private Integer rank;

}