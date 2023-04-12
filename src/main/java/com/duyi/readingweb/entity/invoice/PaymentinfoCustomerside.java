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
public class PaymentinfoCustomerside {
    @TableId(type = IdType.AUTO)
    private Integer idpaymentinfoCustomerside;

    private Integer countnum;

    private Integer discount;

    private Integer getpoint;

    private Integer paymentamount;

    private Integer secondhalfdiscount;

    private Integer timelydiscount;

    private Integer total;

    private Integer usedpoint;

    private Integer deliveryamount;

    private String useremail;
    private Date expiredate;
}