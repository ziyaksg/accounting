package com.uydev.services.impl;

import com.uydev.dto.ClientVendorDTO;
import com.uydev.dto.CompanyDTO;
import com.uydev.entity.ClientVendor;
import com.uydev.enums.ClientVendorType;
import com.uydev.mapper.MapperUtil;
import com.uydev.repository.ClientVendorRepository;
import com.uydev.services.ClientVendorService;
import com.uydev.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
                .filter(cv -> cv.getCompany().getId().equals(loggedInCompanyId))
                .map(cv -> mapper.convert(cv, new ClientVendorDTO()))
                .toList();
    }

    @Override
    public void createNewClientVendor(ClientVendorDTO newClientVendor) {
        CompanyDTO currentCompany = securityService.getLoggedInUser().getCompany();
        newClientVendor.setCompany(currentCompany);
        repository.save(mapper.convert(newClientVendor, new ClientVendor()));
    }

    @Override
    public ClientVendorDTO findById(long id) {
        return mapper.convert(repository.findByIdAndIsDeleted(id, false), new ClientVendorDTO());
    }

    @Override
    public void updateClientVendor(ClientVendorDTO updatedClientVendor) {
        CompanyDTO currentCompany = securityService.getLoggedInUser().getCompany();
        updatedClientVendor.setCompany(currentCompany);
        repository.save(mapper.convert(updatedClientVendor, new ClientVendor()));
    }

    @Override
    public void deleteByid(long clientVendorId) {
        ClientVendor clientVendor = repository.findById(clientVendorId).orElseThrow();
        clientVendor.setIsDeleted(true);
        repository.save(clientVendor);

    }

    @Override
    public List<ClientVendorDTO> getAllVendors() {
        return getClientVendorByType(ClientVendorType.VENDOR);
    }

    @Override
    public List<ClientVendorDTO> getAllClients() {
        return getClientVendorByType(ClientVendorType.CLIENT);
    }

    private List<ClientVendorDTO> getClientVendorByType(ClientVendorType clientVendorType) {
        Long companyId = securityService.getLoggedInUser().getCompany().getId();

        return repository.findClientVendorsByClientVendorTypeAndCompanyIdAndIsDeleted
                        (clientVendorType, companyId, false).stream()
                .map(vendor -> mapper.convert(vendor, new ClientVendorDTO())).collect(Collectors.toList());
    }

}
