package com.E_commerce.dao;

import com.E_commerce.entity.TblUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface UserRepository extends JpaRepository<TblUsers, BigInteger> {

    TblUsers findByEmail(String email);
}
