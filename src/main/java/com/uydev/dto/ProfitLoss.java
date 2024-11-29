package com.uydev.dto;

import com.uydev.enums.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
@Getter
@AllArgsConstructor
public class ProfitLoss {
    private String month;
    private BigDecimal value;
    private Currency currency;
}
