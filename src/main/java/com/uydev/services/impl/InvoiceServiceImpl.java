package com.uydev.services.impl;

import com.uydev.dto.*;
import com.uydev.entity.Invoice;
import com.uydev.enums.InvoiceStatus;
import com.uydev.enums.InvoiceType;
import com.uydev.mapper.MapperUtil;
import com.uydev.repository.InvoiceRepository;
import com.uydev.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final SecurityService securityService;
    private final MapperUtil mapperUtil;
    private final ClientVendorService clientVendorService;
    private final InvoiceProductService invoiceProductService;
    private final ProductService productService;

    @Override
    public List<InvoiceDTO> getAllPurchaseInvoice() {
        return getInvoicesByType(InvoiceType.PURCHASE);
    }

    private List<InvoiceDTO> getInvoicesByType(InvoiceType invoiceType) {
        Long companyId = securityService.getLoggedInUser().getCompany().getId();
        return invoiceRepository.findAllByCompanyIdAndInvoiceTypeAndIsDeleted
                        (companyId, invoiceType, false).stream()
                .map(invoice -> {
                            InvoiceDTO invoiceDTO = mapperUtil.convert(invoice, new InvoiceDTO());
                            invoiceDTO.setTotal(invoiceProductService.calculateTotal(invoice.getId()));
                            invoiceDTO.setHasInvoiceProduct(invoiceProductService.hasInvoiceProduct(invoice.getId()));
                            return invoiceDTO;
                        }
                ).collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO createNewPurchaseInvoice() {
        return generateNewInvoiceByType(InvoiceType.PURCHASE);
    }

    private InvoiceDTO generateNewInvoiceByType(InvoiceType invoiceType) {
        CompanyDTO companyDTO = securityService.getLoggedInUser().getCompany();
        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setDate(LocalDate.now());

        invoiceDTO.setInvoiceNo(
                getNextInvoiceNo(companyDTO.getId(), invoiceType)
        );
        return invoiceDTO;
    }

    private String getNextInvoiceNo(Long companyId, InvoiceType invoiceType) {

        Integer lastInvoiceNo = invoiceRepository.countAllByCompanyIdAndInvoiceType(companyId, invoiceType);
        if (invoiceType.equals(InvoiceType.PURCHASE)) {

            return "P-" + String.format("%03d", lastInvoiceNo + 1);
        } else return "S-" + String.format("%03d", lastInvoiceNo + 1);
    }

    @Override
    public List<ClientVendorDTO> getAllVendors() {
        return clientVendorService.getAllVendors();

    }

    @Override
    public InvoiceDTO savePurchaseInvoice(InvoiceDTO invoiceDTO) {
        return saveInvoices(invoiceDTO, InvoiceType.PURCHASE);
    }

    @Override
    public InvoiceDTO saveSalesInvoice(InvoiceDTO invoiceDTO) {
        return saveInvoices(invoiceDTO, InvoiceType.SALES);
    }

    private InvoiceDTO saveInvoices(InvoiceDTO invoiceDTO, InvoiceType invoiceType) {
        invoiceDTO.setCompany(securityService.getLoggedInUser().getCompany());
        invoiceDTO.setInvoiceType(invoiceType);
        invoiceDTO.setInvoiceStatus(InvoiceStatus.AWAITING_APPROVAL);
        return mapperUtil.convert(invoiceRepository
                .save(mapperUtil.convert(invoiceDTO, new Invoice())), new InvoiceDTO());
    }

    @Override
    public InvoiceDTO findById(Long id,InvoiceType invoiceType) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(() -> new RuntimeException("there is no invoice with " + id));
        InvoiceDTO invoiceDto = mapperUtil.convert(invoice, new InvoiceDTO());
        List<InvoiceProductDTO> invoiceProducts;
        if (invoiceType.equals(InvoiceType.PURCHASE)) {
     invoiceProducts= invoiceProductService.getAllPurchaseInvoiceProducts(id);
        }
        else {
            invoiceProducts=invoiceProductService.getAllSalesInvoiceProducts(id);
        }

        invoiceDto.setTax(calculateTax(invoiceProducts));
        invoiceDto.setPrice(calculateSubTotal(invoiceProducts));    //subtotal;
        invoiceDto.setTotal(invoiceDto.getPrice().add(invoiceDto.getTax()));//GRAND TOTAL
        return invoiceDto;
    }


    private BigDecimal calculateSubTotal(List<InvoiceProductDTO> invoiceProducts) {
        return invoiceProducts.stream()
                .map(ip ->
                        ip.getPrice().multiply(BigDecimal.valueOf(ip.getQuantity())) //250*5=1250
                ).reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    private BigDecimal calculateTax(List<InvoiceProductDTO> invoiceProducts) {
        return invoiceProducts.stream()
                .map(ip -> calculateProductTax(ip)
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculateProductTax(InvoiceProductDTO ip) {
        BigDecimal price = ip.getPrice();
        Integer taxRate = ip.getTax();
        Integer quantity = ip.getQuantity();
        BigDecimal tax = (price.multiply(BigDecimal.valueOf(((long) taxRate * quantity)))).divide(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
        return tax;

    }

    @Override
    public List<InvoiceDTO> getAllSalesInvoices() {
        return getInvoicesByType(InvoiceType.SALES);

    }

    @Override
    public InvoiceDTO createNewSalesInvoice() {
        return generateNewInvoiceByType(InvoiceType.SALES);

    }

    @Override
    public List<ClientVendorDTO> getAllClients() {
        return clientVendorService.getAllClients();
    }

    @Override
    public void addProduct(InvoiceProductDTO invoiceProductDTO, Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(() -> new RuntimeException("there is no invoice with " + invoiceId));
        invoiceProductDTO.setInvoice(mapperUtil.convert(invoice, new InvoiceDTO()));
        invoiceProductService.save(invoiceProductDTO);
    }

    @Override
    public void delete(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow((() -> new RuntimeException("there is no invoice with " + id)));
        invoice.setIsDeleted(true);
        invoiceRepository.save(invoice);
    }
@Override
    public void removeProductFromPurchaseInvoice(Long invoiceId, Long invoiceProductId) {
        removeProduct(InvoiceType.PURCHASE, invoiceId, invoiceProductId);
    }
    @Override
    public void removeProductFromSalesInvoice(Long invoiceId, Long invoiceProductId) {
        removeProduct(InvoiceType.SALES, invoiceId, invoiceProductId);
    }

    private void removeProduct(InvoiceType invoiceType, Long invoiceId, Long invoiceProductId) {
        List<InvoiceProductDTO> invoiceProductList;
        if (invoiceType.equals(InvoiceType.PURCHASE)) {
            invoiceProductList = invoiceProductService.getAllPurchaseInvoiceProducts(invoiceId);
        } else {
            invoiceProductList = invoiceProductService.getAllSalesInvoiceProducts(invoiceId);
        }

        for (InvoiceProductDTO invoiceProduct : invoiceProductList) {
            if (invoiceProduct.getId().equals(invoiceProductId)) {
                invoiceProductService.deleteById(invoiceProduct.getId());
            }
        }
    }

    @Override
    public void approvePurchase(Long invoiceId) {
        // change status
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(RuntimeException::new);
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoice.setDate(LocalDate.now());
        invoiceRepository.save(invoice);

        // increase product stock and invoiceProduct remaining quantity,
        List<InvoiceProductDTO> invoiceProductList = invoiceProductService.getAllPurchaseInvoiceProducts(invoiceId);
        for (InvoiceProductDTO ip :invoiceProductList){
            ProductDTO product = productService.findById(ip.getProduct().getId());
            product.setQuantityInStock(product.getQuantityInStock() + ip.getQuantity());
            productService.save(product);

            //set invoiceProduct remaining quantity

            ip.setRemainingQuantity(ip.getQuantity());
            ip.setProfitLoss(BigDecimal.ZERO);
            invoiceProductService.save(ip);


        }

    }

    @Override
    public void approveSales(Long invoiceId) {
        // change status
        Invoice invoice = invoiceRepository.findById(invoiceId).orElseThrow(RuntimeException::new);
        invoice.setInvoiceStatus(InvoiceStatus.APPROVED);
        invoice.setDate(LocalDate.now());
        invoiceRepository.save(invoice);

        // decrease product stock and invoiceProduct remaining quantity,
        List<InvoiceProductDTO> invoiceProductList = invoiceProductService.getAllSalesInvoiceProducts(invoiceId);
        for (InvoiceProductDTO ip :invoiceProductList){
            // decrease product stock
            ProductDTO product = productService.findById(ip.getProduct().getId());
            product.setQuantityInStock(product.getQuantityInStock() - ip.getQuantity());
            productService.save(product);
            ip.setRemainingQuantity(ip.getQuantity());
            ip.setProfitLoss(BigDecimal.ZERO);
            invoiceProductService.save(ip);

            // invoiceProduct remaining quantity
            List<InvoiceProductDTO> invoiceProducts = invoiceProductService.getAllPurchaseInvoiceProductsByProductId(product.getId());

            for (InvoiceProductDTO pip :invoiceProducts){
                if (ip.getRemainingQuantity()>=pip.getRemainingQuantity()){   // apple 2 purchase  950
                                                                    // apple 5 purchase  1200
                    int quantity = pip.getRemainingQuantity();                                                // apple 6 sales  1000
                    pip.setRemainingQuantity(0);
                    invoiceProductService.save(pip);
                    // calculate profit
                    BigDecimal profitPrice = ip.getPrice().subtract(pip.getPrice()); //50
                    BigDecimal profit = profitPrice.multiply(BigDecimal.valueOf(quantity)); // 100
                    ip.setProfitLoss(ip.getProfitLoss().add(profit));
                    ip.setRemainingQuantity(ip.getRemainingQuantity()-quantity);  // 4
                    invoiceProductService.save(ip);
                }
                else {
                    int quantity = ip.getRemainingQuantity(); // 4
                    pip.setRemainingQuantity(pip.getRemainingQuantity()- ip.getRemainingQuantity()); // 1
                    invoiceProductService.save(pip);
                    // calculate profit
                    BigDecimal profitPrice = ip.getPrice().subtract(pip.getPrice()); // -200
                    BigDecimal profit = profitPrice.multiply(BigDecimal.valueOf(quantity)); // -800
                    ip.setProfitLoss(ip.getProfitLoss().add(profit)); // -700
                    ip.setRemainingQuantity(0);
                    invoiceProductService.save(ip);
                }
                }
            if (ip.getRemainingQuantity()==0){
                break;
            }
        }
    }
}
