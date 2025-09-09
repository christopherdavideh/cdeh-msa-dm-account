package com.banking.cdeh_msa_dm_account.service.impl;

import com.banking.cdeh_msa_dm_account.exception.BadRequestException;
import com.banking.cdeh_msa_dm_account.exception.ResourceNotFoundException;
import com.banking.cdeh_msa_dm_account.helper.AccountNumberHelper;
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
    AccountNumberHelper accountNumberHelper;

    @Override
    public Mono<AccountResponseDto> createAccount(AccountRequestDto accountRequestDto) {
        return Mono.just(accountRequestDto)
                .flatMap(newAccountNumber -> {
                    if (newAccountNumber.getAccountNumber() == null || newAccountNumber.getAccountNumber().trim().isEmpty()) {
                        return accountNumberHelper.generateAccountNumber(newAccountNumber.getAccountType())
                                .map(generatedNumber -> {
                                    newAccountNumber.setAccountNumber(generatedNumber);
                                    return newAccountNumber;
                                });
                    }
                    return Mono.just(newAccountNumber);
                })
                .flatMap(accountData -> accountRepository.findByAccountNumber(accountData.getAccountNumber())
                        .hasElement()
                        .flatMap(exists -> {
                            if (exists) {
                                return Mono.error(new BadRequestException("Account number already exists: " + accountData.getAccountNumber()));
                            }
                            return accountRepository.save(accountMapper.toAccount(accountData));
                        }))
                .map(accountMapper::toAccountResponseDto)
                .doFirst(() -> log.info(LogMessages.CREATING_ACCOUNT_REQUEST,
                    accountRequestDto.getAccountNumber() != null ? accountRequestDto.getAccountNumber() : "auto-generated"))
                .doOnSuccess(account -> log.info(LogMessages.ACCOUNT_CREATED_SUCCESS, account.getAccountId()))
                .doOnError(error -> log.error(LogMessages.ERROR_CREATING_ACCOUNT, error.getMessage()))
                .onErrorResume(throwable -> {
                    if (accountRequestDto.getCustomerId() == null) {
                        return Mono.error(new BadRequestException("Customer ID is required"));
                    }
                    return Mono.error(throwable);
                });
    }

    @Override
    public Mono<AccountResponseDto> getAccountById(UUID accountId) {
        if (accountId == null) {
            return Mono.error(new BadRequestException("Account ID cannot be null"));
        }
        
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(LogMessages.ACCOUNT_NOT_FOUND_BY_ID + accountId)))
                .map(accountMapper::toAccountResponseDto)
                .doFirst(() -> log.info(LogMessages.GETTING_ACCOUNT_BY_ID_REQUEST, accountId))
                .doOnSuccess(account -> log.info(LogMessages.ACCOUNT_FOUND_BY_ID, accountId))
                .doOnError(error -> log.error(LogMessages.ERROR_GETTING_ACCOUNT_BY_ID, accountId, error.getMessage()));
    }

    @Override
    public Mono<AccountResponseDto> getAccountByAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return Mono.error(new BadRequestException("Account number cannot be null or empty"));
        }
        
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(LogMessages.ACCOUNT_NOT_FOUND_BY_NUMBER + accountNumber)))
                .map(accountMapper::toAccountResponseDto)
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
    public Flux<AccountResponseDto> getAccountsByCustomerId(UUID customerId) {
        if (customerId == null) {
            return Flux.error(new BadRequestException("Customer ID cannot be null"));
        }
        
        return accountRepository.findByCustomerId(customerId)
                .map(accountMapper::toAccountResponseDto)
                .doFirst(() -> log.info(LogMessages.GETTING_ACCOUNTS_BY_CUSTOMER_REQUEST, customerId))
                .doOnComplete(() -> log.info(LogMessages.ACCOUNTS_BY_CUSTOMER_RETRIEVED, customerId))
                .doOnError(error -> log.error(LogMessages.ERROR_GETTING_ACCOUNTS_BY_CUSTOMER, customerId, error.getMessage()));
    }

    @Override
    public Mono<AccountResponseDto> updateAccount(UUID accountId, AccountRequestDto accountRequestDto) {
        if (accountId == null) {
            return Mono.error(new BadRequestException("Account ID cannot be null"));
        }
        if (accountRequestDto.getAccountNumber() == null || accountRequestDto.getAccountNumber().trim().isEmpty()) {
            return Mono.error(new BadRequestException("Account number is required for updates"));
        }
        
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(LogMessages.ACCOUNT_NOT_FOUND_BY_ID + accountId)))
                .flatMap(existingAccount -> {
                    // Verificar duplicado solo si cambió el número
                    if (!existingAccount.getAccountNumber().equals(accountRequestDto.getAccountNumber())) {
                        return accountRepository.findByAccountNumber(accountRequestDto.getAccountNumber())
                                .hasElement()
                                .flatMap(exists -> {
                                    if (exists) {
                                        return Mono.error(new BadRequestException("Account number already exists: " + accountRequestDto.getAccountNumber()));
                                    }
                                    return Mono.just(existingAccount);
                                });
                    }
                    return Mono.just(existingAccount);
                })
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
                .doOnError(error -> log.error(LogMessages.ERROR_UPDATING_BALANCE, accountId, error.getMessage()))
                .onErrorResume(throwable -> {
                    if (accountId == null) {
                        return Mono.error(new BadRequestException("Account ID cannot be null"));
                    }
                    if (updateBalanceDto.getInitialBalance() == null) {
                        return Mono.error(new BadRequestException("Initial balance cannot be null"));
                    }
                    if (updateBalanceDto.getInitialBalance().compareTo(java.math.BigDecimal.ZERO) < 0) {
                        return Mono.error(new BadRequestException("Initial balance cannot be negative"));
                    }
                    return Mono.error(throwable);
                });
    }

    @Override
    public Mono<Void> deleteAccount(UUID accountId) {
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(LogMessages.ACCOUNT_NOT_FOUND_BY_ID + accountId)))
                .flatMap(account -> accountRepository.softDeleteAccount(accountId))
                .then()
                .doFirst(() -> log.info(LogMessages.DELETING_ACCOUNT_REQUEST, accountId))
                .doOnSuccess(v -> log.info(LogMessages.ACCOUNT_DELETED_SUCCESS, accountId))
                .doOnError(error -> log.error(LogMessages.ERROR_DELETING_ACCOUNT, accountId, error.getMessage()))
                .onErrorResume(throwable -> {
                    if (accountId == null) {
                        return Mono.error(new BadRequestException("Account ID cannot be null"));
                    }
                    return Mono.error(throwable);
                });
    }
}
