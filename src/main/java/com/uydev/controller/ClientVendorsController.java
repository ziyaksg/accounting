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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/clientVendors")
public class ClientVendorsController {
    private final ClientVendorService clientVendorService;
    private final CompanyService companyService;

    @GetMapping("/list")
    public String getAllClientVendors(Model model) {
        List<ClientVendorDTO> clientVendors = clientVendorService.getClientVendors();
        model.addAttribute("clientVendors", clientVendors);
        return "/clientVendor/clientVendor-list";
    }

    @GetMapping("/create")
    public String createClientVendors(Model model) {

        model.addAttribute("newClientVendor", new ClientVendorDTO());
        model.addAttribute("clientVendorTypes", List.of(ClientVendorType.values()));
        model.addAttribute("countries", companyService.getAllCounties());

        return "/clientVendor/clientVendor-create";
    }

    @PostMapping("/create")
    public String saveClientVendors(@ModelAttribute("newClientVendor") ClientVendorDTO newClientVendor) {
        clientVendorService.createNewClientVendor(newClientVendor);
        return "redirect:/clientVendors/list";
    }

    @GetMapping("/update/{id}")
    public String updateClientVendor(@PathVariable("id") long id, Model model) {

        model.addAttribute("clientVendor", clientVendorService.findById(id));
        model.addAttribute("clientVendorTypes", List.of(ClientVendorType.values()));
        model.addAttribute("countries", companyService.getAllCounties());

        return "/clientVendor/clientVendor-update";
    }

    @PostMapping("/update/{id}")
    public String saveUpdatedClientVendor(@ModelAttribute("clientVendor") ClientVendorDTO updatedClientVendor) {
        clientVendorService.updateClientVendor(updatedClientVendor);
        return "redirect:/clientVendors/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteClientVendor(@PathVariable("id") long clientVendorId) {
        clientVendorService.deleteByid(clientVendorId);
        return "redirect:/clientVendors/list";
    }

}
