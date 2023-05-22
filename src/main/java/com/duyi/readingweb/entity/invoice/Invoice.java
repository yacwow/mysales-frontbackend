package com.duyi.readingweb.entity.invoice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Invoice {
    @TableId(type = IdType.AUTO)
    private Integer invoiceid;

    private Integer userid;

    private Date orderdate;

    private String paymentmethod;

    private String deliverymethod;

    private Integer originprice;

    private Integer normaldiscount;

    private Integer timelydiscount;

    private Integer secondhalfdiscount;

    private Integer usedpoint;

    private Integer priceafterdiscount;

    private Integer deliveryfee;

    private Integer paymentamount;

    private String paymentstatus;
    private String tips;


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

    private String cancelreason;

}