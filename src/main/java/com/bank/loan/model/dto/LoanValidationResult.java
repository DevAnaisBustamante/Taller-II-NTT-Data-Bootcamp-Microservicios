package com.bank.loan.model.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoanValidationResult {
    private boolean eligible;
    private List<String> reasons;      // allowed: HAS_RECENT_LOANS, PLAZO_MAXIMO_SUPERADO, CAPACIDAD_INSUFICIENTE, DATOS_INVALIDOS
    private BigDecimal monthlyPayment; // requestedAmount / termMonths
}
