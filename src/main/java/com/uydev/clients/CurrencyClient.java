package com.uydev.clients;

import com.uydev.dto.CountryInfoDTO;
import com.uydev.dto.exchange.ExchangeResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Component
public class CurrencyClient {
    @Value("${CURRENCY.APIKEY}")
    private String apikey;
    private final RestClient restClient;

    public CurrencyClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public ExchangeResponseDTO getCurrencies() {

        return  restClient.get()
                .uri("https://api.freecurrencyapi.com/v1/currencies?apikey="+apikey)
                .retrieve()
                .body(ExchangeResponseDTO.class);
    }
}
