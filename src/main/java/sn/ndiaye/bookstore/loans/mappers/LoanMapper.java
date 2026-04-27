package sn.ndiaye.bookstore.loans.mappers;

import org.mapstruct.*;
import sn.ndiaye.bookstore.loans.dtos.UpdateLoanRequest;
import sn.ndiaye.bookstore.loans.entities.Loan;
import sn.ndiaye.bookstore.loans.dtos.LoanDto;
import sn.ndiaye.bookstore.users.dtos.UserLoanDto;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    @Mapping(target = "userId", expression = "java(loan.getUser().getId())")
    @Mapping(target = "bookId", expression = "java(loan.getBook().getId())")
    @Mapping(target = "remainingDays", expression = "java(loan.getRemainingDays())")
    LoanDto toDto(Loan loan);

    @Mapping(target = "bookId", expression = "java(loan.getBook().getId())")
    @Mapping(target = "remainingDays", expression = "java(loan.getRemainingDays())")
    UserLoanDto toUserLoanDto(Loan loan);

    Iterable<LoanDto> toDtos(Iterable<Loan> loans);

    Iterable<UserLoanDto> toUserLoanDtos(Iterable<Loan> loans);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Loan loan, UpdateLoanRequest request);
}
