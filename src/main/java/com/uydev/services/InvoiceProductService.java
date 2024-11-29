package com.uydev.services;

import com.uydev.dto.InvoiceProductDTO;
import com.uydev.entity.InvoiceProduct;
import com.uydev.enums.InvoiceType;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceProductService {
    void save(InvoiceProductDTO invoiceProductDTO);

    List<InvoiceProductDTO> getAllPurchaseInvoiceProducts(Long invoiceId);

    void deleteById(Long id);

    List<InvoiceProductDTO> getAllSalesInvoiceProducts(Long invoiceId);

    BigDecimal getProfitLossByMonth(int year, int monthValue, Long id, InvoiceType invoiceType);

    List<InvoiceProductDTO> getAllPurchaseInvoiceProductsByProductId(Long id);
}
