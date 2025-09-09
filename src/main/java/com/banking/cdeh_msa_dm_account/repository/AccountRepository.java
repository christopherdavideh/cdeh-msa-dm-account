package com.banking.cdeh_msa_dm_account.repository;

import com.banking.cdeh_msa_dm_account.domain.entity.Account;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

@Repository
public interface AccountRepository extends ReactiveCrudRepository<Account, UUID> {

    @Query("SELECT * FROM account WHERE account_number = :accountNumber")
    Mono<Account> findByAccountNumber(String accountNumber);

    @Query("SELECT * FROM account WHERE customer_id = :customerId")
    Flux<Account> findByCustomerId(UUID customerId);

    @Query("SELECT * FROM account WHERE account_status = true")
    Flux<Account> findByAccountStatusTrue();

    @Query("SELECT COUNT(*) FROM account WHERE account_number LIKE CONCAT(:prefix, '%')")
    Mono<Long> countByAccountNumberStartingWith(String prefix);

    @Modifying
    @Query("UPDATE account SET initial_balance = :initialBalance WHERE account_id = :accountId")
    Mono<Integer> updateInitialBalance(UUID accountId, BigDecimal initialBalance);

    @Modifying
    @Query("UPDATE account SET account_status = false WHERE account_id = :accountId")
    Mono<Integer> softDeleteAccount(UUID accountId);
}