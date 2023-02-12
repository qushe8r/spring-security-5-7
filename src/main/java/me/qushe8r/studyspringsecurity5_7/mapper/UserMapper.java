package me.qushe8r.studyspringsecurity5_7.mapper;

import me.qushe8r.studyspringsecurity5_7.domain.Account;
import me.qushe8r.studyspringsecurity5_7.domain.AccountDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Account accountDtoToAccount(AccountDto accountDto);
}
