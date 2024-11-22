package com.uydev.converter;

import com.uydev.dto.ClientVendorDTO;
import com.uydev.services.ClientVendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ClientVendorConvertor implements Converter<String, ClientVendorDTO> {
    private final ClientVendorService clientVendorService;

    @Override
    public ClientVendorDTO convert(String id) {
        if (id == null || id.isEmpty()) {
            return null;

        }
        return clientVendorService.findById(Long.parseLong(id));
    }
}
