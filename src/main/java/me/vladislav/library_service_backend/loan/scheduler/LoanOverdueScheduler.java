package me.vladislav.library_service_backend.loan.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.library_service_backend.fine.model.Fine;
import me.vladislav.library_service_backend.fine.repository.FineRepository;
import me.vladislav.library_service_backend.fine.service.FineService;
import me.vladislav.library_service_backend.loan.model.BookLoan;
import me.vladislav.library_service_backend.loan.model.LoanStatus;
import me.vladislav.library_service_backend.loan.repository.BookLoanRepository;
import me.vladislav.library_service_backend.notification.dto.FineCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j

@Component
public class LoanOverdueScheduler {
    private final BookLoanRepository bookLoanRepository;
    private final FineService fineService;
    private final ApplicationEventPublisher eventPublisher;
    private final FineRepository fineRepository;

    @Transactional
    @Scheduled(cron = "${library.scheduler.overdue-cron}")
    public void markOverdueLoans() {
        log.info("LoanOverdueScheduler started");

        LocalDateTime now = LocalDateTime.now();

        List<BookLoan> overdueLoans = bookLoanRepository.findAllByStatusInAndDueAtBefore(
                List.of(LoanStatus.ISSUED, LoanStatus.OVERDUE),
                now
        );

        for (BookLoan loan : overdueLoans) {
            if (loan.getStatus().equals(LoanStatus.ISSUED)) {
                loan.setStatus(LoanStatus.OVERDUE);
            }

            boolean isNewFine = fineService.createOrUpdateFine(loan);

            if (isNewFine) {
                BigDecimal fineAmount = fineRepository.findByBookLoan(loan)
                        .map(Fine::getAmount).orElse(BigDecimal.ZERO);

                eventPublisher.publishEvent(
                        new FineCreatedEvent(
                                loan.getId(),
                                loan.getUser().getEmail(),
                                loan.getBook().getTitle(),
                                fineAmount
                        )
                );
            }
        }

        log.info("LoanOverdueScheduler finished. Processed: {}", overdueLoans.size());
    }

}
