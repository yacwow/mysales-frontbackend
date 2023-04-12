package com.duyi.readingweb.entity.invoice;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class InvoiceProductProductdetail {
    @TableId(type= IdType.AUTO)
    private Integer productdetailid;

    private Integer invoiceidProductdetail;

    private Integer productidProductdetail;

    private Integer amount;

    private String productcolor;

    private String productsize;

}