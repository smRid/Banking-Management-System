package com.riduan.bankingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class CreditDebitRequest {
    private String accountNumber;
    private BigDecimal amount;
}
