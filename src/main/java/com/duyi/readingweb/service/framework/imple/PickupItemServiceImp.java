package com.duyi.readingweb.service.framework.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.framework.Pickupitem;
import com.duyi.readingweb.service.framework.PickupItemService;
import com.duyi.readingweb.mapper.framework.PickupItemMapper;
import org.springframework.stereotype.Service;

@Service
public class PickupItemServiceImp extends ServiceImpl<PickupItemMapper, Pickupitem>
    implements PickupItemService {
}
