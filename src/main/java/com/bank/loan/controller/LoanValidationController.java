package com.bank.loan.controller;

import com.bank.loan.dto.LoanValidationRequest;
import com.bank.loan.dto.LoanValidationResult;
import com.bank.loan.service.LoanValidationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/loan-validations")
@Validated
public class LoanValidationController {

    private final LoanValidationService service;

    public LoanValidationController(LoanValidationService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<LoanValidationResult>> validateLoan(
            @Valid @RequestBody LoanValidationRequest request) {

        return service.validate(request)
                .map(result -> ResponseEntity.ok(result));
    }
}
