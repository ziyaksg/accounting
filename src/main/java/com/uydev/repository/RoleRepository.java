package com.uydev.repository;

import com.uydev.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    @Query("select r FROM  Role r where r.id !=1")
    List<Role> findAllRoles();
}
