package com.uydev.services;

import com.uydev.dto.ProfitLoss;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReportService {
   List<Map.Entry<String, BigDecimal>> monthlyProfitLossDataMap();
   List<ProfitLoss> monthlyProfitLossData();

   List<Map.Entry<String, BigDecimal>> getProductProfitLoss();
}
