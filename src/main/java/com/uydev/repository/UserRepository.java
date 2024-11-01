package com.uydev.repository;

import com.uydev.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsernameAndIsDeleted(String username, boolean isDeleted);

    @Query("SELECT u FROM User u where u.isDeleted = :isDeleted")
    List<User> findAllAndIsDeleted(boolean isDeleted);
}
