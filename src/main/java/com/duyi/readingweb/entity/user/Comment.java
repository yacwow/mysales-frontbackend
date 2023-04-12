package com.duyi.readingweb.entity.user;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @TableId()
    private Integer commentid;

    private Integer productidComment;

    private String name;

    private String commentdetail;

    private Date updatedtime;

    private String imgsrc;

    private Integer ratestar;


}