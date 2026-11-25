package com.retail.compliance.tax.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record TaxCalculationRequest(
        @NotBlank(message = "El código de país es obligatorio")
        String countryCode,

        @NotNull(message = "El importe base es obligatorio")
        @DecimalMin(value = "0.01", message = "El importe base debe ser mayor que 0")
        BigDecimal baseAmount,

        @NotBlank(message = "La categoría de producto es obligatoria (STANDARD, REDUCED, SUPER_REDUCED)")
        String productCategory
) {}