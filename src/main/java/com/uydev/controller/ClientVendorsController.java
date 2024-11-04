package com.uydev.controller;

import com.uydev.dto.ClientVendorDTO;
import com.uydev.enums.ClientVendorType;
import com.uydev.services.ClientVendorService;
import com.uydev.services.CompanyService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/clientVendors")
public class ClientVendorsController {
    private final ClientVendorService clientVendorService;
    private final CompanyService companyService;

    @GetMapping("/list")
    public String getAllClientVendors(Model model){
        List<ClientVendorDTO> clientVendors = clientVendorService.getClientVendors();
        model.addAttribute("clientVendors", clientVendors);
        return "/clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String createClientVendors(Model model){

        model.addAttribute("newClientVendor", new ClientVendorDTO());
        model.addAttribute("clientVendorTypes", List.of(ClientVendorType.values()));
        model.addAttribute("countries", companyService.getAllCounties());

        return "/clientVendor/clientVendor-create";
    }
}
