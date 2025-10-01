package com.finalBanking.demo.Service.Impl;

import com.finalBanking.demo.Dto.AccountRequest;
import com.finalBanking.demo.Dto.AccountResponse;
import com.finalBanking.demo.Entity.Account;
import com.finalBanking.demo.Exception.AccountNumberGenerator;
import com.finalBanking.demo.Repository.AccountRepository;
import com.finalBanking.demo.Service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {


    private final AccountRepository accountRepository;

    @Override
    public AccountResponse createAccount(AccountRequest request, String createdByUser) {
        String accountNumber;
        do {
            accountNumber = AccountNumberGenerator.generateAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber));

        Account account = Account.builder()
                .accountHolderName(request.accountHolderName())
                .accountHolderEmail(request.accountHolderEmail())
                .accountHolderPhone(request.accountHolderPhone())
                .nationalId(request.nationalId())
                .accountType(request.accountType())
                .accountNumber(accountNumber)
                .balance(BigDecimal.ZERO)
                .isActive(true)
                .build();

        account = accountRepository.save(account);

        return new AccountResponse(
                account.getId(),
                account.getAccountHolderName(),
                account.getAccountHolderEmail(),
                account.getAccountHolderPhone(),
                account.getNationalId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getBalance(),
                account.getIsActive(),
                account.getCreatedAt()
        );

    }

    @Override
    public AccountResponse getAccountById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isPresent()) {
                Account a = account.get();
            return new AccountResponse(a.getId(), a.getAccountHolderName(), a.getAccountHolderEmail(),
                    a.getAccountHolderPhone(), a.getNationalId(), a.getAccountNumber(), a.getAccountType(),
                    a.getBalance(), a.getIsActive(), a.getCreatedAt());
        }
        return null; // Handle the case in the controller with a 404 response
    }

    @Override
    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(account -> new AccountResponse(account.getId(), account.getAccountHolderName(), account.getAccountHolderEmail(),
                        account.getAccountHolderPhone(), account.getNationalId(), account.getAccountNumber(), account.getAccountType(),
                        account.getBalance(), account.getIsActive(), account.getCreatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AccountResponse updateAccount(Long id, AccountRequest request, String updatedByUser) {
        Optional<Account> accountOpt = accountRepository.findById(id);
        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();
            account.setAccountHolderName(request.accountHolderName());
            account.setAccountHolderEmail(request.accountHolderEmail());
            account.setAccountHolderPhone(request.accountHolderPhone());
            account.setNationalId(request.nationalId());
            account.setAccountType(request.accountType());


            account = accountRepository.save(account);

            return new AccountResponse(account.getId(), account.getAccountHolderName(), account.getAccountHolderEmail(),
                    account.getAccountHolderPhone(), account.getNationalId(), account.getAccountNumber(),
                    account.getAccountType(), account.getBalance(), account.getIsActive(),
                    account.getCreatedAt());
        }
        return null;
    }

    @Override
    public boolean deleteAccount(Long id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
