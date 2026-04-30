package sn.ndiaye.bookstore.payments.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.ndiaye.bookstore.books.entities.Book;
import sn.ndiaye.bookstore.payments.entities.PaymentType;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private Book book;
    private PaymentType paymentType;
    private String operation_id;
    private BigDecimal unitCost;
    private Long quantity;


    public String getBookName() {
        return book.getTitle() + "-" + book.getAuthor();
    }

    public String getType() {
        return paymentType.name();
    }
}
