package com.uydev.controller;

import com.uydev.dto.CompanyDTO;
import com.uydev.entity.Company;
import com.uydev.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/list")
    public String companyList(Model model){

        List<CompanyDTO> allCompany = companyService.getAllCompany();
        model.addAttribute("companies",allCompany);
        return "/company/company-list";
    }

    @GetMapping("/update/{companyId}")
    public String companyUpdate(@PathVariable("companyId") Long companyId, Model model){

        CompanyDTO company = companyService.getCompanyById(companyId);
        model.addAttribute("company", company);

        return "/company/company-update";
    }

    @PostMapping("/update/{companyId}")
    public String companyUpdateSave(@ModelAttribute("company") CompanyDTO companyDto){
        companyService.updateCompany(companyDto);
        return "redirect:/companies/list";
    }
}
