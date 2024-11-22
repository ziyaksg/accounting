package com.uydev.services;

import com.uydev.dto.InvoiceProductDTO;
import com.uydev.entity.InvoiceProduct;

import java.util.List;

public interface InvoiceProductService {
    void save(InvoiceProductDTO invoiceProductDTO);

    List<InvoiceProductDTO> getAllInvoiceProducts(Long invoiceId);

    void deleteById(Long id);
}
