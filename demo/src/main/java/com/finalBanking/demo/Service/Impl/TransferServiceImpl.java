package com.finalBanking.demo.Service.Impl;

import com.finalBanking.demo.Entity.Account;
import com.finalBanking.demo.Entity.Transaction;
import com.finalBanking.demo.Entity.User;
import com.finalBanking.demo.Enumration.TransactionStatus;
import com.finalBanking.demo.Enumration.TransactionType;
import com.finalBanking.demo.Exception.AccountNotFoundException;
import com.finalBanking.demo.Exception.InsufficientFundsException;
import com.finalBanking.demo.Exception.LimitExceededException;
import com.finalBanking.demo.Repository.AccountRepository;
import com.finalBanking.demo.Repository.TransactionRepository;
import com.finalBanking.demo.Repository.userRepository;
import com.finalBanking.demo.Service.TransferService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final userRepository userRepository;

    @Value("${app.transfer.limit:1000000.00}")
    private BigDecimal transferLimit;

    @Override
    @Transactional
    public Transaction transfer(String fromAcc, String toAcc, BigDecimal amount, String description, String principal) {
        User processor = userRepository.findByUsername(principal)
                .or(() -> userRepository.findByEmail(principal))
                .orElseThrow(() -> new IllegalStateException("User not found: " + principal));

        // Create and save the transaction as PENDING initially
        Transaction tx = transactionRepository.save(Transaction.builder()
                .fromAccountNumber(fromAcc)
                .toAccountNumber(toAcc)
                .amount(amount)
                .type(TransactionType.TRANSFER)
                .description(description)
                .processedBy(processor)
                .status(TransactionStatus.PENDING)
                .build());

        try {
            if (fromAcc.equals(toAcc)) throw new IllegalArgumentException("from/to accounts must differ");
            if (amount == null || amount.signum() <= 0) throw new IllegalArgumentException("Amount must be positive");
            if (amount.compareTo(transferLimit) > 0) throw new LimitExceededException(amount, transferLimit);

            // lock both accounts (deterministic order)
            String first = fromAcc.compareTo(toAcc) < 0 ? fromAcc : toAcc;
            String second = fromAcc.compareTo(toAcc) < 0 ? toAcc : fromAcc;

            Account a1 = accountRepository.findByAccountNumberForUpdate(first)
                    .orElseThrow(() -> new AccountNotFoundException(first));
            Account a2 = accountRepository.findByAccountNumberForUpdate(second)
                    .orElseThrow(() -> new AccountNotFoundException(second));

            Account debit = a1.getAccountNumber().equals(fromAcc) ? a1 : a2;
            Account credit = (debit == a1) ? a2 : a1;

            if (Boolean.FALSE.equals(debit.getIsActive()) || Boolean.FALSE.equals(credit.getIsActive())) {
                throw new IllegalArgumentException("One or both accounts are inactive");
            }
            if (debit.getBalance().compareTo(amount) < 0) {
                throw new InsufficientFundsException(fromAcc, debit.getBalance(), amount);
            }

            debit.setBalance(debit.getBalance().subtract(amount));
            credit.setBalance(credit.getBalance().add(amount));
            accountRepository.save(debit);
            accountRepository.save(credit);

            tx.setStatus(TransactionStatus.COMPLETED);
            return transactionRepository.save(tx);

        } catch (RuntimeException e) {
            tx.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(tx);  // Ensure the transaction is saved in the FAILED state
            throw e;
        }
    }
}