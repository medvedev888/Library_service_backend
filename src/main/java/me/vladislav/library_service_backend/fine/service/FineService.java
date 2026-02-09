package me.vladislav.library_service_backend.fine.service;


import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.fine.repository.FineRepository;
import me.vladislav.library_service_backend.loan.model.BookLoan;
import me.vladislav.library_service_backend.fine.model.Fine;
import me.vladislav.library_service_backend.fine.model.FineStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor

@Service
public class FineService {
    private final FineRepository fineRepository;

    @Value("${library.daily-fine}")
    private BigDecimal dailyFine;


    @Transactional
    public void createOrUpdateFine(BookLoan loan) {
        Fine fine = fineRepository
                .findByBookLoan(loan)
                .orElseGet(() -> createNewFine(loan));

        if (fine.getStatus() != FineStatus.UNPAID) {
            return;
        }

        BigDecimal amount = calculateFineAmount(loan);
        fine.setAmount(amount);
    }


    private Fine createNewFine(BookLoan loan) {
        Fine fine = Fine.builder()
                .bookLoan(loan)
                .description("Штраф за просрочку книги до " + loan.getDueAt().toLocalDate())
                .overdueFrom(loan.getDueAt())
                .status(FineStatus.UNPAID)
                .amount(BigDecimal.ZERO)
                .build();

        return fineRepository.save(fine);
    }


    private BigDecimal calculateFineAmount(BookLoan loan) {
        long daysOverdue = ChronoUnit.DAYS.between(
                loan.getDueAt().toLocalDate(),
                LocalDate.now()
        );

        if (daysOverdue <= 0) {
            return BigDecimal.ZERO;
        }

        return dailyFine.multiply(BigDecimal.valueOf(daysOverdue));
    }

}
