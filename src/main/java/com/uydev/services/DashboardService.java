package com.uydev.services;

import java.math.BigDecimal;
import java.util.Map;

public interface DashboardService{
    Map<String, BigDecimal> getSummaryNumbers();
}
