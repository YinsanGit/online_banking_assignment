package com.finalBanking.demo.Controller;

import com.finalBanking.demo.Dto.TransferRequest;
import com.finalBanking.demo.Dto.TransferResponse;
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
@RequestMapping(value = "/api/transfer", produces = "application/json")
@RequiredArgsConstructor

public class TransferController {
    private final TransferService transferService;

    @PostMapping(consumes = "application/json")
//    @PreAuthorize("hasAuthority('TRANSFER_FUNDS')")
    public ResponseEntity<ApiResponseEntityDto> transfer(@Valid @RequestBody TransferRequest req, Authentication auth) {
        // Calling the transferService to perform the actual transfer logic
        Transaction tx = transferService.transfer(
                req.fromAccountNumber(), req.toAccountNumber(), req.amount(), req.description(), auth.getName()
        );

        // Mapping the resulting Transaction to TransferResponse DTO
        TransferResponse transferResponse = TransactionMapper.toDto(tx);

        // Create the API response using the successResponse methodz
        ApiResponseEntityDto apiResponse = ApiResponseUtil.successResponse(transferResponse);

        // Returning the ApiResponseEntityDto in the response body
        return ResponseEntity.ok(apiResponse);
    }

}
