package com.duyi.readingweb.service.product.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.product.ProductImg;
import com.duyi.readingweb.mapper.product.ProductImgMapper;
import com.duyi.readingweb.service.product.ProductImgService;
import org.springframework.stereotype.Service;

@Service
public class ProductImgServiceImp extends ServiceImpl<ProductImgMapper, ProductImg>
implements ProductImgService {
}
