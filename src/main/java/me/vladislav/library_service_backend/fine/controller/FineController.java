package me.vladislav.library_service_backend.fine.controller;

import lombok.RequiredArgsConstructor;
import me.vladislav.library_service_backend.auth.security.CustomUserDetails;
import me.vladislav.library_service_backend.common.dto.ApiResponse;
import me.vladislav.library_service_backend.fine.dto.FineDTO;
import me.vladislav.library_service_backend.fine.model.Fine;
import me.vladislav.library_service_backend.fine.model.FineStatus;
import me.vladislav.library_service_backend.fine.service.FineService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;

    @GetMapping("/my")
    public ApiResponse myFines(@AuthenticationPrincipal CustomUserDetails principal) {
        Long userId = principal.getUser().getId();

        List<FineDTO> list = fineService.listMyFines(userId).stream()
                .map(this::toDto)
                .sorted(Comparator.comparing(FineDTO::getDueDate, Comparator.nullsLast(String::compareTo)).reversed())
                .toList();

        return ApiResponse.success("Мои штрафы", list);
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @GetMapping
    public ApiResponse allFines(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String state
    ) {
        String qq = q == null ? "" : q.trim().toLowerCase();
        String st = state == null ? "" : state.trim().toUpperCase();

        List<FineDTO> list = fineService.listAllFines().stream()
                .map(this::toDto)
                .filter(dto -> st.isBlank() || dto.getState().equals(st))
                .filter(dto -> {
                    if (qq.isBlank()) return true;
                    String hay = (dto.getId() + " " + dto.getReaderId() + " " + dto.getIssuanceId() + " " + dto.getDescription())
                            .toLowerCase();
                    return hay.contains(qq);
                })
                .sorted(Comparator.comparing(FineDTO::getDueDate, Comparator.nullsLast(String::compareTo)).reversed())
                .toList();

        return ApiResponse.success("Штрафы", list);
    }

    @PostMapping("/{id}/pay")
    public ApiResponse pay(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails principal) {
        boolean isLibrarian = principal.getAuthorities().stream()
                .anyMatch(a -> "ROLE_LIBRARIAN".equals(a.getAuthority()));

        Fine fine = fineService.payFine(id, principal.getUser().getId(), isLibrarian);
        return ApiResponse.success("Штраф оплачен", toDto(fine));
    }

    @PreAuthorize("hasRole('LIBRARIAN')")
    @PostMapping("/{id}/write-off")
    public ApiResponse writeOff(@PathVariable Long id) {
        Fine fine = fineService.writeOffFine(id);
        return ApiResponse.success("Штраф списан", toDto(fine));
    }

    private FineDTO toDto(Fine fine) {
        Long readerId = fine.getBookLoan().getUser().getId();
        Long issuanceId = fine.getBookLoan().getId();

        String dueDate = fine.getOverdueFrom() != null ? fine.getOverdueFrom().toLocalDate().toString() : null;
        String paymentDate = fine.getPaidAt() != null ? fine.getPaidAt().toLocalDate().toString() : null;

        boolean writtenOff = fine.getStatus() == FineStatus.CANCELLED;
        String state = fine.getStatus() == FineStatus.UNPAID ? "UNPAID" : "PAID";

        return FineDTO.builder()
                .id(fine.getId())
                .readerId(readerId)
                .issuanceId(issuanceId)
                .description(fine.getDescription())
                .dueDate(dueDate)
                .paymentDate(paymentDate)
                .state(state)
                .amount(fine.getAmount())
                .writtenOff(writtenOff)
                .build();
    }
}
