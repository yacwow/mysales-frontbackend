package com.duyi.readingweb.entity.user;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private static final long serialVersionUID = -1L;
//    private String uname;
    @TableId(type= IdType.AUTO)
    private Integer userid;

    private String upassword;

    private String firstname;

    private String lastname;

    private String email;

    private Date lastlogin;

    private String birthday;

    private Integer phone;

    private Integer mobilephone;

    private Integer deduction;

    @TableField(fill = FieldFill.UPDATE)
    private String token;
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime tokencreationdate;

    private Boolean advertise;
    private String level;
}
