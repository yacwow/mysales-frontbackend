package com.duyi.readingweb.service.product.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.product.Product;
import com.duyi.readingweb.mapper.product.ProductMapper;
import com.duyi.readingweb.service.product.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImp extends ServiceImpl<ProductMapper, Product>
implements ProductService {
}
