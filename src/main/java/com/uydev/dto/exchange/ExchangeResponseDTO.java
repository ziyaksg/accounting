package com.uydev.dto.exchange;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeResponseDTO {

    //The JSON file was saying that we needed to add these objects

    private List<Currencies> data;


}
