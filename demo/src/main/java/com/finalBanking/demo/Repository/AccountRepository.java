package com.finalBanking.demo.Repository;

import com.finalBanking.demo.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    boolean existsByAccountNumber(String accountNumber);
    // If you already have AccountRepository, just ADD this method:
    @Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("select a from Account a where a.accountNumber = :acc")
    java.util.Optional<com.finalBanking.demo.Entity.Account>
    findByAccountNumberForUpdate(@org.springframework.data.repository.query.Param("acc") String accountNumber);

}
