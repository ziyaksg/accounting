package com.uydev.dto;

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

    private String phone;

    private String website;

    private AddressDTO address;

    private boolean hasInvoice;


}