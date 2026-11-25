package com.retail.compliance.tax.dto;

import java.math.BigDecimal;

public record TaxCalculationResponse(
        String countryCode,
        String countryName,
        String currency,
        String productCategory,
        BigDecimal baseAmount,
        BigDecimal appliedRatePercentage,
        BigDecimal taxAmount,
        BigDecimal totalAmount
) {}