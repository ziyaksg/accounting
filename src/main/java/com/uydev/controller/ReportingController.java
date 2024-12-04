package com.uydev.controller;

import com.uydev.services.InvoiceProductService;
import com.uydev.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Controller
@RequestMapping("/reports")
public class ReportingController {
    private final ReportService reportService;
    private final InvoiceProductService invoiceProductService;

    @GetMapping("/profitLossData/table")
    public String profitLossData(Model model){
//    model.addAttribute("monthlyProfitLossDataMap",reportService.monthlyProfitLossDataMap()) ;
    model.addAttribute("monthlyProfitLossData",reportService.monthlyProfitLossData()) ;
return "report/profit-loss-report-table";
}

    @GetMapping("productProfitLoss/table")
    public String productProfitLoss(Model model){
        model.addAttribute("productProfitLossDataMap",reportService.getProductProfitLoss());
        return "/report/product-profit-loss-table";
    }

    @GetMapping("/stockData")
    public String stackReport(Model model){
        model.addAttribute("invoiceProducts", invoiceProductService.getAllInvoiceProducts());
        return "/report/stock-report";
    }


}
