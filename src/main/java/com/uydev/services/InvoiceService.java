package com.uydev.services;

import com.uydev.dto.ClientVendorDTO;
import com.uydev.dto.InvoiceDTO;
import com.uydev.dto.InvoiceProductDTO;
import com.uydev.enums.InvoiceType;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceService {
    List<InvoiceDTO> getAllPurchaseInvoice();

    InvoiceDTO createNewPurchaseInvoice();

    List<ClientVendorDTO> getAllVendors();

    InvoiceDTO savePurchaseInvoice(InvoiceDTO invoiceDTO);

    InvoiceDTO findById(Long id, InvoiceType invoiceType);

    void addProduct(InvoiceProductDTO invoiceProductDTO, Long invoiceId);

    void delete(Long id);

    void removeProductFromPurchaseInvoice(Long invoiceId, Long invoiceProductId);
    void removeProductFromSalesInvoice(Long invoiceId, Long invoiceProductId);
    BigDecimal calculateProductTax(InvoiceProductDTO ip);

    List<InvoiceDTO> getAllSalesInvoices();

    InvoiceDTO createNewSalesInvoice();

    List<ClientVendorDTO> getAllClients();

    InvoiceDTO saveSalesInvoice(InvoiceDTO invoiceDTO);

    void approvePurchase(Long invoiceId);

    void approveSales(Long invoiceId);

    List<InvoiceDTO> getLastThreeInvoice();
}
