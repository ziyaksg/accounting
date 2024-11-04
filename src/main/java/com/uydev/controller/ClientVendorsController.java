package com.uydev.controller;

import com.uydev.dto.ClientVendorDTO;
import com.uydev.services.ClientVendorService;
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

    @GetMapping("/list")
    public String getAllClientVendors(Model model){
        List<ClientVendorDTO> clientVendors = clientVendorService.getClientVendors();
        model.addAttribute("clientVendors", clientVendors);
        return "/clientVendor/clientVendor-list";
    }
}
