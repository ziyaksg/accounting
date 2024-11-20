package com.uydev.services.impl;

import com.uydev.dto.*;
import com.uydev.entity.Invoice;
import com.uydev.entity.Product;
import com.uydev.enums.InvoiceStatus;
import com.uydev.enums.InvoiceType;
import com.uydev.mapper.MapperUtil;
import com.uydev.repository.InvoiceRepository;
import com.uydev.services.ClientVendorService;
import com.uydev.services.InvoiceProductService;
import com.uydev.services.InvoiceService;
import com.uydev.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final SecurityService securityService;
    private final MapperUtil mapperUtil;
    private final ClientVendorService clientVendorService;
    private final InvoiceProductService invoiceProductService;
    @Override
    public List<InvoiceDTO> getAllPurchaseInvoice() {
        Long companyId=securityService.getLoggedInUser().getCompany().getId();
        return invoiceRepository.findAllByCompanyIdAndInvoiceTypeAndIsDeleted
                (companyId, InvoiceType.PURCHASE,false).stream()
                .map(invoice->mapperUtil.convert(invoice,new InvoiceDTO())).collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO createNewPurchaseInvoice() {
        CompanyDTO companyDTO =securityService.getLoggedInUser().getCompany();
        InvoiceDTO invoiceDTO=new InvoiceDTO();
        invoiceDTO.setInvoiceType(InvoiceType.PURCHASE);
        invoiceDTO.setCompany(companyDTO);
        invoiceDTO.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        invoiceDTO.setDate(LocalDate.now());

        invoiceDTO.setInvoiceNo(
                getNextInvoiceNo(companyDTO.getId(), InvoiceType.PURCHASE)
        );
        return invoiceDTO;
    }

    private String getNextInvoiceNo(Long companyId, InvoiceType invoiceType) {

        Integer lastInvoiceNo=invoiceRepository.countAllByCompanyIdAndInvoiceType(companyId,invoiceType);
       if (invoiceType.equals(InvoiceType.PURCHASE)){

          return "P-"+String.format("%03d", lastInvoiceNo+1);
       }else return "S-"+String.format("%03d", lastInvoiceNo+1);
    }

    @Override
    public List<ClientVendorDTO> getAllVendors() {
        return clientVendorService.getAllVendors();

    }

    @Override
    public InvoiceDTO save(InvoiceDTO invoiceDTO) {
        invoiceDTO.setCompany(securityService.getLoggedInUser().getCompany());
        invoiceDTO.setInvoiceType(InvoiceType.PURCHASE);
        invoiceDTO.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        return mapperUtil.convert(invoiceRepository
                .save(mapperUtil.convert(invoiceDTO,new Invoice())),new InvoiceDTO());
    }

    @Override
    public InvoiceDTO findById(Long id) {
        Invoice invoice=invoiceRepository.findById(id).orElseThrow(()->new RuntimeException("there is no invoice with "+id));
        return mapperUtil.convert(invoice,new InvoiceDTO());
    }

    @Override
    public void addProduct(InvoiceProductDTO invoiceProductDTO, Long invoiceId) {
        Invoice invoice=invoiceRepository.findById(invoiceId).orElseThrow(()->new RuntimeException("there is no invoice with "+invoiceId));
        invoiceProductDTO.setInvoice(mapperUtil.convert(invoice,new InvoiceDTO()));
        invoiceProductService.save(invoiceProductDTO);
    }
}
