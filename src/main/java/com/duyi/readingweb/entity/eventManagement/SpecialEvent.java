package com.duyi.readingweb.entity.eventManagement;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SpecialEvent {
    @TableId(type = IdType.AUTO)
    private Integer specialEventid;

    private String specialcode;

    private String description;

    private String promotioncode;

    private Integer count;
    private Boolean active;
    private Date createtime;
    private Date updatetime;
private String imgurl;
}