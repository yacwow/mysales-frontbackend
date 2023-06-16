package com.duyi.readingweb.service.user.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.user.BadInvoice;
import com.duyi.readingweb.mapper.user.BadInvoiceMapper;
import com.duyi.readingweb.service.user.BadInvoiceService;
import org.springframework.stereotype.Service;

@Service
public class BadInvoiceServiceImp extends ServiceImpl<BadInvoiceMapper, BadInvoice>
implements BadInvoiceService {
}
