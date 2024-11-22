package com.uydev.services;

import com.uydev.dto.ClientVendorDTO;
import com.uydev.dto.InvoiceDTO;
import com.uydev.dto.InvoiceProductDTO;
import com.uydev.dto.ProductDTO;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceService {
    List<InvoiceDTO> getAllPurchaseInvoice();
    InvoiceDTO createNewPurchaseInvoice();
    List<ClientVendorDTO> getAllVendors();

    InvoiceDTO save(InvoiceDTO invoiceDTO);

    InvoiceDTO findById(Long id);

    void addProduct(InvoiceProductDTO invoiceProductDTO, Long invoiceId);

    void delete(Long id);

    void removeProduct(Long invoiceId, Long productId);

    BigDecimal calculateProductTax(InvoiceProductDTO ip);
}
