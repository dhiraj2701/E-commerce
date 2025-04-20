package com.E_commerce.dao;

import com.E_commerce.entity.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepo extends JpaRepository<UserLogin, Long> {
     UserLogin findByUsername(String username);
}
