package com.bank.loan.service.Impl;


import com.bank.loan.dto.LoanValidationRequest;
import com.bank.loan.dto.LoanValidationResult;
import com.bank.loan.model.Reason;
import com.bank.loan.service.LoanValidationService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanValidationServiceImpl implements LoanValidationService {

    private final Clock clock;

    public LoanValidationServiceImpl(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Mono<LoanValidationResult> validate(LoanValidationRequest req) {
        return Mono.fromSupplier(() -> evaluate(req));
    }

    private LoanValidationResult evaluate(LoanValidationRequest req) {
        List<String> reasons = new ArrayList<>();

        // R4 - datos validos
        boolean datosInvalidos = req.getMonthlySalary() == null || req.getRequestedAmount() == null
                || req.getMonthlySalary() <= 0 || req.getRequestedAmount() <= 0;
        if (datosInvalidos) {
            reasons.add(Reason.DATOS_INVALIDOS.name());
        }

        // R2 - plazo
        if (req.getTermMonths() == null || req.getTermMonths() < 1 || req.getTermMonths() > 36) {
            reasons.add(Reason.PLAZO_MAXIMO_SUPERADO.name());
        }

        double monthlyPayment = 0.0;
        if (req.getTermMonths() != null && req.getTermMonths() > 0 && req.getRequestedAmount() != null) {
            monthlyPayment = req.getRequestedAmount() / req.getTermMonths();
        }

        // R3 - capacidad de pago
        if (!datosInvalidos && req.getMonthlySalary() != null && monthlyPayment > 0.4 * req.getMonthlySalary()) {
            reasons.add(Reason.CAPACIDAD_INSUFICIENTE.name());
        }

        // R1 - antigüedad
        if (req.getLastLoanDate() != null) {
            LocalDate today = LocalDate.now(clock);
            LocalDate since = today.minusMonths(3);
            // if lastLoanDate is on or after 'since', it's recent -> fail
            if (!req.getLastLoanDate().isBefore(since)) {
                reasons.add(Reason.HAS_RECENT_LOANS.name());
            }
        }

        boolean eligible = reasons.isEmpty();
        return new LoanValidationResult(eligible, reasons, monthlyPayment);
    }
}