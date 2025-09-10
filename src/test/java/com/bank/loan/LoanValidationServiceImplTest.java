package com.bank.loan;

import com.bank.loan.dto.LoanValidationRequest;
import com.bank.loan.service.Impl.LoanValidationServiceImpl;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;

class LoanValidationServiceImplTest {

    private final Clock fixedClock = Clock.fixed(LocalDate.of(2025, 9, 10).atStartOfDay(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
    private final LoanValidationServiceImpl service = new LoanValidationServiceImpl(fixedClock);

    @Test
    void shouldBeEligible_whenAllRulesPass() {
        LoanValidationRequest req = new LoanValidationRequest(2500.0, 6000.0, 24, LocalDate.of(2025, 4, 1));
        StepVerifier.create(service.validate(req))
                .assertNext(result -> {
                    // monthlyPayment = 6000 / 24 = 250
                    assert result.isEligible();
                    assert result.getReasons().isEmpty();
                    assert Double.compare(result.getMonthlyPayment(), 250.0) == 0;
                })
                .verifyComplete();
    }

    @Test
    void shouldFail_hasRecentLoan_and_capacity() {
        // lastLoanDate is recent relative to fixedClock(2025-09-10) => since = 2025-06-10
        LoanValidationRequest req = new LoanValidationRequest(1000.0, 3600.0, 12, LocalDate.of(2025, 7, 1));
        StepVerifier.create(service.validate(req))
                .assertNext(result -> {
                    assert !result.isEligible();
                    // Expected reasons contain HAS_RECENT_LOANS and CAPACIDAD_INSUFICIENTE
                    assert result.getReasons().contains("HAS_RECENT_LOANS");
                    assert result.getReasons().contains("CAPACIDAD_INSUFICIENTE");
                })
                .verifyComplete();
    }

    @Test
    void shouldMark_invalid_when_negative_values() {
        LoanValidationRequest req = new LoanValidationRequest(-1.0, -100.0, 0, null);
        StepVerifier.create(service.validate(req))
                .assertNext(result -> {
                    assert !result.isEligible();
                    assert result.getReasons().contains("DATOS_INVALIDOS");
                    assert result.getReasons().contains("PLAZO_MAXIMO_SUPERADO");
                })
                .verifyComplete();
    }
}