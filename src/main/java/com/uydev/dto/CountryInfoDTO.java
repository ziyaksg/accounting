package com.uydev.dto;
import lombok.Getter;

import java.util.Map;

@Getter
public class CountryInfoDTO {
    private String status;
    private Map<String, Country> data;
}
