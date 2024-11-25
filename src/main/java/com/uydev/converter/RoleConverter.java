package com.uydev.converter;

import com.uydev.dto.RoleDTO;
import com.uydev.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleConverter implements Converter<String, RoleDTO> {
    private final RoleService roleService;

    @Override
    public RoleDTO convert(String roleId) {

        if (roleId == null || roleId.isEmpty()) {
            return null;

        }
        return roleService.findById(Long.parseLong(roleId));
    }
}
