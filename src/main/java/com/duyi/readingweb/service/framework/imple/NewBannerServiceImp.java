package com.duyi.readingweb.service.framework.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.framework.Newbanner;
import com.duyi.readingweb.mapper.framework.NewBannerMapper;
import com.duyi.readingweb.service.framework.NewBannerService;
import org.springframework.stereotype.Service;

@Service
public class NewBannerServiceImp extends ServiceImpl<NewBannerMapper, Newbanner>
implements NewBannerService {
}
