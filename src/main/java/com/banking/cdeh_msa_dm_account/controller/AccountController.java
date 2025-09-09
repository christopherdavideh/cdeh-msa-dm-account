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
        log.info("POST /cuentas - Creating new account");
        return accountService.createAccount(accountRequestDto)
                .map(account -> ResponseEntity.status(HttpStatus.CREATED).body(account));
    }

    @GetMapping
    public Mono<ResponseEntity<Flux<AccountResponseDto>>> getAllAccounts() {
        log.info("GET /cuentas - Getting all accounts");
        return Mono.just(ResponseEntity.ok(accountService.getAllAccounts()));
    }

    @GetMapping("/activas")
    public Mono<ResponseEntity<Flux<AccountResponseDto>>> getAllAccountActive() {
        log.info("GET /cuentas/activas - Getting all active accounts using custom query");
        return Mono.just(ResponseEntity.ok(accountService.getAllAccountActive()));
    }

    @GetMapping("/{accountId}")
    public Mono<ResponseEntity<AccountResponseDto>> getAccountById(@PathVariable UUID accountId) {
        log.info("GET /cuentas/{} - Getting account by ID", accountId);
        return accountService.getAccountById(accountId)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/numero/{accountNumber}")
    public Mono<ResponseEntity<AccountResponseDto>> getAccountByNumber(@PathVariable String accountNumber) {
        log.info("GET /cuentas/numero/{} - Getting account by number", accountNumber);
        return accountService.getAccountByAccountNumber(accountNumber)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/cliente/{customerId}")
    public Mono<ResponseEntity<Flux<AccountResponseDto>>> getAccountsByCustomerId(@PathVariable UUID customerId) {
        log.info("GET /cuentas/cliente/{} - Getting accounts by customer ID", customerId);
        return Mono.just(ResponseEntity.ok(accountService.getAccountsByCustomerId(customerId)));
    }

    @PutMapping("/{accountId}")
    public Mono<ResponseEntity<AccountResponseDto>> updateAccount(@PathVariable UUID accountId,
                                                                  @Valid @RequestBody AccountRequestDto accountRequestDto) {
        log.info("PUT /cuentas/{} - Updating account", accountId);
        return accountService.updateAccount(accountId, accountRequestDto)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/{accountId}/balance")
    public Mono<ResponseEntity<AccountResponseDto>> updateAccountBalance(@PathVariable UUID accountId,
                                                                         @Valid @RequestBody UpdateBalanceDto updateBalanceDto) {
        log.info("PATCH /cuentas/{}/balance - Updating account balance", accountId);
        return accountService.updateAccountBalance(accountId, updateBalanceDto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{accountId}")
    public Mono<ResponseEntity<Void>> deleteAccount(@PathVariable UUID accountId) {
        log.info("DELETE /cuentas/{} - Soft deleting account", accountId);
        return accountService.deleteAccount(accountId)
                .map(v -> ResponseEntity.noContent().build());
    }
}
