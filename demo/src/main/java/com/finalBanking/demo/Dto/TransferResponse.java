package com.finalBanking.demo.Dto;

import com.finalBanking.demo.Enumration.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponse(
        Long transactionId,
        String fromAccountNumber,
        String toAccountNumber,
        BigDecimal amount,
        TransactionStatus status,
        String processedBy,
        LocalDateTime timestamp,
        String message
) {
}
