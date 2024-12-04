package com.uydev.services.impl;

import com.uydev.dto.CompanyDTO;
import com.uydev.dto.InvoiceProductDTO;
import com.uydev.dto.ProductDTO;
import com.uydev.dto.ProfitLoss;
import com.uydev.enums.Currency;
import com.uydev.enums.InvoiceType;
import com.uydev.services.InvoiceProductService;
import com.uydev.services.ProductService;
import com.uydev.services.ReportService;
import com.uydev.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private final SecurityService securityService;
    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;


    @Override
    public List<Map.Entry<String, BigDecimal>> monthlyProfitLossDataMap() {

        CompanyDTO loggedInCompany = securityService.getLoggedInUser().getCompany();
        LocalDate startingDate= loggedInCompany.getInsertDateTime().toLocalDate();
        List<LocalDate> mapKey= mapKeyGenerator(startingDate);

        return getMonthlyProfitLoss(loggedInCompany,mapKey);


    }

    private List<Map.Entry<String, BigDecimal>> getMonthlyProfitLoss(CompanyDTO loggedInCompany, List<LocalDate> mapKey) {
        List<Map.Entry<String, BigDecimal>> monthlyProfitLoss = new ArrayList<>();
        for (LocalDate date : mapKey) {
            BigDecimal profitLoss = invoiceProductService.getProfitLossByMonth(date.getYear(),date.getMonthValue(),loggedInCompany.getId(), InvoiceType.SALES);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM");
            String key = date.format(formatter);
        monthlyProfitLoss.add(Map.entry(key,profitLoss));
        }
        return monthlyProfitLoss;

    }

    private List<LocalDate> mapKeyGenerator(LocalDate startingDate) {
        List<LocalDate> mapKey = new ArrayList<>();
        LocalDate endDate=LocalDate.now();

        while(startingDate.isBefore(endDate)){
            mapKey.add(endDate);
endDate =endDate.minusMonths(1);
        }

        return mapKey;


    }

    @Override
    public List<ProfitLoss> monthlyProfitLossData() {
        CompanyDTO loggedInCompany = securityService.getLoggedInUser().getCompany();
        LocalDate startingDate= loggedInCompany.getInsertDateTime().toLocalDate();
        List<LocalDate> mapKey= mapKeyGenerator(startingDate);

        List<ProfitLoss> monthlyProfitLoss = new ArrayList<>();
        for (LocalDate date : mapKey) {
            BigDecimal profitLoss = invoiceProductService.getProfitLossByMonth(date.getYear(),date.getMonthValue(),loggedInCompany.getId(), InvoiceType.SALES);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MMMM");
            String key = date.format(formatter);
            monthlyProfitLoss.add(new ProfitLoss(key,profitLoss, Currency.USD));
        }
        return monthlyProfitLoss;

    }

    @Override
    public List<Map.Entry<String, BigDecimal>> getProductProfitLoss() {
        List<ProductDTO> products = productService.getAllProducts();
        List<Map.Entry<String,BigDecimal>> productProfitLoss = new ArrayList<>();
        for (ProductDTO product : products) {
            BigDecimal profitLoss = getProductProfitLossByProductId(product.getId());
            productProfitLoss.add(Map.entry(product.getName(),profitLoss));
        }
        return productProfitLoss;
    }

    private BigDecimal getProductProfitLossByProductId(Long productId) {
        List<InvoiceProductDTO> invoiceProducts = invoiceProductService.getAllSalesInvoiceProductsByProductId(productId);
        return  invoiceProducts.stream()
                .map(InvoiceProductDTO::getProfitLoss)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
