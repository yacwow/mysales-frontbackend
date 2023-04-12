package com.duyi.readingweb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Producttype {
    @TableId(type = IdType.AUTO)
    private Integer producttypeid;

    private String firstleveltype;

    private String firstlevelname;

    private String secondleveltype;

    private String secondlevelname;

    private Byte hot;


}