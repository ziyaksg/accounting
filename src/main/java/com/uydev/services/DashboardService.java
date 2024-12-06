package com.uydev.services;

import com.uydev.dto.exchange.ExchangeResponseDTO;

import java.math.BigDecimal;
import java.util.Map;

public interface DashboardService{
    Map<String, BigDecimal> getSummaryNumbers();

    Map<String, BigDecimal> getCurrencies();
}
