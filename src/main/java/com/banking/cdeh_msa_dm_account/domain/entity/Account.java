package com.banking.cdeh_msa_dm_account.domain.entity;

import com.banking.cdeh_msa_dm_account.domain.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Table(name = "account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    private UUID accountId;
    private String accountNumber;
    private AccountType accountType;
    private BigDecimal initialBalance;
    private Boolean accountStatus;
    private UUID customerId;
}
