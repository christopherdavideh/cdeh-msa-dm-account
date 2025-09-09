package com.banking.cdeh_msa_dm_account.domain.entity;

import com.banking.cdeh_msa_dm_account.domain.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Table("account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @Column("account_id")
    private UUID accountId;

    @Column("account_number")
    private String accountNumber;

    @Column("account_type")
    private AccountType accountType;

    @Column("initial_balance")
    private BigDecimal initialBalance;

    @Column("account_status")
    private Boolean accountStatus;

    @Column("customer_id")
    private UUID customerId;
}
