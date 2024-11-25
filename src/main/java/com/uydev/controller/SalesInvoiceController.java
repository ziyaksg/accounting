package com.uydev.controller;


import com.uydev.dto.InvoiceDTO;
import com.uydev.dto.InvoiceProductDTO;
import com.uydev.enums.InvoiceType;
import com.uydev.services.InvoiceProductService;
import com.uydev.services.InvoiceService;
import com.uydev.services.ProductService;
import com.uydev.services.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/salesInvoices")
public class SalesInvoiceController {
    private final InvoiceService invoiceService;
    private final ProductService productService;
    private final InvoiceProductService invoiceProductService;
    private  final SecurityService securityService;

    @GetMapping("/list")
    public String getAllSalesInvoices(Model model) {
        model.addAttribute("invoices", invoiceService.getAllSalesInvoices());
        return "invoice/sales-invoice-list";
    }

    @GetMapping("/create")
    public String createSalesInvoices(Model model) {
        model.addAttribute("newSalesInvoice", invoiceService.createNewSalesInvoice());
        model.addAttribute("clients", invoiceService.getAllClients());
        return "invoice/sales-invoice-create";
    }

    @PostMapping("/create")
    public String saveCreatedSalesInvoices(@ModelAttribute("newSalesInvoice") InvoiceDTO invoiceDTO) {
        InvoiceDTO savedInvoice = invoiceService.saveSalesInvoice(invoiceDTO);
        return "redirect:/salesInvoices/update/" + savedInvoice.getId();
    }

    @GetMapping("/update/{id}")
    public String updateSalesInvoice(Model model, @PathVariable Long id) {
        model.addAttribute("invoice", invoiceService.findById(id,InvoiceType.SALES));
        model.addAttribute("clients", invoiceService.getAllClients());
        model.addAttribute("newInvoiceProduct", new InvoiceProductDTO());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("invoiceProducts", invoiceProductService.getAllSalesInvoiceProducts(id));
        return "/invoice/sales-invoice-update";
    }

    @PostMapping("/update/{id}")
    public String saveUpdateInvoice(@ModelAttribute("newSalesInvoice") InvoiceDTO invoiceDTO, @PathVariable Long id) {
        invoiceService.saveSalesInvoice(invoiceDTO);

        return "redirect:/salesInvoices/update/" + id;
    }

    @PostMapping("/addInvoiceProduct/{invoiceId}")
    public String addProduct(@ModelAttribute("newInvoiceProduct") InvoiceProductDTO invoiceProductDTO, @PathVariable Long invoiceId) {

        invoiceService.addProduct(invoiceProductDTO, invoiceId);

        return "redirect:/salesInvoices/update/" + invoiceId;
    }

    @GetMapping("/delete/{id}")
    public String deleteSalesInvoice(@PathVariable("id") Long id) {
        invoiceService.delete(id);

        return "redirect:/salesInvoices/list";
    }

    @GetMapping("removeInvoiceProduct/{invoiceId}/{invoiceProductId}")
    public String removeInvoiceProduct(@PathVariable("invoiceProductId") Long invoiceProductId, @PathVariable("invoiceId") Long invoiceId) {
        invoiceService.removeProductFromSalesInvoice(invoiceId, invoiceProductId);

        return "redirect:/salesInvoices/update/" + invoiceId;
    }
    @GetMapping("/print/{id}")
    public String printSalesInvoice(@PathVariable("id") Long id, Model model) {
        model.addAttribute("company", securityService.getLoggedInUser().getCompany());
        model.addAttribute("invoice", invoiceService.findById(id, InvoiceType.SALES));
        model.addAttribute("invoiceProducts", invoiceProductService.getAllSalesInvoiceProducts(id));

        return "/invoice/invoice_print";
    }
}


