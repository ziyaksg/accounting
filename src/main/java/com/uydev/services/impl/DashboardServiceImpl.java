package com.uydev.services.impl;

import com.uydev.clients.CurrencyClient;
import com.uydev.dto.InvoiceDTO;
import com.uydev.dto.InvoiceProductDTO;
import com.uydev.dto.exchange.ExchangeResponseDTO;
import com.uydev.entity.Invoice;
import com.uydev.services.DashboardService;
import com.uydev.services.InvoiceProductService;
import com.uydev.services.InvoiceService;
import com.uydev.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final SecurityService securityService;
    private final InvoiceService invoiceService;
    private final InvoiceProductService invoiceProductService;
    private final CurrencyClient currencyClient;
    @Override
    public Map<String, BigDecimal> getSummaryNumbers() {
        BigDecimal totalSales = BigDecimal.ZERO;
        BigDecimal profitLoss = BigDecimal.ZERO;
        BigDecimal totalCost = BigDecimal.ZERO;

        Map<String,BigDecimal> summaryNumbers = new HashMap<>();

        List<InvoiceDTO> purchaseInvoices = invoiceService.getAllPurchaseInvoice();

        totalCost = totalCost.add(purchaseInvoices.stream()
                .map(inv-> invoiceProductService.calculateTotal(inv.getId()))
                .reduce(BigDecimal.ZERO,BigDecimal::add));

        List<InvoiceDTO> salesInvoices = invoiceService.getAllSalesInvoices();

        for (InvoiceDTO salesInvoice : salesInvoices) {
            totalSales = totalSales.add(invoiceProductService.calculateTotal(salesInvoice.getId()));

            List<InvoiceProductDTO> invoiceProductList = invoiceProductService.getAllSalesInvoiceProducts(salesInvoice.getId());
            profitLoss = profitLoss.add(invoiceProductList.stream()
                    .map(InvoiceProductDTO::getProfitLoss)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
        }

        summaryNumbers.put("totalSales",totalSales);
        summaryNumbers.put("profitLoss",profitLoss);
        summaryNumbers.put("totalCost",totalCost);

        return  summaryNumbers;
    }

    @Override
    public ExchangeResponseDTO getCurrencies() {
     return    currencyClient.getCurrencies();

    }
}
