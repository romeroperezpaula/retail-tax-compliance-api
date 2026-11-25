package com.retail.compliance.tax.controller;

import com.retail.compliance.tax.dto.*;
import com.retail.compliance.tax.model.ComplianceAudit;
import com.retail.compliance.tax.model.TaxRule;
import com.retail.compliance.tax.service.TaxComplianceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TaxComplianceController {

    private final TaxComplianceService service;

    public TaxComplianceController(TaxComplianceService service) {
        this.service = service;
    }

    @GetMapping("/tax-rules/{countryCode}")
    public ResponseEntity<TaxRule> getTaxRules(@PathVariable String countryCode) {
        return ResponseEntity.ok(service.getTaxRuleByCountry(countryCode));
    }

    @PostMapping("/tax/calculate")
    public ResponseEntity<TaxCalculationResponse> calculateTax(@Valid @RequestBody TaxCalculationRequest request) {
        return ResponseEntity.ok(service.calculateTax(request));
    }

    @PostMapping("/compliance/validate-invoice")
    public ResponseEntity<InvoiceValidationResponse> validateInvoice(@Valid @RequestBody InvoiceValidationRequest request) {
        return ResponseEntity.ok(service.validateInvoice(request));
    }

    @GetMapping("/compliance/audits")
    public ResponseEntity<List<ComplianceAudit>> getAudits(
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) Boolean valid) {
        return ResponseEntity.ok(service.getAudits(countryCode, valid));
    }
}