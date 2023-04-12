package com.duyi.readingweb.service.product.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.product.UserProductCartdetail;
import com.duyi.readingweb.mapper.product.UserProductCartdetailMapper;
import com.duyi.readingweb.service.product.UserProductCartdetailService;
import org.springframework.stereotype.Service;

@Service
public class UserProductCartdetailServiceImp extends ServiceImpl<UserProductCartdetailMapper, UserProductCartdetail>
implements UserProductCartdetailService {
}
