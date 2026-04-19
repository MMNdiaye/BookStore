package sn.ndiaye.bookstore.users;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserDto createUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new EmailAlreadyTakenException(request.getEmail());

        // Storing hashed password inside the database is more secure
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        var user = userMapper.toEntity(request);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUser(UUID id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(user);
    }
}
