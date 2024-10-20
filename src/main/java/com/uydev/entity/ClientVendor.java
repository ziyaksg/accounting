package com.uydev.entity;
import com.uydev.enums.ClientVendorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Where;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Where(clause = "is_deleted=false")
@Table(name = "clients_vendors")
@ToString
public class ClientVendor extends BaseEntity {

    private String clientVendorName;
    private String phone;
    private String website;


    @Enumerated(EnumType.STRING)
    private ClientVendorType clientVendorType;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;



}
