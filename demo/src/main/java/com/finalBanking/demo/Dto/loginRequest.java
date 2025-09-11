package com.finalBanking.demo.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record loginRequest(
        @Email @NotBlank String email,
        @NotBlank String password

) {
}
