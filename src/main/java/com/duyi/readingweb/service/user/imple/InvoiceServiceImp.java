package com.duyi.readingweb.service.user.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.user.Invoice;
import com.duyi.readingweb.mapper.user.InvoiceMapper;
import com.duyi.readingweb.service.user.InvoiceService;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImp extends ServiceImpl<InvoiceMapper, Invoice>
implements InvoiceService {
}
