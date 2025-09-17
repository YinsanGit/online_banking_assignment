package com.finalBanking.demo.Service;

import com.finalBanking.demo.Dto.AccountRequest;
import com.finalBanking.demo.Dto.AccountResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    AccountResponse createAccount(AccountRequest request, String createdByUser);
    AccountResponse getAccountById(Long id);
    List<AccountResponse> getAllAccounts();
    AccountResponse updateAccount(Long id, AccountRequest request, String updatedByUser);
    boolean deleteAccount(Long id);
}
