package com.uydev.services;

import com.uydev.dto.ClientVendorDTO;
import com.uydev.entity.ClientVendor;

import java.util.List;

public interface ClientVendorService {
    List<ClientVendorDTO> getClientVendors();

    void createNewClientVendor(ClientVendorDTO newClientVendor);

    ClientVendorDTO findById(long id);

    void updateClientVendor(ClientVendorDTO updatedClientVendor);

    void deleteByid(long clientVendorId);

    List<ClientVendorDTO> getAllVendors();

}
