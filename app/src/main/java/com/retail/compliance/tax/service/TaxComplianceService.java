package com.retail.compliance.tax.service;

import com.retail.compliance.tax.dto.*;
import com.retail.compliance.tax.model.ComplianceAudit;
import com.retail.compliance.tax.model.TaxRule;
import com.retail.compliance.tax.repository.ComplianceAuditRepository;
import com.retail.compliance.tax.repository.TaxRuleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class TaxComplianceService {

    private final TaxRuleRepository taxRuleRepository;
    private final ComplianceAuditRepository auditRepository;

    public TaxComplianceService(TaxRuleRepository taxRuleRepository, ComplianceAuditRepository auditRepository) {
        this.taxRuleRepository = taxRuleRepository;
        this.auditRepository = auditRepository;
    }

    public TaxRule getTaxRuleByCountry(String countryCode) {
        return taxRuleRepository.findByCountryCodeIgnoreCase(countryCode.trim())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No existen reglas fiscales configuradas para el país: " + countryCode
                ));
    }

    public TaxCalculationResponse calculateTax(TaxCalculationRequest request) {
        TaxRule rule = getTaxRuleByCountry(request.countryCode());
        String category = request.productCategory().trim().toUpperCase();

        BigDecimal rate = switch (category) {
            case "STANDARD" -> rule.getStandardRate();
            case "REDUCED" -> rule.getReducedRate();
            case "SUPER_REDUCED" -> rule.getSuperReducedRate();
            default -> throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Categoría inválida. Valores permitidos: STANDARD, REDUCED, SUPER_REDUCED"
            );
        };

        BigDecimal taxAmount = request.baseAmount()
                .multiply(rate)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal totalAmount = request.baseAmount().add(taxAmount).setScale(2, RoundingMode.HALF_UP);

        return new TaxCalculationResponse(
                rule.getCountryCode(),
                rule.getCountryName(),
                rule.getCurrency(),
                category,
                request.baseAmount().setScale(2, RoundingMode.HALF_UP),
                rate,
                taxAmount,
                totalAmount
        );
    }

    public InvoiceValidationResponse validateInvoice(InvoiceValidationRequest request) {
        String code = request.countryCode().trim().toUpperCase();
        TaxRule rule = taxRuleRepository.findByCountryCodeIgnoreCase(code).orElse(null);

        if (rule == null) {
            String msg = "País no soportado para validación: " + code;
            ComplianceAudit saved = auditRepository.save(new ComplianceAudit(
                    request.invoiceNumber(), code, request.customerTaxId(),
                    request.totalAmount(), request.currency().toUpperCase(), false, msg
            ));
            return toValidationResponse(saved);
        }

        List<String> errors = new ArrayList<>();

        // 1. Validar formato del NIF / VAT ID según regex del país
        if (!Pattern.matches(rule.getTaxIdRegex(), request.customerTaxId().trim().toUpperCase())) {
            errors.add("Formato de VAT ID inválido para " + code + " (ejemplo esperado: " + rule.getTaxIdExample() + ")");
        }

        // 2. Validar moneda oficial del país
        if (!rule.getCurrency().equalsIgnoreCase(request.currency().trim())) {
            errors.add("Moneda incorrecta para " + code + " (esperada: " + rule.getCurrency() + ")");
        }

        // 3. Validar cuadre aritmético (base + impuestos == total)
        BigDecimal expectedTotal = request.baseAmount().add(request.taxAmount()).setScale(2, RoundingMode.HALF_UP);
        if (expectedTotal.compareTo(request.totalAmount().setScale(2, RoundingMode.HALF_UP)) != 0) {
            errors.add("Descuadre aritmético: base + impuestos (" + expectedTotal + ") != total (" + request.totalAmount() + ")");
        }

        boolean isValid = errors.isEmpty();
        String message = isValid ? "Factura válida y conforme a la normativa de " + rule.getCountryName()
                : String.join("; ", errors);

        ComplianceAudit saved = auditRepository.save(new ComplianceAudit(
                request.invoiceNumber().trim(),
                rule.getCountryCode(),
                request.customerTaxId().trim().toUpperCase(),
                request.totalAmount().setScale(2, RoundingMode.HALF_UP),
                request.currency().trim().toUpperCase(),
                isValid,
                message
        ));

        return toValidationResponse(saved);
    }

    public List<ComplianceAudit> getAudits(String countryCode, Boolean valid) {
        boolean hasCountry = countryCode != null && !countryCode.isBlank();
        if (hasCountry && valid != null) {
            return auditRepository.findByCountryCodeIgnoreCaseAndValid(countryCode.trim(), valid);
        } else if (hasCountry) {
            return auditRepository.findByCountryCodeIgnoreCase(countryCode.trim());
        } else if (valid != null) {
            return auditRepository.findByValid(valid);
        }
        return auditRepository.findAll();
    }

    private InvoiceValidationResponse toValidationResponse(ComplianceAudit audit) {
        return new InvoiceValidationResponse(
                audit.getId(),
                audit.getInvoiceNumber(),
                audit.getCountryCode(),
                audit.isValid(),
                audit.getValidationMessage(),
                audit.getCheckedAt()
        );
    }
}