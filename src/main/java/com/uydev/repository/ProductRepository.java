package com.uydev.repository;

import com.uydev.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p " +
            "JOIN Category ca ON p.category.id = ca.id " +
            "JOIN Company c ON ca.company.id = c.id " +
            "WHERE c.id = :companyId")

//    @Query("SELECT p FROM Product p " +
//            "JOIN p.category c " +
//            "JOIN c.company co " +
//            "WHERE co.id = ?1")

    List<Product> findProductsByCompanyId(Long companyId);

    Product findByIdAndIsDeleted(Long productId, boolean isDeleted);
}
