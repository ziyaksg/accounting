package com.uydev.controller;

import com.uydev.dto.InvoiceDTO;
import com.uydev.dto.InvoiceProductDTO;
import com.uydev.enums.InvoiceType;
import com.uydev.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/purchaseInvoices")
public class PurchaseInvoiceController {
    private final InvoiceService invoiceService;
    private final ProductService productService;
    private final InvoiceProductService invoiceProductService;
    private final SecurityService securityService;

    @GetMapping("/list")
    public String getList(Model model) {

        model.addAttribute("invoices", invoiceService.getAllPurchaseInvoice());


        return "/invoice/purchase-invoice-list";
    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("newPurchaseInvoice", invoiceService.createNewPurchaseInvoice());
        model.addAttribute("vendors", invoiceService.getAllVendors());
        return "/invoice/purchase-invoice-create";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute("newPurchaseInvoice") InvoiceDTO invoiceDTO) {
        InvoiceDTO savedInvoiceDto = invoiceService.savePurchaseInvoice(invoiceDTO);

        return "redirect:/purchaseInvoices/update/" + savedInvoiceDto.getId();
    }

    @GetMapping("/update/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("invoice", invoiceService.findById(id,InvoiceType.PURCHASE));
        model.addAttribute("vendors", invoiceService.getAllVendors());
        model.addAttribute("newInvoiceProduct", new InvoiceProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("invoiceProducts", invoiceProductService.getAllPurchaseInvoiceProducts(id));


        return "/invoice/purchase-invoice-update";
    }

    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String addProduct(@ModelAttribute("newInvoiceProduct") InvoiceProductDTO invoiceProductDTO, @PathVariable Long invoiceId) {

        invoiceService.addProduct(invoiceProductDTO, invoiceId);

        return "redirect:/purchaseInvoices/update/" + invoiceId;
    }

    @PostMapping("/update/{id}")
    public String saveUpdateInvoice(@ModelAttribute("newPurchaseInvoice") InvoiceDTO invoiceDTO, @PathVariable Long id) {
        invoiceService.savePurchaseInvoice(invoiceDTO);

        return "redirect:/purchaseInvoices/update/" + id;
    }

    @GetMapping("/delete/{id}")
    public String deletePurchaseInvoice(@PathVariable("id") Long id) {
        invoiceService.delete(id);

        return "redirect:/purchaseInvoices/list";
    }

    @GetMapping("removeInvoiceProduct/{invoiceId}/{invoiceProductId}")
    public String removeInvoiceProduct(@PathVariable("invoiceProductId") Long invoiceProductId, @PathVariable("invoiceId") Long invoiceId) {
        invoiceService.removeProductFromPurchaseInvoice(invoiceId, invoiceProductId);

        return "redirect:/purchaseInvoices/update/" + invoiceId;
    }

    @GetMapping("/print/{id}")
    public String printPurchaseInvoice(@PathVariable("id") Long id, Model model) {
        model.addAttribute("company", securityService.getLoggedInUser().getCompany());
        model.addAttribute("invoice", invoiceService.findById(id, InvoiceType.PURCHASE));
        model.addAttribute("invoiceProducts", invoiceProductService.getAllPurchaseInvoiceProducts(id));

        return "/invoice/invoice_print";
    }

    @GetMapping("/approve/{invoiceId}")
    public String approveInvoice(@PathVariable Long invoiceId){

        invoiceService.approvePurchase(invoiceId);
        return "redirect:/purchaseInvoices/list";
    }


}
