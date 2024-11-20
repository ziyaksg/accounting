package com.uydev.services.impl;

import com.uydev.dto.InvoiceProductDTO;
import com.uydev.entity.InvoiceProduct;
import com.uydev.enums.InvoiceType;
import com.uydev.mapper.MapperUtil;
import com.uydev.repository.InvoiceProductRepository;
import com.uydev.services.InvoiceProductService;
import com.uydev.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceProductServiceImpl implements InvoiceProductService {
    private final InvoiceProductRepository invoiceProductRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;


    @Override
    public void save(InvoiceProductDTO invoiceProductDTO) {
        invoiceProductRepository.save(mapperUtil.convert(invoiceProductDTO,new InvoiceProduct()));
    }

    @Override
    public List<InvoiceProductDTO> getAllInvoiceProducts(Long invoiceId) {
        Long companyId = securityService.getLoggedInUser().getCompany().getId();
        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findAllInvoiceProductByCompanyIdAndInvoiceType
                (companyId, InvoiceType.PURCHASE, false);
        return invoiceProducts.stream().map(ip->mapperUtil.convert(ip,new InvoiceProductDTO())).collect(Collectors.toList());
    }
}
