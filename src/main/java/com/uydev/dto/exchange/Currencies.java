package com.uydev.dto.exchange;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class Currencies {
    //In the exchange table, we only want to see these currencies.
    private String name;
    private BigDecimal value;


}
