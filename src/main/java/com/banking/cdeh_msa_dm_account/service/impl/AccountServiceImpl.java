package com.banking.cdeh_msa_dm_account.service.impl;

import com.banking.cdeh_msa_dm_account.exception.ResourceNotFoundException;
import com.banking.cdeh_msa_dm_account.repository.AccountRepository;
import com.banking.cdeh_msa_dm_account.service.AccountService;
import com.banking.cdeh_msa_dm_account.service.dto.AccountRequestDto;
import com.banking.cdeh_msa_dm_account.service.dto.AccountResponseDto;
import com.banking.cdeh_msa_dm_account.service.dto.UpdateBalanceDto;
import com.banking.cdeh_msa_dm_account.service.mapper.AccountMapper;
import com.banking.cdeh_msa_dm_account.util.LogMessages;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AccountServiceImpl implements AccountService {
    
    AccountRepository accountRepository;
    AccountMapper accountMapper;

    @Override
    public Mono<AccountResponseDto> createAccount(AccountRequestDto accountRequestDto) {
        return accountRepository.save(accountMapper.toAccount(accountRequestDto))
                .map(accountMapper::toAccountResponseDto)
                .doFirst(() -> log.info(LogMessages.CREATING_ACCOUNT_REQUEST, accountRequestDto.getAccountNumber()))
                .doOnSuccess(account -> log.info(LogMessages.ACCOUNT_CREATED_SUCCESS, account.getAccountId()))
                .doOnError(error -> log.error(LogMessages.ERROR_CREATING_ACCOUNT, error.getMessage()));
    }

    @Override
    public Mono<AccountResponseDto> getAccountById(UUID accountId) {
        return accountRepository.findById(accountId)
                .map(accountMapper::toAccountResponseDto)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(LogMessages.ACCOUNT_NOT_FOUND_BY_ID + accountId)))
                .doFirst(() -> log.info(LogMessages.GETTING_ACCOUNT_BY_ID_REQUEST, accountId))
                .doOnSuccess(account -> log.info(LogMessages.ACCOUNT_FOUND_BY_ID, accountId))
                .doOnError(error -> log.error(LogMessages.ERROR_GETTING_ACCOUNT_BY_ID, accountId, error.getMessage()));
    }

    @Override
    public Mono<AccountResponseDto> getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(accountMapper::toAccountResponseDto)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(LogMessages.ACCOUNT_NOT_FOUND_BY_NUMBER + accountNumber)))
                .doFirst(() -> log.info(LogMessages.GETTING_ACCOUNT_BY_NUMBER_REQUEST, accountNumber))
                .doOnSuccess(account -> log.info(LogMessages.ACCOUNT_FOUND_BY_NUMBER, accountNumber))
                .doOnError(error -> log.error(LogMessages.ERROR_GETTING_ACCOUNT_BY_NUMBER, accountNumber, error.getMessage()));
    }

    @Override
    public Flux<AccountResponseDto> getAllAccounts() {
        return accountRepository.findByAccountStatusTrue()
                .map(accountMapper::toAccountResponseDto)
                .doFirst(() -> log.info(LogMessages.GETTING_ALL_ACCOUNTS_REQUEST))
                .doOnComplete(() -> log.info(LogMessages.ALL_ACCOUNTS_RETRIEVED))
                .doOnError(error -> log.error(LogMessages.ERROR_GETTING_ALL_ACCOUNTS, error.getMessage()));
    }

    @Override
    public Flux<AccountResponseDto> getAllAccountActive() {
        return accountRepository.getAllAccountActive()
                .map(accountMapper::toAccountResponseDto)
                .doFirst(() -> log.info(LogMessages.GETTING_ALL_ACCOUNT_ACTIVE_REQUEST))
                .doOnComplete(() -> log.info(LogMessages.ALL_ACCOUNT_ACTIVE_RETRIEVED))
                .doOnError(error -> log.error(LogMessages.ERROR_GETTING_ALL_ACCOUNT_ACTIVE, error.getMessage()));
    }

    @Override
    public Flux<AccountResponseDto> getAccountsByCustomerId(UUID customerId) {
        return accountRepository.findByCustomerId(customerId)
                .map(accountMapper::toAccountResponseDto)
                .doFirst(() -> log.info(LogMessages.GETTING_ACCOUNTS_BY_CUSTOMER_REQUEST, customerId))
                .doOnComplete(() -> log.info(LogMessages.ACCOUNTS_BY_CUSTOMER_RETRIEVED, customerId))
                .doOnError(error -> log.error(LogMessages.ERROR_GETTING_ACCOUNTS_BY_CUSTOMER, customerId, error.getMessage()));
    }

    @Override
    public Mono<AccountResponseDto> updateAccount(UUID accountId, AccountRequestDto accountRequestDto) {
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(LogMessages.ACCOUNT_NOT_FOUND_BY_ID + accountId)))
                .map(existingAccount -> {
                    existingAccount.setAccountNumber(accountRequestDto.getAccountNumber());
                    existingAccount.setAccountType(accountRequestDto.getAccountType());
                    existingAccount.setInitialBalance(accountRequestDto.getInitialBalance());
                    existingAccount.setCustomerId(accountRequestDto.getCustomerId());
                    return existingAccount;
                })
                .flatMap(accountRepository::save)
                .map(accountMapper::toAccountResponseDto)
                .doFirst(() -> log.info(LogMessages.UPDATING_ACCOUNT_REQUEST, accountId))
                .doOnSuccess(account -> log.info(LogMessages.ACCOUNT_UPDATED_SUCCESS, accountId))
                .doOnError(error -> log.error(LogMessages.ERROR_UPDATING_ACCOUNT, accountId, error.getMessage()));
    }

    @Override
    public Mono<AccountResponseDto> updateAccountBalance(UUID accountId, UpdateBalanceDto updateBalanceDto) {
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(LogMessages.ACCOUNT_NOT_FOUND_BY_ID + accountId)))
                .flatMap(account -> accountRepository.updateInitialBalance(accountId, updateBalanceDto.getInitialBalance())
                        .then(Mono.just(account)))
                .map(account -> {
                    account.setInitialBalance(updateBalanceDto.getInitialBalance());
                    return account;
                })
                .map(accountMapper::toAccountResponseDto)
                .doFirst(() -> log.info(LogMessages.UPDATING_BALANCE_REQUEST, accountId))
                .doOnSuccess(account -> log.info(LogMessages.ACCOUNT_BALANCE_UPDATED_SUCCESS, accountId))
                .doOnError(error -> log.error(LogMessages.ERROR_UPDATING_BALANCE, accountId, error.getMessage()));
    }

    @Override
    public Mono<Void> deleteAccount(UUID accountId) {
        return accountRepository.findById(accountId)
                .doFirst(() -> log.info(LogMessages.DELETING_ACCOUNT_REQUEST, accountId))
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(LogMessages.ACCOUNT_NOT_FOUND_BY_ID + accountId)))
                .flatMap(account -> accountRepository.softDeleteAccount(accountId))
                .then()
                .doOnSuccess(v -> log.info(LogMessages.ACCOUNT_DELETED_SUCCESS, accountId))
                .doOnError(error -> log.error(LogMessages.ERROR_DELETING_ACCOUNT, accountId, error.getMessage()));
    }
}

