package com.duyi.readingweb.service.couponlist.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.couponlist.Couponlist;
import com.duyi.readingweb.mapper.couponlist.CouponlistMapper;
import com.duyi.readingweb.service.couponlist.CouponlistService;
import org.springframework.stereotype.Service;

@Service
public class CouponlistServiceImp extends ServiceImpl<CouponlistMapper, Couponlist>
implements CouponlistService {
}
