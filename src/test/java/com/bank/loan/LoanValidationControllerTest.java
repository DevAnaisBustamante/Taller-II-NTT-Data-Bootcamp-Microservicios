package com.bank.loan;

import com.bank.loan.controller.LoanValidationController;
import com.bank.loan.dto.LoanValidationRequest;
import com.bank.loan.dto.LoanValidationResult;
import com.bank.loan.service.LoanValidationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

class LoanValidationControllerTest {

    @Test
    void postValidation_returns200() {
        LoanValidationRequest req = new LoanValidationRequest(2500.0, 6000.0, 24, null);
        LoanValidationResult res = new LoanValidationResult(true, List.of(), 250.0);

        LoanValidationService mockService = Mockito.mock(LoanValidationService.class);
        Mockito.when(mockService.validate(Mockito.any()))
                .thenReturn(Mono.just(res));

        LoanValidationController controller = new LoanValidationController(mockService);
        WebTestClient client = WebTestClient.bindToController(controller).build();

        client.post()
                .uri("/loan-validations")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.eligible").isEqualTo(true)
                .jsonPath("$.reasons").isArray()
                .jsonPath("$.monthlyPayment").isEqualTo(250.0);
    }
}