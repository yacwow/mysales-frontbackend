package com.duyi.readingweb.entity.invoice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class InvoiceAddress {
    @TableId(type= IdType.AUTO)
    private Integer iduserAddress;

    private String email;
    private String updateemail;

    private String detailaddress;
    private String firstname;
    private String lastname;
    private String mobilephone;
    private String country;

    private String province;

    private String city;

    private String area;

    private Integer postcode;


}