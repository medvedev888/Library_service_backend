package me.vladislav.library_service_backend.loan.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.vladislav.library_service_backend.fine.service.FineService;
import me.vladislav.library_service_backend.loan.model.BookLoan;
import me.vladislav.library_service_backend.loan.model.LoanStatus;
import me.vladislav.library_service_backend.loan.repository.BookLoanRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Slf4j

@Component
public class LoanOverdueScheduler {
    private final BookLoanRepository bookLoanRepository;
    private final FineService fineService;

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
            if(loan.getStatus().equals(LoanStatus.ISSUED)){
                loan.setStatus(LoanStatus.OVERDUE);
            }
            fineService.createOrUpdateFine(loan);
        }

        log.info("LoanOverdueScheduler finished. Processed: {}", overdueLoans.size());
    }

}
