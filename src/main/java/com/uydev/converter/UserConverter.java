package com.uydev.converter;

import com.uydev.dto.UserDTO;
import org.springframework.core.convert.converter.Converter;

public class UserConverter implements Converter<String, UserDTO> {

    @Override
    public UserDTO convert(String source) {
        return null;
    }
}