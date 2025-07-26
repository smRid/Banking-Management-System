package com.riduan.bankingService.service.impl;


import com.riduan.bankingService.entity.Transaction;
import com.riduan.bankingService.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
public class BankStatement {
    /*
     * retrieve list of transaction within a date range given an account number
     * Generate a pdf file of transaction
     * Send the pdf file via email
     */

    private TransactionRepository transactionRepository;


    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        List<Transaction> transactionList = transactionRepository.findAll().stream()
                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().toLocalDate().isEqual(start) ||
                        transaction.getCreatedAt().toLocalDate().isAfter(start))
                .filter(transaction -> transaction.getCreatedAt().toLocalDate().isEqual(end) ||
                        transaction.getCreatedAt().toLocalDate().isBefore(end))
                .toList();

        return transactionList;
    }

}
