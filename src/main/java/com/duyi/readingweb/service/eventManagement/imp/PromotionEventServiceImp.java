package com.duyi.readingweb.service.eventManagement.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.eventManagement.PromotionEvent;
import com.duyi.readingweb.mapper.eventManagement.PromotionEventMapper;
import com.duyi.readingweb.service.eventManagement.PromotionEventService;
import org.springframework.stereotype.Service;

@Service
public class PromotionEventServiceImp extends ServiceImpl<PromotionEventMapper, PromotionEvent>
implements PromotionEventService {
}
