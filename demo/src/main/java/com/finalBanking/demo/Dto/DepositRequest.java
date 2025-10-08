package com.finalBanking.demo.Dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DepositRequest(
        @NotBlank String accountNumber,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,
        @Size(max = 255)
        String description
        ) {
}
