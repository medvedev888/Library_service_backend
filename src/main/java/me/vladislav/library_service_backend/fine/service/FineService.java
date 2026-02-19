package me.vladislav.library_service_backend.fine.service;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.fine.exception.FineAccessDeniedException;
import me.vladislav.library_service_backend.fine.exception.FineNotFoundException;
import me.vladislav.library_service_backend.fine.model.Fine;
import me.vladislav.library_service_backend.fine.model.FineStatus;
import me.vladislav.library_service_backend.fine.repository.FineRepository;
import me.vladislav.library_service_backend.loan.model.BookLoan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FineService {
    private final FineRepository fineRepository;

    @Value("${library.daily-fine}")
    private BigDecimal dailyFine;

    @Transactional
    public boolean createOrUpdateFine(BookLoan loan) {
        Fine fine = fineRepository
                .findByBookLoan(loan)
                .orElseGet(() -> createNewFine(loan));

        if (fine.getStatus() != FineStatus.UNPAID) {
            return false;
        }

        BigDecimal amount = calculateFineAmount(loan);
        fine.setAmount(amount);
        fineRepository.save(fine);

        return fine.getAmount().compareTo(BigDecimal.ZERO) > 0;
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


    @Transactional(readOnly = true)
    public List<Fine> listMyFines(Long userId) {
        return fineRepository.findAllByUserId(userId);
    }


    @Transactional(readOnly = true)
    public List<Fine> listAllFines() {
        return fineRepository.findAll();
    }


    @Transactional
    public Fine payFine(Long fineId, Long requesterUserId, boolean requesterIsLibrarian) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new FineNotFoundException(fineId));

        Long ownerId = fine.getBookLoan().getUser().getId();
        if (!requesterIsLibrarian && !ownerId.equals(requesterUserId)) {
            throw new FineAccessDeniedException();
        }

        if (fine.getStatus() == FineStatus.PAID) {
            return fine;
        }

        if (fine.getStatus() == FineStatus.CANCELLED) {
            return fine;
        }

        fine.setStatus(FineStatus.PAID);
        fine.setPaidAt(LocalDateTime.now());
        return fineRepository.save(fine);
    }

    @Transactional
    public Fine writeOffFine(Long fineId) {
        Fine fine = fineRepository.findById(fineId)
                .orElseThrow(() -> new FineNotFoundException(fineId));

        if (fine.getStatus() == FineStatus.CANCELLED) {
            return fine;
        }

        fine.setStatus(FineStatus.CANCELLED);
        fine.setPaidAt(LocalDateTime.now());
        return fineRepository.save(fine);
    }

}
