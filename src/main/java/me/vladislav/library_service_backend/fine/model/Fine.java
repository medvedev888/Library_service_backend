package me.vladislav.library_service_backend.fine.model;


import jakarta.persistence.*;
import lombok.*;
import me.vladislav.library_service_backend.loan.model.BookLoan;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "fines")
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(
            name = "book_loan_id",
            nullable = false,
            unique = true
    )
    private BookLoan bookLoan;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDateTime overdueFrom;

    private LocalDateTime paidAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FineStatus status;

    private String description;
}
