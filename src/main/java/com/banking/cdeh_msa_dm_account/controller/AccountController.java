package com.banking.cdeh_msa_dm_account.controller;

import com.banking.cdeh_msa_dm_account.service.AccountService;
import com.banking.cdeh_msa_dm_account.service.dto.AccountRequestDto;
import com.banking.cdeh_msa_dm_account.service.dto.AccountResponseDto;
import com.banking.cdeh_msa_dm_account.service.dto.UpdateBalanceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public Mono<ResponseEntity<AccountResponseDto>> createAccount(@Valid @RequestBody AccountRequestDto accountRequestDto) {
        return accountService.createAccount(accountRequestDto)
                .map(account -> ResponseEntity.status(HttpStatus.CREATED).body(account));
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<AccountResponseDto>>> getAllAccounts() {
        return Mono.just(ResponseEntity.ok(accountService.getAllAccounts()));
    }

    @GetMapping("/{accountId}")
    public Mono<ResponseEntity<AccountResponseDto>> getAccountById(@PathVariable UUID accountId) {
        return accountService.getAccountById(accountId)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/numero/{accountNumber}")
    public Mono<ResponseEntity<AccountResponseDto>> getAccountByNumber(@PathVariable String accountNumber) {
        return accountService.getAccountByAccountNumber(accountNumber)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/cliente/{customerId}")
    public Mono<ResponseEntity<Flux<AccountResponseDto>>> getAccountsByCustomerId(@PathVariable UUID customerId) {
        return Mono.just(ResponseEntity.ok(accountService.getAccountsByCustomerId(customerId)));
    }

    @PutMapping("/{accountId}")
    public Mono<ResponseEntity<AccountResponseDto>> updateAccount(@PathVariable UUID accountId,
                                                                  @Valid @RequestBody AccountRequestDto accountRequestDto) {
        return accountService.updateAccount(accountId, accountRequestDto)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/{accountId}/balance")
    public Mono<ResponseEntity<AccountResponseDto>> updateAccountBalance(@PathVariable UUID accountId,
                                                                         @Valid @RequestBody UpdateBalanceDto updateBalanceDto) {
        return accountService.updateAccountBalance(accountId, updateBalanceDto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{accountId}")
    public Mono<ResponseEntity<Void>> deleteAccount(@PathVariable UUID accountId) {
        return accountService.deleteAccount(accountId)
                .map(v -> ResponseEntity.noContent().build());
    }
}
