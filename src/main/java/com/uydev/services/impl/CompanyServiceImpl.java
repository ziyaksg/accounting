package com.uydev.services.impl;

import com.uydev.clients.CountryClient;
import com.uydev.dto.CompanyDTO;
import com.uydev.dto.Country;
import com.uydev.dto.CountryInfoDTO;
import com.uydev.dto.UserDTO;
import com.uydev.entity.Company;
import com.uydev.enums.CompanyStatus;
import com.uydev.mapper.MapperUtil;
import com.uydev.repository.CompanyRepository;
import com.uydev.services.CompanyService;

import com.uydev.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
   private final CompanyRepository repository;
   private final SecurityService securityService;
   private final CountryClient countryClient;
   private final MapperUtil mapper;


    @Override
    public List<CompanyDTO> getAllCompany() {
        UserDTO loggedInUser = securityService.getLoggedInUser();
        if (loggedInUser.getId() !=1){
            return List.of(loggedInUser.getCompany());
        }
        List<Company> allCompanies = repository.getAllCompanyForRoot(1L);
        return allCompanies.stream().map(company -> mapper.convert(company, new CompanyDTO())).toList();
    }

    @Override
    public CompanyDTO getCompanyById(Long id) {
        return mapper.convert(repository.getCompanyById(id),new CompanyDTO());
    }

    @Override
    public void updateCompany(CompanyDTO companyDto) {

        Company oldCompany = repository.findById(companyDto.getId()).orElseThrow(() -> new RuntimeException("Company with id: " + companyDto.getId() + " Not Found "));
        companyDto.setCompanyStatus(oldCompany.getCompanyStatus());
        Company updatedCompany = mapper.convert(companyDto,new Company());
        repository.save(updatedCompany);
    }

    @Override
    public CompanyDTO deactivate(Long id) {
        Company company = repository.getCompanyById(id);
        company.setCompanyStatus(CompanyStatus.PASSIVE);
        repository.save(company);
        return mapper.convert(company, new CompanyDTO());
    }

    @Override
    public void activate(Long id) {
        Company company = repository.getCompanyById(id);
        company.setCompanyStatus(CompanyStatus.ACTIVE);
        repository.save(company);
    }

    @Override
    public void createCompany(CompanyDTO newCompany) {
        newCompany.setCompanyStatus(CompanyStatus.ACTIVE);
        repository.save(mapper.convert(newCompany, new Company()));

    }

    @Override
    public List<String> getAllCounties() {
        CountryInfoDTO allCountiesInfo = countryClient.getCountries();
        if (allCountiesInfo.getStatus().equals("OK"))
        {
            Collection<Country> country = allCountiesInfo.getData().values();
           return country.stream().map(Country::getCountry).toList();

        }
        return List.of();
    }
}
