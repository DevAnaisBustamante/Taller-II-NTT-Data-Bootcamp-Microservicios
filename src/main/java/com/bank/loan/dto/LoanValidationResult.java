package com.bank.loan.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanValidationResult {
    private boolean eligible;
    private List<String> reasons;
    private double monthlyPayment;
}
