package com.duyi.readingweb.service.user.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.user.Pointhistory;
import com.duyi.readingweb.mapper.user.PointhistoryMapper;
import com.duyi.readingweb.service.user.PointhistoryService;
import org.springframework.stereotype.Service;

@Service
public class PointhistoryServiceImp extends ServiceImpl<PointhistoryMapper, Pointhistory> implements PointhistoryService {
}
