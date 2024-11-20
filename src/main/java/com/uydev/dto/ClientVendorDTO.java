package com.uydev.dto;

import com.uydev.entity.Company;
import com.uydev.enums.ClientVendorType;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
public class ClientVendorDTO {

    private Long id;

    private String clientVendorName;

    private ClientVendorType clientVendorType;

    private String phone;

    private String website;

    private AddressDTO address;

    private boolean hasInvoice;

    private CompanyDTO company;


}