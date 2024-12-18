package com.uydev.repository;

import com.uydev.entity.ClientVendor;
import com.uydev.enums.ClientVendorType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientVendorRepository extends JpaRepository<ClientVendor, Long> {
    List<ClientVendor> findAllByIsDeleted(boolean isDeleted);

    ClientVendor findByIdAndIsDeleted(Long id, boolean isDeleted);

    List<ClientVendor> findClientVendorsByClientVendorTypeAndCompanyIdAndIsDeleted
            (ClientVendorType clientVendorType, Long companyId, boolean isDeleted);
}
