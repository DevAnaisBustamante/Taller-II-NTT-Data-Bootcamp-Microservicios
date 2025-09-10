package com.bank.loan;

import com.bank.loan.controller.LoanValidationController;
import com.bank.loan.dto.LoanValidationRequest;
import com.bank.loan.service.LoanValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;

@WebFluxTest(controllers = LoanValidationController.class)
class LoanValidationControllerTest {

    WebTestClient webTestClient;

    @MockBean
    LoanValidationService service;

    @BeforeEach
    void setUp(WebTestClient.Builder builder) {
        webTestClient = builder.baseUrl("/").build();
    }

    @Test
    void postValidation_returns200() {
        Mockito.when(service.validate(any())).thenReturn(Mono.just(
                new com.bank.loan.dto.LoanValidationResult(true, java.util.List.of(), 250.0)
        ));

        LoanValidationRequest req = new LoanValidationRequest(2500.0, 6000.0, 24, null);

        webTestClient.post()
                .uri("/loan-validations")
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.eligible").isEqualTo(true)
                .jsonPath("$.monthlyPayment").isEqualTo(250.0);
    }
}