package com.finalBanking.demo.Dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponse(
        Long id,
        String accountHolderName,
        String accountHolderEmail,
        String accountHolderPhone,
        String nationalId,
        String accountNumber,
        com.finalBanking.demo.Enumration.AccountType accountType,
        BigDecimal balance,
        Boolean isActive,
        LocalDateTime createdAt
) {

}
