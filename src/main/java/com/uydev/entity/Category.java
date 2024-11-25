package com.uydev.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "categories")
@Where(clause = "is_deleted=false")
public class Category extends BaseEntity {

    private String description;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Company company;


}

