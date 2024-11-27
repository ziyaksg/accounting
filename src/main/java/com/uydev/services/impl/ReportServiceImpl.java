package com.uydev.services.impl;

import com.uydev.dto.CompanyDTO;
import com.uydev.enums.InvoiceType;
import com.uydev.services.InvoiceProductService;
import com.uydev.services.ReportService;
import com.uydev.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private final SecurityService securityService;
    private final InvoiceServiceImpl invoiceServiceImpl;
    private final InvoiceProductService invoiceProductService;



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
}
