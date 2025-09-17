package com.finalBanking.demo.Service;

import com.finalBanking.demo.Entity.Transaction;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface TransferService {
    Transaction transfer(String fromAcc, String toAcc, BigDecimal amount, String description, String principal);
}
