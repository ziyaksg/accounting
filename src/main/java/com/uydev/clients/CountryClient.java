package com.uydev.clients;

import com.uydev.dto.CountryInfoDTO;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class CountryClient {
    private final RestClient restClient;

    public CountryClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public CountryInfoDTO getCountries(){
        return restClient.get()
                .uri("https://api.first.org/data/v1/countries")
                .retrieve()
                .body(CountryInfoDTO.class);
    }
}
