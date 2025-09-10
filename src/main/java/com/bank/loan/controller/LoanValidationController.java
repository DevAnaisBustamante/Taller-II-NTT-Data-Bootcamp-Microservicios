package com.bank.loan.controller;

import com.bank.loan.model.dto.LoanValidationRequest;
import com.bank.loan.model.dto.LoanValidationResult;
import com.bank.loan.service.LoanValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class LoanValidationController {

    private final LoanValidationService service;

    @PostMapping(path = "/loan-validations", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<LoanValidationResult> validate(@RequestBody @Validated LoanValidationRequest request) {
        return service.validate(request);
    }
}
