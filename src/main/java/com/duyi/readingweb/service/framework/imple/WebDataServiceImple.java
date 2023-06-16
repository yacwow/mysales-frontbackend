package com.duyi.readingweb.service.framework.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.framework.Webdata;
import com.duyi.readingweb.mapper.framework.WebdataMapper;
import com.duyi.readingweb.service.framework.WebDataService;
import org.springframework.stereotype.Service;

@Service
public class WebDataServiceImple extends ServiceImpl<WebdataMapper, Webdata>
implements WebDataService {
}
