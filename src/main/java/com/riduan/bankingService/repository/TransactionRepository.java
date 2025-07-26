package com.riduan.bankingService.repository;

import com.riduan.bankingService.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
