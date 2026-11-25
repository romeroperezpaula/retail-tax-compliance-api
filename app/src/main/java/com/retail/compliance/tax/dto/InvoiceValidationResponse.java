package com.retail.compliance.tax.dto;

import java.time.LocalDateTime;

public record InvoiceValidationResponse(
        Long auditId,
        String invoiceNumber,
        String countryCode,
        boolean valid,
        String message,
        LocalDateTime checkedAt
) {}