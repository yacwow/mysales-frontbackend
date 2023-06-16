package com.duyi.readingweb.entity.framework;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Webdata {
    @TableId(type = IdType.AUTO)
    private Integer idwebdatacontroller;

    private String weburl;

    private Byte discount;

    private Byte description;

    private Byte secondonehalf;

    private String searchparams;
    private Date expiretime;
    private String imgsrc;
    private Integer rank;
}