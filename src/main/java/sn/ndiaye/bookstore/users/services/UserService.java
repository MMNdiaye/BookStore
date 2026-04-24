package sn.ndiaye.bookstore.users.services;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sn.ndiaye.bookstore.users.entities.User;
import sn.ndiaye.bookstore.users.mappers.UserMapper;
import sn.ndiaye.bookstore.users.exceptions.EmailAlreadyTakenException;
import sn.ndiaye.bookstore.users.dtos.RegisterUserRequest;
import sn.ndiaye.bookstore.users.dtos.UserDto;
import sn.ndiaye.bookstore.users.exceptions.UserNotFoundException;
import sn.ndiaye.bookstore.users.repositories.UserRepository;

import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserDto createUser(RegisterUserRequest request) {
        var user = createUserEntity(request);
        return userMapper.toDto(user);
    }

    public User createUserEntity(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new EmailAlreadyTakenException(request.getEmail());

        // Storing hashed password inside the database is more secure
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        var user = userMapper.toEntity(request);
        userRepository.save(user);
        return user;
    }

    public Iterable<UserDto> getAllUsers() {
        var users = findAllUserEntities();
        return userMapper.toDtos(users);
    }

    public Iterable<User> findAllUserEntities() {
        return userRepository.findAll();
    }

    public UserDto getUser(UUID id) {
        var user = findUserEntity(id);
        return userMapper.toDto(user);
    }

    public User findUserEntity(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }
}
