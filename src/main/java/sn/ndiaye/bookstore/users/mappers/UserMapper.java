package sn.ndiaye.bookstore.users.mappers;

import org.mapstruct.Mapper;
import sn.ndiaye.bookstore.users.entities.User;
import sn.ndiaye.bookstore.users.dtos.RegisterUserRequest;
import sn.ndiaye.bookstore.users.dtos.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper{
    User toEntity(RegisterUserRequest request);
    UserDto toDto(User user);
}
