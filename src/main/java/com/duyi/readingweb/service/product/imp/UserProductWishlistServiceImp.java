package com.duyi.readingweb.service.product.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.product.UserProductWishlist;
import com.duyi.readingweb.mapper.product.UserProductWishlistMapper;
import com.duyi.readingweb.service.product.UserProductWishlistService;
import org.springframework.stereotype.Service;

@Service
public class UserProductWishlistServiceImp extends ServiceImpl<UserProductWishlistMapper, UserProductWishlist>
implements UserProductWishlistService {
}
