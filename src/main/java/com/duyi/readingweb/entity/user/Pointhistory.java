package com.duyi.readingweb.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pointhistory {
    @TableId(type = IdType.AUTO)
    private Integer pointhistoryid;

    private Integer pointamount;

    private String pointdescription;

    private Date getdate;

    private String email;


}