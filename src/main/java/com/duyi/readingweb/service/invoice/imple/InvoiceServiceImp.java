package com.duyi.readingweb.service.invoice.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.invoice.Invoice;
import com.duyi.readingweb.mapper.invoice.InvoiceMapper;
import com.duyi.readingweb.service.invoice.InvoiceService;
import org.springframework.stereotype.Service;

@Service
public class InvoiceServiceImp extends ServiceImpl<InvoiceMapper, Invoice>
implements InvoiceService {
}
