package com.duyi.readingweb.service.eventManagement.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.eventManagement.SpecialEventDetail;
import com.duyi.readingweb.mapper.eventManagement.SpecialEventDetailMapper;
import com.duyi.readingweb.service.eventManagement.SpecialEventDetailService;
import org.springframework.stereotype.Service;

@Service
public class SpecialEventDetailServiceImp extends ServiceImpl<SpecialEventDetailMapper, SpecialEventDetail>
implements SpecialEventDetailService {
}
