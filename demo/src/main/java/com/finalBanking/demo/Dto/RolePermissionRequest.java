package com.finalBanking.demo.Dto;

import jakarta.validation.constraints.NotBlank;

public record RolePermissionRequest(
        @NotBlank
        String name
) {
}
