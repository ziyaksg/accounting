package com.uydev.controller;

import com.uydev.dto.CompanyDTO;
import com.uydev.services.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/companies")
public class CompanyController {

    private final CompanyService companyService;

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

    @PostMapping("/update/{id}")
    public String companyUpdateSave(@ModelAttribute("company") CompanyDTO companyDto){

        companyService.updateCompany(companyDto);
        return "redirect:/companies/list";
    }

    @GetMapping("/deactivate/{id}")
    public String deactivateCompany(@PathVariable("id")Long id){
        companyService.deactivate(id);
        return "redirect:/companies/list";
    }

    @GetMapping("/activate/{id}")
    public String activateCompany(@PathVariable("id")Long id){
        companyService.activate(id);
        return "redirect:/companies/list";
    }

    @GetMapping("/create")
    public String createCompany(Model model){
        model.addAttribute("newCompany", new CompanyDTO());

        return "/company/company-create";
    }

    @PostMapping("/create")
    public String saveCompany(@ModelAttribute("newCompany") CompanyDTO newCompany){
        companyService.createCompany(newCompany);
        return "redirect:/companies/list";
    }
}
