package com.uydev.controller;

import com.uydev.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Controller
@RequestMapping("/reports/profitLossData")
public class ReportingController {
    private final ReportService reportService;

@GetMapping("/table")
    public String profitLossData(Model model){
//    model.addAttribute("monthlyProfitLossDataMap",reportService.monthlyProfitLossDataMap()) ;
    model.addAttribute("monthlyProfitLossData",reportService.monthlyProfitLossData()) ;
return "report/profit-loss-report-table";
}

}
