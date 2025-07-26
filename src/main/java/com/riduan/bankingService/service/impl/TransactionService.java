package com.riduan.bankingService.service.impl;

import com.riduan.bankingService.dto.TransactionDto;


public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
