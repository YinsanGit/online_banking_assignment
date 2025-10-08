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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@PreAuthorize("hasRole('ROLE_MANAGER')")
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



    @GetMapping("/getAll")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        try {
            List<AccountResponse> accounts = accountService.getAllAccounts();
            return ResponseEntity.ok(accounts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseEntityDto> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody AccountRequest request,
            @RequestHeader("X-User") String updatedByUser
    ) {
        try {
            AccountResponse updatedAccount = accountService.updateAccount(id, request, updatedByUser);
            if (updatedAccount != null) {
                log.info("Account with ID {} has been updated successfully", id);
                return ResponseEntity.ok(ApiResponseUtil.successResponse(updatedAccount));
            } else {
                ApiResponseEntityDto errorBody = ApiResponseUtil.createApiResponseEntityDto(
                        "404",
                        404,
                        "Not Found",
                        "Account with ID " + id + " not found",
                        null
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody);
            }
        } catch (Exception e) {
            log.error("Error updating account with ID {}: {}", id, e.getMessage(), e);
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


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseEntityDto> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok(
                ApiResponseUtil.deleteSuccessResponse("Account with ID " + id + " has been deleted successfully")
        );
    }


}

