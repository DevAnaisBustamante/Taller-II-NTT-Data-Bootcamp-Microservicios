package com.bank.loan.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;


@Data
public class LoanValidationRequest {
    private BigDecimal monthlySalary;   // > 0
    private BigDecimal requestedAmount; // > 0
    private Integer termMonths;         // 1..36
    private LocalDate lastLoanDate;
}
