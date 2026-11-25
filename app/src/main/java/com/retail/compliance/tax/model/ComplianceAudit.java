package com.retail.compliance.tax.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "compliance_audits")
public class ComplianceAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String invoiceNumber;

    @Column(nullable = false, length = 2)
    private String countryCode;

    @Column(nullable = false)
    private String customerTaxId;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false)
    private boolean valid;

    @Column(length = 500)
    private String validationMessage;

    @Column(nullable = false)
    private LocalDateTime checkedAt;

    public ComplianceAudit() {}

    public ComplianceAudit(String invoiceNumber, String countryCode, String customerTaxId,
                           BigDecimal totalAmount, String currency, boolean valid, String validationMessage) {
        this.invoiceNumber = invoiceNumber;
        this.countryCode = countryCode;
        this.customerTaxId = customerTaxId;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.valid = valid;
        this.validationMessage = validationMessage;
        this.checkedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getInvoiceNumber() { return invoiceNumber; }
    public String getCountryCode() { return countryCode; }
    public String getCustomerTaxId() { return customerTaxId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public String getCurrency() { return currency; }
    public boolean isValid() { return valid; }
    public String getValidationMessage() { return validationMessage; }
    public LocalDateTime getCheckedAt() { return checkedAt; }
}