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
    @DecimalMin(value = "0.01", inclusive = true)
    private Double monthlySalary;

    @NotNull
    @DecimalMin(value = "0.01", inclusive = true)
    private Double requestedAmount;

    @NotNull
    @Min(1)
    @Max(36)
    private Integer termMonths;

    // nullable
    private LocalDate lastLoanDate;
}
