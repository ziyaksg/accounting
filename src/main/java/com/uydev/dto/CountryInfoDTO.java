package com.uydev.dto;
import lombok.Getter;

import java.util.Map;

@Getter
public class CountryInfoDTO {
    private Map<String, Map<String, Country>> data;
}
