package sn.ndiaye.bookstore.users.services;

import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sn.ndiaye.bookstore.auth.services.AuthService;
import sn.ndiaye.bookstore.loans.entities.Loan;
import sn.ndiaye.bookstore.loans.exceptions.LoanNotFoundException;
import sn.ndiaye.bookstore.loans.services.LoanService;
import sn.ndiaye.bookstore.users.entities.User;
import sn.ndiaye.bookstore.users.mappers.UserMapper;
import sn.ndiaye.bookstore.users.exceptions.EmailAlreadyTakenException;
import sn.ndiaye.bookstore.users.dtos.RegisterUserRequest;
import sn.ndiaye.bookstore.users.exceptions.UserNotFoundException;
import sn.ndiaye.bookstore.users.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final AuthService authService;
    private final LoanService loanService;

    public User createUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new EmailAlreadyTakenException(request.getEmail());

        // Storing hashed password inside the database is more secure
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        var user = userMapper.toEntity(request);
        userRepository.save(user);
        return user;
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }


    public User getUser(UUID id) {
        var authUser = authService.getCurrentUser();
        if (!authUser.isAdmin() && !authUser.getId().equals(id))
            throw new AccessDeniedException("Denied access to this user");

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public Iterable<Loan> getLoans() {
        var user = authService.getCurrentUser();
        return loanService.getAllLoans(null, user.getId(), null);
    }

    public Loan getLoan(UUID loanId) {
        var user =authService.getCurrentUser();
        var loan = loanService.getLoan(loanId);
        if (!loan.getUser().equals(user))
            throw new LoanNotFoundException(loanId);
        return loan;
    }

    public BigDecimal endsLoan(UUID loanId) {
        var loan = getLoan(loanId);
        return loan.processReturnFee();
    }
}
