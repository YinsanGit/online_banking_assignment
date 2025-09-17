package com.finalBanking.demo.Controller;

import com.finalBanking.demo.Dto.AccountRequest;
import com.finalBanking.demo.Dto.AccountResponse;
import com.finalBanking.demo.Exception.ApiResponseEntityDto;
import com.finalBanking.demo.Exception.ApiResponseUtil;
import com.finalBanking.demo.Service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponseEntityDto> createAccount(
            @Valid @RequestBody AccountRequest request,
            @RequestHeader("X-User") String createdByUser
    ) {
        try {
            AccountResponse response = accountService.createAccount(request, createdByUser);

            return ResponseEntity.ok(ApiResponseUtil.successResponse(response));

        } catch (Exception e) {
            ApiResponseEntityDto errorBody = ApiResponseUtil.createApiResponseEntityDto(
                    "500",
                    500,
                    "Internal Server Error",
                    e.getMessage(),
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBody);
        }
    }


    // Get Account by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseEntityDto> getAccountById(@PathVariable Long id) {
        try {
            AccountResponse response = accountService.getAccountById(id);
            if (response != null) {
                return ResponseEntity.ok(ApiResponseUtil.successResponse(response));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Get all Accounts
    @GetMapping("/getAll")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        try {
            List<AccountResponse> accounts = accountService.getAllAccounts();
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Update Account
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseEntityDto> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody AccountRequest request,
            @RequestHeader("X-User") String updatedByUser
    ) {
        try {
            AccountResponse updatedAccount = accountService.updateAccount(id, request, updatedByUser);
            if (updatedAccount != null) {
                return ResponseEntity.ok(ApiResponseUtil.successResponse(updatedAccount));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete Account
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        try {
            boolean isDeleted = accountService.deleteAccount(id);
            if (isDeleted) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

