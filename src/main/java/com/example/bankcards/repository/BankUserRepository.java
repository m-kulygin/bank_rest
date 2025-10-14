package com.example.bankcards.repository;

import com.example.bankcards.entity.BankUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankUserRepository extends JpaRepository<BankUser, Long> {
}
