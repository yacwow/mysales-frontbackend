package com.duyi.readingweb.service.invoice.imple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.duyi.readingweb.entity.invoice.InvoiceAddress;
import com.duyi.readingweb.mapper.invoice.InvoiceAddressMapper;
import com.duyi.readingweb.service.invoice.InvoiceAddressService;
import org.springframework.stereotype.Service;

@Service
public class InvoiceAddressServiceImp extends ServiceImpl<InvoiceAddressMapper, InvoiceAddress>
implements InvoiceAddressService {
}
