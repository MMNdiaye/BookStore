package sn.ndiaye.bookstore.users;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper{
    User toEntity(RegisterUserRequest request);
    UserDto toDto(User user);
}
