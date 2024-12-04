package com.uydev.enums;

import lombok.Getter;

@Getter
public enum Currency {
    EUR("eur","€"), USD("usd","$");

    private final String currency;
    private final String symbol;

    Currency(String currency,String symbol) {
        this.currency = currency;
        this.symbol = symbol;
    }
}