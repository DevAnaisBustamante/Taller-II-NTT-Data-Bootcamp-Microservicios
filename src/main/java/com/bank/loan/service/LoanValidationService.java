package com.bank.loan.service;

import com.bank.loan.dto.LoanValidationRequest;
import com.bank.loan.dto.LoanValidationResult;

import reactor.core.publisher.Mono;


public interface LoanValidationService {
    Mono<LoanValidationResult> validate(LoanValidationRequest request);
}
