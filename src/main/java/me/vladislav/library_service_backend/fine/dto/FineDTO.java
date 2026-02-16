package me.vladislav.library_service_backend.fine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FineDTO {
    private Long id;
    private Long readerId;
    private Long issuanceId;

    private String description;
    private String dueDate;       // yyyy-MM-dd
    private String paymentDate;   // yyyy-MM-dd | null

    private String state;         // UNPAID | PAID
    private BigDecimal amount;

    private Boolean writtenOff;   // true, если CANCELLED
}
