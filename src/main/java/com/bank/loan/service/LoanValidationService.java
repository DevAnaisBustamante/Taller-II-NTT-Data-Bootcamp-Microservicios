package com.bank.loan.service;

import com.bank.loan.model.dto.LoanValidationRequest;
import com.bank.loan.model.dto.LoanValidationResult;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LoanValidationService {

    private final Clock clock;

    private enum Reason {
        HAS_RECENT_LOANS,
        PLAZO_MAXIMO_SUPERADO,
        CAPACIDAD_INSUFICIENTE,
        DATOS_INVALIDOS
    }

    public Mono<LoanValidationResult> validate(LoanValidationRequest req) {
        final List<String> reasons = new ArrayList<>();

        // Null-safe (defensive)
        BigDecimal monthlySalary = nz(req.getMonthlySalary());
        BigDecimal requestedAmount = nz(req.getRequestedAmount());
        int termMonths = req.getTermMonths() == null ? 0 : req.getTermMonths();

        // R4: Valid data
        if (monthlySalary.compareTo(BigDecimal.ZERO) <= 0
                || requestedAmount.compareTo(BigDecimal.ZERO) <= 0) {
            reasons.add(Reason.DATOS_INVALIDOS.name());
        }

        // R2: Term range
        if (termMonths < 1 || termMonths > 36) {
            reasons.add(Reason.PLAZO_MAXIMO_SUPERADO.name());
        }

        // monthlyPayment (avoid division by zero)
        BigDecimal monthlyPayment = (termMonths > 0)
                ? requestedAmount.divide(BigDecimal.valueOf(termMonths), 8, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        // R3: Capacity
        BigDecimal threshold = monthlySalary.multiply(new BigDecimal("0.40"));
        if (monthlyPayment.compareTo(threshold) > 0) {
            reasons.add(Reason.CAPACIDAD_INSUFICIENTE.name());
        }

        // R1: Recency (inclusive: lastLoanDate <= today-3m fails)
        if (req.getLastLoanDate() != null) {
            LocalDate today = LocalDate.now(clock);
            LocalDate limit = today.minusMonths(3);
            if (!req.getLastLoanDate().isAfter(limit)) {
                reasons.add(Reason.HAS_RECENT_LOANS.name());
            }
        }

        boolean eligible = reasons.isEmpty();

        return Mono.just(
                LoanValidationResult.builder()
                        .eligible(eligible)
                        .reasons(reasons)
                        .monthlyPayment(monthlyPayment)
                        .build()
        );
    }

    private static BigDecimal nz(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }
}
