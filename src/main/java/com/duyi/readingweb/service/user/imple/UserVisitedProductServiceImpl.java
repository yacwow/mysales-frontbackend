package com.duyi.readingweb.service.user.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.user.UserVisitedProduct;
import com.duyi.readingweb.mapper.user.UserVisitedProductMapper;
import com.duyi.readingweb.service.user.UserVisitedProductService;
import org.springframework.stereotype.Service;

@Service
public class UserVisitedProductServiceImpl extends ServiceImpl<UserVisitedProductMapper, UserVisitedProduct>
implements UserVisitedProductService {
}
