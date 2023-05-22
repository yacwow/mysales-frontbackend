package com.duyi.readingweb.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryManagement {
    @TableId(type = IdType.AUTO)
    private Integer categorymanagementid;

    private String title;

    private String href;

    private String firstlevelcategory;

    private String secondlevelcategory;

    private String author;

    private Date updatedtime;
    private Integer countnum;
    private String categoryimgurl;

}