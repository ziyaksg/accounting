package com.uydev.controller;

import com.uydev.dto.InvoiceDTO;
import com.uydev.dto.InvoiceProductDTO;
import com.uydev.dto.ProductDTO;
import com.uydev.services.InvoiceProductService;
import com.uydev.services.InvoiceService;
import com.uydev.services.ProductService;
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
    @GetMapping("/list")
    public String getList(Model model){

        model.addAttribute("invoices",invoiceService.getAllPurchaseInvoice());


        return "/invoice/purchase-invoice-list";
    }
    @GetMapping("/create")
    public String create(Model model){

        model.addAttribute("newPurchaseInvoice",invoiceService.createNewPurchaseInvoice());
        model.addAttribute("vendors",invoiceService.getAllVendors());
        return "/invoice/purchase-invoice-create";
    }
    @PostMapping("/create")
    public String save(@ModelAttribute("newPurchaseInvoice") InvoiceDTO invoiceDTO){
        invoiceService.save(invoiceDTO);
        return "redirect:/purchaseInvoices/list";
    }
    @GetMapping("/update/{id}")
    public String edit(@PathVariable("id") Long id, Model model){
        model.addAttribute("invoice",invoiceService.findById(id));
        model.addAttribute("vendors",invoiceService.getAllVendors());
        model.addAttribute("newInvoiceProduct",new InvoiceProductDTO());
        model.addAttribute("products",productService.getAllProducts());
        model.addAttribute("invoiceProducts",invoiceProductService.getAllInvoiceProducts(id));


        return "/invoice/purchase-invoice-update";
    }
    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String addProduct(@ModelAttribute("newInvoiceProduct") InvoiceProductDTO invoiceProductDTO, @PathVariable Long invoiceId){

        invoiceService.addProduct(invoiceProductDTO,invoiceId);

        return "redirect:/purchaseInvoices/update/"+invoiceId;
    }
    @PostMapping("/update/{id}")
    public String saveUpdateInvoice(@ModelAttribute("newPurchaseInvoice") InvoiceDTO invoiceDTO, @PathVariable Long id){
        invoiceService.save(invoiceDTO);

        return "redirect:/purchaseInvoices/update/"+id;
    }



}