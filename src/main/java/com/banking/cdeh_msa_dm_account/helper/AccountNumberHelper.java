package com.banking.cdeh_msa_dm_account.helper;

import com.banking.cdeh_msa_dm_account.domain.enums.AccountType;
import com.banking.cdeh_msa_dm_account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccountNumberHelper {

    private final AccountRepository accountRepository;

    // Prefijos base para cada tipo de cuenta
    private static final String AHORROS_PREFIX = "478758";
    private static final String CORRIENTE_PREFIX = "225487";

    public Mono<String> generateAccountNumber(AccountType accountType) {
        String prefix = getPrefix(accountType);

        return accountRepository.countByAccountNumberStartingWith(prefix)
                .map(count -> {
                    // Incrementar el contador y generar el número con padding
                    long nextSequence = count + 1;
                    String sequenceStr = String.format("%04d", nextSequence); // 4 dígitos con ceros a la izquierda
                    String accountNumber = prefix + sequenceStr;

                    log.info("Generated account number: {} for type: {}", accountNumber, accountType);
                    return accountNumber;
                })
                .doOnError(error -> log.error("Error generating account number for type {}: {}", accountType, error.getMessage()));
    }

    private String getPrefix(AccountType accountType) {
        return switch (accountType) {
            case AHORRO -> AHORROS_PREFIX;
            case CORRIENTE -> CORRIENTE_PREFIX;
        };
    }
}
