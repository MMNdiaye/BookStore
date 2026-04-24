package sn.ndiaye.bookstore.books.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import sn.ndiaye.bookstore.books.dtos.LoanDto;
import sn.ndiaye.bookstore.books.entities.Loan;

@Mapper(componentModel = "spring")
public interface LoanMapper {
    @Mapping(target = "userId", expression = "java(loan.getUser().getId())")
    @Mapping(target = "bookId", expression = "java(loan.getBook().getId())")
    @Mapping(target = "remainingDays", expression = "java(loan.getRemainingDays())")
    LoanDto toDto(Loan loan);
}
