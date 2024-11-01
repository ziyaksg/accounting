package com.uydev.clients;

import com.uydev.dto.CountryInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;


@Component
@FeignClient(url = "https://api.first.org/data/v1/countries", name = "COUNTRY-CLIENT")
public interface CountryClient {
    @GetMapping("")
    ResponseEntity<CountryInfoDTO> getCountries();
}
