package com.uydev.repository;

import com.uydev.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {

    @Query("SELECT c FROM Company c WHERE c.id != 1 " +
            "ORDER BY CASE WHEN c.companyStatus = 'ACTIVE' THEN 0 ELSE 1 END, c.title")
    List<Company>getAllCompanyForRoot(Long company_id);

    @Query("SELECT c FROM Company c where c.id=?1")
    Company getCompanyById(Long id);
}
