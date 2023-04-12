package com.duyi.readingweb.bean.product.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetail {

    private String detailAddress;

    private String country;

    private String province;

    private String city;

    private String area;

    private Integer postcode;
    private String mobilePhone;
    private String firstName;
    private String lastName;
    private String email;//这个是invoice特有的email，存入的时候因为是验证了，所以有user自己的email

    private Integer invoiceId;
}
