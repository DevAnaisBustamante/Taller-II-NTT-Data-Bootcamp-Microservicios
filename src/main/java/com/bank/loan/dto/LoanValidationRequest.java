package com.bank.loan.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoanValidationRequest {
    @NotNull
    private Double monthlySalary;

    @NotNull
    private Double requestedAmount;

    @NotNull
    private Integer termMonths;

    // nullable
    private LocalDate lastLoanDate;
}
