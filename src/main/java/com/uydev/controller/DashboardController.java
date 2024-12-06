package com.uydev.controller;

import com.uydev.services.DashboardService;
import com.uydev.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    private final InvoiceService invoiceService;

    @GetMapping()
    public String getDashboard(Model model){
        model.addAttribute("summaryNumbers", dashboardService.getSummaryNumbers());
        model.addAttribute("invoices", invoiceService.getLastThreeInvoice());
        model.addAttribute("exchangeRates",dashboardService.getCurrencies());
        return "dashboard";
    }

}
