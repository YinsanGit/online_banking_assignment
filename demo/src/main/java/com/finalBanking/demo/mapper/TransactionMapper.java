package com.finalBanking.demo.mapper;

import com.finalBanking.demo.Dto.TransferResponse;
import com.finalBanking.demo.Entity.Transaction;

public final class TransactionMapper {
    private TransactionMapper() {}
    public static TransferResponse toDto(Transaction tx) {
        return new TransferResponse(
                tx.getId(),
                tx.getFromAccountNumber(),
                tx.getToAccountNumber(),
                tx.getAmount(),
                tx.getStatus(),
                tx.getProcessedBy() != null ? tx.getProcessedBy().getUsername() : null,
                tx.getTimestamp(),
                tx.getStatus().name()
        );
    }
}
