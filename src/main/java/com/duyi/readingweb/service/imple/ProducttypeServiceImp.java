package com.duyi.readingweb.service.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.Producttype;
import com.duyi.readingweb.mapper.ProducttypeMapper;
import com.duyi.readingweb.service.ProducttypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProducttypeServiceImp extends ServiceImpl<ProducttypeMapper, Producttype>
implements ProducttypeService {

}
