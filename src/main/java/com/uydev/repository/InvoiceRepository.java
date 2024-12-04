package com.uydev.repository;

import com.uydev.entity.Company;
import com.uydev.entity.Invoice;
import com.uydev.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findAllByCompanyIdAndInvoiceTypeAndIsDeleted
            (Long companyId, InvoiceType invoiceType, boolean isDeleted);

    Integer countAllByCompanyIdAndInvoiceType(Long companyId, InvoiceType invoiceType);


    List<Invoice> findTop3ByCompany_IdAndIsDeletedOrderByLastUpdateDateTimeDesc(Long id, boolean b);
}
