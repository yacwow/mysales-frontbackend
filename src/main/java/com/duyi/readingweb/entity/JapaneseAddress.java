package com.duyi.readingweb.entity;

import lombok.Data;

@Data
public class JapaneseAddress {
    private Integer id;

    private Integer pid;

    private Integer postcode;

    private String province;

    private String city;

    private String area;


}