package com.uydev.services;

import com.uydev.dto.InvoiceProductDTO;

import java.util.List;

public interface InvoiceProductService {
    void save(InvoiceProductDTO invoiceProductDTO);

    List<InvoiceProductDTO> getAllPurchaseInvoiceProducts(Long invoiceId);

    void deleteById(Long id);

    List<InvoiceProductDTO> getAllSalesInvoiceProducts(Long invoiceId);
}
