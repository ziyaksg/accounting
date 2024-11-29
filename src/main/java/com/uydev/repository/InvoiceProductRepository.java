package com.uydev.repository;

import com.uydev.entity.InvoiceProduct;
import com.uydev.enums.InvoiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface InvoiceProductRepository extends JpaRepository<InvoiceProduct, Long> {
    @Query("Select ip from InvoiceProduct ip where ip.invoice.company.id=:companyId " +
            "and ip.invoice.invoiceType=:invoiceType and ip.isDeleted=:isDeleted and ip.invoice.id=:invoiceId")
    List<InvoiceProduct> findAllInvoiceProductByCompanyIdAndInvoiceType
            (Long companyId, InvoiceType invoiceType, boolean isDeleted, Long invoiceId);

    @Query("select ip FROM InvoiceProduct ip " +
            "WHERE YEAR(ip.insertDateTime) = :year " +
            "AND MONTH(ip.insertDateTime) = :month " +
            "AND ip.invoice.company.id = :companyId " +
            "AND ip.invoice.invoiceType = :invoiceType ")
List<InvoiceProduct> getTotalProfitLossByMonthForCompany(int year,int month,Long companyId,InvoiceType invoiceType);

    List<InvoiceProduct> getInvoiceProductByProduct_IdOrderByInsertDateTimeAsc(Long productId);

}
