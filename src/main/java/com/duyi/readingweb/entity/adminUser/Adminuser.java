package com.duyi.readingweb.entity.adminUser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adminuser {
    @TableId(type = IdType.AUTO)
    private Integer adminuserid;

    private String adminusername;

    private String adminuserpassword;

    private String level;

    private Date lastlogin;
    private String adminname;

}