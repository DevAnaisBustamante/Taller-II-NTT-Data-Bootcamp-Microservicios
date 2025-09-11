package com.bank.loan;

import com.bank.loan.dto.LoanValidationRequest;
import com.bank.loan.dto.LoanValidationResult;
import com.bank.loan.service.Impl.LoanValidationServiceImpl;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LoanValidationServiceImplTest {

    // Reloj fijo para controlar "hoy" en los tests
    private final Clock fixedClock = Clock.fixed(
            Instant.parse("2025-09-10T00:00:00Z"), ZoneId.of("UTC"));

    private final LoanValidationServiceImpl service = new LoanValidationServiceImpl(fixedClock);

    @Test
    void shouldBeEligible_whenAllRulesPass() {
        LoanValidationRequest req = new LoanValidationRequest(
                2500.0,    // monthlySalary
                6000.0,    // requestedAmount
                24,        // termMonths
                LocalDate.parse("2025-04-01") // lastLoanDate (older than 3 months from 2025-09-10)
        );

        StepVerifier.create(service.validate(req))
                .assertNext(result -> {
                    assertThat(result.isEligible()).isTrue();
                    assertThat(result.getReasons()).isEmpty();
                    assertThat(result.getMonthlyPayment()).isEqualTo(6000.0 / 24);
                })
                .verifyComplete();
    }

    @Test
    void shouldFail_hasRecentLoan_and_capacity() {
        // today = 2025-09-10 UTC => since = 2025-06-10
        LoanValidationRequest req = new LoanValidationRequest(
                1000.0,
                12000.0,
                12,
                LocalDate.parse("2025-07-01") // recent (after 2025-06-10)
        );

        StepVerifier.create(service.validate(req))
                .assertNext(result -> {
                    assertThat(result.isEligible()).isFalse();
                    assertThat(result.getReasons()).contains("HAS_RECENT_LOANS", "CAPACIDAD_INSUFICIENTE");
                    assertThat(result.getMonthlyPayment()).isEqualTo(12000.0 / 12);
                })
                .verifyComplete();
    }

    @Test
    void shouldFail_whenInvalidData() {
        LoanValidationRequest req = new LoanValidationRequest(
                0.0,       // invalid salary
                -100.0,    // invalid amount
                0,         // invalid term (but validated by service too)
                null
        );

        StepVerifier.create(service.validate(req))
                .assertNext(result -> {
                    assertThat(result.isEligible()).isFalse();
                    assertThat(result.getReasons()).contains("DATOS_INVALIDOS", "PLAZO_MAXIMO_SUPERADO");
                })
                .verifyComplete();
    }

    @Test
    void shouldFail_whenTermOutOfRange() {
        LoanValidationRequest req = new LoanValidationRequest(
                2000.0,
                1000.0,
                37, // > 36
                null
        );

        StepVerifier.create(service.validate(req))
                .assertNext(result -> {
                    assertThat(result.isEligible()).isFalse();
                    assertThat(result.getReasons()).contains("PLAZO_MAXIMO_SUPERADO");
                })
                .verifyComplete();
    }
}
