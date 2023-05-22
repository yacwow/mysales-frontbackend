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
public class SpecialEventDetail {
    @TableId(type = IdType.AUTO)
    private Integer specialEventDetailid;

    private String specialcode;

    private Integer productid;

    private Integer categoryranknum;
    private Date updatetime;
}