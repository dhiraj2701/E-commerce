package com.E_commerce.dao;

import com.E_commerce.entity.TblUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginRepo extends JpaRepository<TblUser, Long> {
     TblUser findByUsername(String username);

     Optional<Object> findByUsernameOrEmail(String username, String email);

    TblUser findByEmail(String email);

    TblUser findUserByEmail(String email);
}
