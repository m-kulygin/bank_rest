package com.example.bankcards.repository;

import com.example.bankcards.entity.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface BankCardRepository extends JpaRepository<BankCard, Long>, JpaSpecificationExecutor<BankCard> {
    List<BankCard> findByUser_Id(Long userId);
}
