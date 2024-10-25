package com.uydev.converter;

import com.uydev.dto.CompanyDTO;
import com.uydev.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CompanyConverter implements Converter<String, CompanyDTO> {
    private final CompanyService companyService;

    @Override
    public CompanyDTO convert(String companyId) {
        return companyService.getCompanyById(Long.parseLong(companyId));
    }
}
