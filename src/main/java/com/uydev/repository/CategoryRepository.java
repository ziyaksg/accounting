package com.uydev.repository;

import com.uydev.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findCategoriesByCompany_IdAndIsDeleted(Long companyId,boolean isDeleted);

}
