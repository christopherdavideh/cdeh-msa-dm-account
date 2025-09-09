package com.banking.cdeh_msa_dm_account.service.dto;

import com.banking.cdeh_msa_dm_account.domain.enums.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountRequestDto {

    private UUID accountId;

    @Size(max = 10, message = "Account number must not exceed 10 characters")
    private String accountNumber; // Ahora es opcional, se genera automáticamente si no se proporciona

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @PositiveOrZero(message = "Initial balance must be zero or positive")
    @Builder.Default
    private BigDecimal initialBalance = BigDecimal.valueOf(0.00);

    private Boolean accountStatus;

    @NotNull(message = "Customer ID is required")
    private UUID customerId;
}
