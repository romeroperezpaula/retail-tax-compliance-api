package com.retail.compliance.tax.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record InvoiceValidationRequest(
        @NotBlank(message = "El número de factura es obligatorio")
        String invoiceNumber,

        @NotBlank(message = "El código de país es obligatorio")
        String countryCode,

        @NotBlank(message = "El NIF/VAT ID del cliente es obligatorio")
        String customerTaxId,

        @NotNull(message = "El importe base es obligatorio")
        @DecimalMin(value = "0.01", message = "El importe base debe ser mayor que 0")
        BigDecimal baseAmount,

        @NotNull(message = "El importe de impuestos es obligatorio")
        @DecimalMin(value = "0.00", message = "El impuesto no puede ser negativo")
        BigDecimal taxAmount,

        @NotNull(message = "El importe total es obligatorio")
        @DecimalMin(value = "0.01", message = "El importe total debe ser mayor que 0")
        BigDecimal totalAmount,

        @NotBlank(message = "La moneda es obligatoria")
        String currency
) {}