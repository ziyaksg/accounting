package com.uydev.services.impl;

import com.uydev.dto.*;
import com.uydev.entity.Invoice;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        Long companyId = securityService.getLoggedInUser().getCompany().getId();
        return invoiceRepository.findAllByCompanyIdAndInvoiceTypeAndIsDeleted
                        (companyId, InvoiceType.PURCHASE, false).stream()
                .map(invoice -> mapperUtil.convert(invoice, new InvoiceDTO())).collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO createNewPurchaseInvoice() {
        CompanyDTO companyDTO = securityService.getLoggedInUser().getCompany();
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setDate(LocalDate.now());

        invoiceDTO.setInvoiceNo(
                getNextInvoiceNo(companyDTO.getId(), InvoiceType.PURCHASE)
        );
        return invoiceDTO;
    }

    private String getNextInvoiceNo(Long companyId, InvoiceType invoiceType) {

        Integer lastInvoiceNo = invoiceRepository.countAllByCompanyIdAndInvoiceType(companyId, invoiceType);
        if (invoiceType.equals(InvoiceType.PURCHASE)) {

            return "P-" + String.format("%03d", lastInvoiceNo + 1);
        } else return "S-" + String.format("%03d", lastInvoiceNo + 1);
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
                .save(mapperUtil.convert(invoiceDTO, new Invoice())), new InvoiceDTO());
    }

    @Override
    public InvoiceDTO findById(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException("there is no invoice with " + id));
        InvoiceDTO invoiceDto = mapperUtil.convert(invoice, new InvoiceDTO());
        List<InvoiceProductDTO> invoiceProducts = invoiceProductService.getAllInvoiceProducts(id);
        invoiceDto.setTax(calculateTax(invoiceProducts));
        invoiceDto.setPrice(calculateSubTotal(invoiceProducts));    //subtotal;
        invoiceDto.setTotal(invoiceDto.getPrice().add(invoiceDto.getTax()));//GRAND TOTAL
        return invoiceDto;
    }



    private BigDecimal calculateSubTotal(List<InvoiceProductDTO> invoiceProducts) {
        return invoiceProducts.stream()
                .map(ip ->
                        ip.getPrice().multiply(BigDecimal.valueOf(ip.getQuantity())) //250*5=1250
                ).reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    private BigDecimal calculateTax(List<InvoiceProductDTO> invoiceProducts) {
        return invoiceProducts.stream()
                .map(ip -> calculateProductTax(ip)
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculateProductTax(InvoiceProductDTO ip) {
        BigDecimal price = ip.getPrice();
        Integer taxRate = ip.getTax();
        Integer quantity = ip.getQuantity();
        BigDecimal tax = (price.multiply(BigDecimal.valueOf(((long) taxRate * quantity)))).divide(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
        return tax;

    }

    @Override
    public void addProduct(InvoiceProductDTO invoiceProductDTO, Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new RuntimeException("there is no invoice with " + invoiceId));
        invoiceProductDTO.setInvoice(mapperUtil.convert(invoice, new InvoiceDTO()));
        invoiceProductService.save(invoiceProductDTO);
    }

    @Override
    public void delete(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow((() -> new RuntimeException("there is no invoice with " + id)));
        invoice.setIsDeleted(true);
        invoiceRepository.save(invoice);
    }

    @Override
    public void removeProduct(Long invoiceId, Long invoiceProductId) {
        List<InvoiceProductDTO> invoiceProductList = invoiceProductService.getAllInvoiceProducts(invoiceId);
        for (InvoiceProductDTO invoiceProduct : invoiceProductList) {
            if (invoiceProduct.getId().equals(invoiceProductId)) {
                invoiceProductService.deleteById(invoiceProduct.getId());
            }
        }
    }
}
