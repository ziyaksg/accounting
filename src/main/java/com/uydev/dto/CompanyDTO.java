package com.uydev.dto;


import com.uydev.enums.CompanyStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CompanyDTO {
    private Long id;

    private String title;

    private String phone;

    private String website;

    private CompanyStatus companyStatus;

    private AddressDTO address;

    private LocalDateTime insertDateTime;
}
