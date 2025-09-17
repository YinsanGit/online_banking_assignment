package com.finalBanking.demo.Dto;

import com.finalBanking.demo.Enumration.AccountType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;


public record AccountRequest(
        @NotBlank
        String accountHolderName,
        @Email
        String accountHolderEmail,
        @NotBlank
        String accountHolderPhone,
        @NotBlank
        String nationalId,
        @NotNull
        AccountType accountType
) {
}
