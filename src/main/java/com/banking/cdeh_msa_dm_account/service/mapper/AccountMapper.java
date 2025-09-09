package com.banking.cdeh_msa_dm_account.service.mapper;

import com.banking.cdeh_msa_dm_account.domain.entity.Account;
import com.banking.cdeh_msa_dm_account.service.dto.AccountRequestDto;
import com.banking.cdeh_msa_dm_account.service.dto.AccountResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "accountStatus", constant = "true")
    Account toAccount(AccountRequestDto dto);

    AccountResponseDto toAccountResponseDto(Account entity);
}