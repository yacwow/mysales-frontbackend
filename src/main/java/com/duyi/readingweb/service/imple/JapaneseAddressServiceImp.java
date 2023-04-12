package com.duyi.readingweb.service.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.JapaneseAddress;
import com.duyi.readingweb.mapper.JapaneseAddressMapper;
import com.duyi.readingweb.service.JapaneseAddressService;
import org.springframework.stereotype.Service;

@Service
public class JapaneseAddressServiceImp extends ServiceImpl<JapaneseAddressMapper, JapaneseAddress>
implements JapaneseAddressService {
}
