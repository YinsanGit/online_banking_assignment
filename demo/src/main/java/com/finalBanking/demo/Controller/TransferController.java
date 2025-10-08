package com.finalBanking.demo.Controller;

import com.finalBanking.demo.Dto.DepositRequest;
import com.finalBanking.demo.Dto.TransferRequest;
import com.finalBanking.demo.Dto.TransferResponse;
import com.finalBanking.demo.Dto.WithdrawRequest;
import com.finalBanking.demo.Entity.Transaction;
import com.finalBanking.demo.Exception.ApiResponseEntityDto;
import com.finalBanking.demo.Exception.ApiResponseUtil;
import com.finalBanking.demo.Service.TransferService;
import com.finalBanking.demo.mapper.TransactionMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping(value = "/api", produces = "application/json")
@RequiredArgsConstructor

public class TransferController {
    private final TransferService transferService;

    @PostMapping(value = "/transfer", consumes = "application/json")
    @PreAuthorize("hasAuthority('TRANSFER')")
    public ResponseEntity<ApiResponseEntityDto> transfer(@Valid @RequestBody TransferRequest req, Authentication auth) {
        // Calling the transferService to perform the actual transfer logic
        Transaction tx = transferService.transfer(
                req.fromAccountNumber(), req.toAccountNumber(), req.amount(), req.description(), auth.getName()
        );

        // Mapping the resulting Transaction to TransferResponse DTO
        TransferResponse transferResponse = TransactionMapper.toDto(tx);

        // Create the API response using the successResponse method
        ApiResponseEntityDto apiResponse = ApiResponseUtil.successResponse(transferResponse);

        // Returning the ApiResponseEntityDto in the response body
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping(value = "/withdraw", consumes = "application/json")
    @PreAuthorize("hasAuthority('WITHDRAW')")
    public ResponseEntity<ApiResponseEntityDto> withdraw(@Valid @RequestBody WithdrawRequest req, Authentication auth) {
        // Calling the withdrawService to perform the actual withdraw logic
        Transaction tx = transferService.withdraw(
                req.accountNumber(), req.amount(), req.description(), auth.getName()
        );

        // Mapping the resulting Transaction to TransferResponse DTO
        TransferResponse withdrawResponse = TransactionMapper.toDto(tx);

        // Create the API response using the successResponse method
        ApiResponseEntityDto apiResponse = ApiResponseUtil.successResponse(withdrawResponse);

        // Returning the ApiResponseEntityDto in the response body
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping(value= "/deposite", consumes = "application/json")
    @PreAuthorize("hasAuthority('DEPOSIT')")
    public ResponseEntity<ApiResponseEntityDto> deposit(@Valid @RequestBody DepositRequest req, Authentication auth) {
        // Calling the depositService to perform the actual deposit logic
        Transaction tx = transferService.deposit(
                req.accountNumber(), req.amount(), req.description(), auth.getName()
        );

        // Mapping the resulting Transaction to TransferResponse DTO
        TransferResponse depositResponse = TransactionMapper.toDto(tx);

        // Create the API response using the successResponse method
        ApiResponseEntityDto apiResponse = ApiResponseUtil.successResponse(depositResponse);

        // Returning the ApiResponseEntityDto in the response body
        return ResponseEntity.ok(apiResponse);
    }


}
