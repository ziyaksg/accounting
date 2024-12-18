package com.uydev.services.impl;

import com.uydev.dto.InvoiceProductDTO;
import com.uydev.entity.InvoiceProduct;
import com.uydev.enums.InvoiceType;
import com.uydev.mapper.MapperUtil;
import com.uydev.repository.InvoiceProductRepository;
import com.uydev.services.InvoiceProductService;
import com.uydev.services.InvoiceService;
import com.uydev.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceProductServiceImpl implements InvoiceProductService {
    private final InvoiceProductRepository invoiceProductRepository;
    private final MapperUtil mapperUtil;
    private final SecurityService securityService;
    private final InvoiceService invoiceService;

    public InvoiceProductServiceImpl(InvoiceProductRepository invoiceProductRepository, MapperUtil mapperUtil, SecurityService securityService, @Lazy InvoiceService invoiceService) {
        this.invoiceProductRepository = invoiceProductRepository;
        this.mapperUtil = mapperUtil;
        this.securityService = securityService;
        this.invoiceService = invoiceService;
    }

    @Override
    public void save(InvoiceProductDTO invoiceProductDTO) {
        invoiceProductRepository.save(mapperUtil.convert(invoiceProductDTO, new InvoiceProduct()));
    }

    @Override
    public List<InvoiceProductDTO> getAllPurchaseInvoiceProducts(Long invoiceId) {
        return getInvoiceProductsByType(InvoiceType.PURCHASE, invoiceId);
    }

    @Override
    public List<InvoiceProductDTO> getAllSalesInvoiceProducts(Long invoiceId) {
        return getInvoiceProductsByType(InvoiceType.SALES, invoiceId);
    }

    @Override
    public BigDecimal getProfitLossByMonth(int year, int monthValue, Long id, InvoiceType invoiceType) {
        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.getTotalProfitLossByMonthForCompany(year, monthValue, id, invoiceType);
        return  invoiceProducts.stream()
            .map(InvoiceProduct::getProfitLoss)
            .reduce(BigDecimal.ZERO, BigDecimal::add);


    }

    private List<InvoiceProductDTO> getInvoiceProductsByType(InvoiceType invoiceType, Long invoiceId) {
        Long companyId = securityService.getLoggedInUser().getCompany().getId();
        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findAllInvoiceProductByCompanyIdAndInvoiceType
                (companyId, invoiceType, false, invoiceId);
        List<InvoiceProductDTO> invoiceProductList = invoiceProducts.stream().map(ip -> mapperUtil.convert(ip, new InvoiceProductDTO())).collect(Collectors.toList());
        return calculateProductTotal(invoiceProductList);
    }

    private List<InvoiceProductDTO> calculateProductTotal(List<InvoiceProductDTO> invoiceProducts) {

        return invoiceProducts.stream()
                .map(ip ->
                        {
                            BigDecimal productTax = invoiceService.calculateProductTax(ip);
                            BigDecimal totalPrice = ip.getPrice().multiply(BigDecimal.valueOf(ip.getQuantity()));
                            ip.setTotal(productTax.add(totalPrice));
                            return ip;
                        }

                ).collect(Collectors.toList());

    }

    @Override
    public void deleteById(Long id) {
        InvoiceProduct invoiceProduct = invoiceProductRepository.findById(id).orElseThrow(() -> new RuntimeException("there is no invoice product with id " + id));
        invoiceProduct.setIsDeleted(true);
        invoiceProductRepository.save(invoiceProduct);
    }

    @Override
    public List<InvoiceProductDTO> getAllPurchaseInvoiceProductsByProductId(Long productId) {
        return  getAllInvoiceProductsByProductIdAndInvoiceType(productId, InvoiceType.PURCHASE);
    }

    @Override
    public List<InvoiceProductDTO> getAllSalesInvoiceProductsByProductId(Long productId) {
        return  getAllInvoiceProductsByProductIdAndInvoiceType(productId, InvoiceType.SALES);
    }

    private List<InvoiceProductDTO> getAllInvoiceProductsByProductIdAndInvoiceType(Long productId, InvoiceType invoiceType){
        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.getInvoiceProductByProduct_IdAndInvoice_InvoiceTypeOrderByInsertDateTimeAsc(productId,invoiceType);
        return invoiceProducts.stream()
                .map(ip->mapperUtil.convert(ip,new InvoiceProductDTO()))
                .toList();
    }

    @Override
    public boolean hasInvoiceProduct(Long invoiceId) {
       return invoiceProductRepository.existsByInvoice_IdAndIsDeleted(invoiceId,false);
    }

    @Override
    public BigDecimal calculateTotal(Long invoiceId) {
        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findAllByInvoice_IdAndIsDeleted(invoiceId,false);
        return invoiceProducts.stream()
                .map(ip-> ip.getPrice().multiply(BigDecimal.valueOf(ip.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
    }

    @Override
    public List<InvoiceProductDTO> getAllInvoiceProducts() {
        Long companyId = securityService.getLoggedInUser().getCompany().getId();
        List<InvoiceProduct> invoiceProducts = invoiceProductRepository.findInvoiceProductByInvoice_Company_IdAndIsDeleted(companyId,false);
        return invoiceProducts.stream()
                .map(ip->mapperUtil.convert(ip,new InvoiceProductDTO()))
                .toList();
    }
}
