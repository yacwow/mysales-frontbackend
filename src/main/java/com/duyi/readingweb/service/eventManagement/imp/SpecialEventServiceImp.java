package com.duyi.readingweb.service.eventManagement.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.eventManagement.SpecialEvent;
import com.duyi.readingweb.mapper.eventManagement.SpecialEventMapper;
import com.duyi.readingweb.service.eventManagement.SpecialEventService;
import org.springframework.stereotype.Service;

@Service
public class SpecialEventServiceImp extends ServiceImpl<SpecialEventMapper, SpecialEvent>
implements SpecialEventService {
}
