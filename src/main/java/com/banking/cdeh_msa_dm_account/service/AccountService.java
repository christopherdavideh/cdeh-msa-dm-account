package com.banking.cdeh_msa_dm_account.service;

import com.banking.cdeh_msa_dm_account.service.dto.AccountRequestDto;
import com.banking.cdeh_msa_dm_account.service.dto.AccountResponseDto;
import com.banking.cdeh_msa_dm_account.service.dto.UpdateBalanceDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AccountService {

    Mono<AccountResponseDto> createAccount(AccountRequestDto accountRequestDto);

    Mono<AccountResponseDto> getAccountById(UUID accountId);

    Mono<AccountResponseDto> getAccountByAccountNumber(String accountNumber);

    Flux<AccountResponseDto> getAllAccounts();

    Flux<AccountResponseDto> getAccountsByCustomerId(UUID customerId);

    Mono<AccountResponseDto> updateAccount(UUID accountId, AccountRequestDto accountRequestDto);

    Mono<AccountResponseDto> updateAccountBalance(String accountNumber, UpdateBalanceDto updateBalanceDto);

    Mono<Void> deleteAccount(UUID accountId);
}
