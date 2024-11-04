package com.uydev.services.impl;

import com.uydev.dto.ClientVendorDTO;
import com.uydev.entity.ClientVendor;
import com.uydev.mapper.MapperUtil;
import com.uydev.repository.ClientVendorRepository;
import com.uydev.services.ClientVendorService;
import com.uydev.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientVendorServiceImpl implements ClientVendorService {
    private final ClientVendorRepository repository;
    private final SecurityService securityService;
    private final MapperUtil mapper;
    @Override
    public List<ClientVendorDTO> getClientVendors() {
        long loggedInCompanyId = securityService.getLoggedInUser().getCompany().getId();
        List<ClientVendor> clientVendors = repository.findAllByIsDeleted(false);
        return clientVendors.stream()
                .filter(cv->cv.getCompany().getId().equals(loggedInCompanyId))
                .map(cv->mapper.convert(cv,new ClientVendorDTO()))
                .toList();
    }
}
