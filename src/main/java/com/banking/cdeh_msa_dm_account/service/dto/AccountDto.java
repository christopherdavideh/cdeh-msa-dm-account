package com.banking.cdeh_msa_dm_account.service.dto;

import com.banking.cdeh_msa_dm_account.domain.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {

    private UUID accountId;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal initialBalance;
    private Boolean accountStatus;
    private UUID customerId;
}
