package com.banking.cdeh_msa_dm_account.service.mapper;

import com.banking.cdeh_msa_dm_account.domain.entity.Account;
import com.banking.cdeh_msa_dm_account.service.dto.AccountRequestDto;
import com.banking.cdeh_msa_dm_account.service.dto.AccountResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account toAccount(AccountRequestDto accountDto);

    AccountResponseDto toAccountResponseDto(Account account);
}
