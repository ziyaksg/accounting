package com.uydev.services;

import com.uydev.dto.ClientVendorDTO;

import java.util.List;

public interface ClientVendorService {
    List<ClientVendorDTO> getClientVendors();

    void createNewClientVendor(ClientVendorDTO newClientVendor);

    ClientVendorDTO findById(long id);

    void updateClientVendor(ClientVendorDTO updatedClientVendor);

    void deleteByid(long clientVendorId);
}
