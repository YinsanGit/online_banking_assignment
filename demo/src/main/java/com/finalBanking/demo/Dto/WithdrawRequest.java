package com.finalBanking.demo.Dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record WithdrawRequest(
        @NotBlank
        String accountNumber,
        @NotNull @DecimalMin(value = "0.01") BigDecimal amount,

        @Size(max = 255)
        String description



) {
}
