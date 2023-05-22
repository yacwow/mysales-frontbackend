package com.duyi.readingweb.entity.adminUser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminProduct {
    @TableId(type = IdType.AUTO)
    private Integer adminProductid;

    private Integer productid;

    private String adminusername;

}