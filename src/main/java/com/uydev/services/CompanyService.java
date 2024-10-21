package com.uydev.services;

import com.uydev.dto.CompanyDTO;

import java.util.List;

public interface CompanyService {

     List<CompanyDTO> getAllCompany();

     CompanyDTO getCompanyById(Long id);

     void updateCompany(CompanyDTO companyDto);
}
