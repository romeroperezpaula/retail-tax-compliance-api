package com.retail.compliance.tax.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tax_rules")
public class TaxRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 2)
    private String countryCode;

    @Column(nullable = false)
    private String countryName;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal standardRate;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal reducedRate;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal superReducedRate;

    @Column(nullable = false)
    private String taxIdRegex;

    @Column(nullable = false)
    private String taxIdExample;

    public TaxRule() {}

    public Long getId() { return id; }
    public String getCountryCode() { return countryCode; }
    public String getCountryName() { return countryName; }
    public String getCurrency() { return currency; }
    public BigDecimal getStandardRate() { return standardRate; }
    public BigDecimal getReducedRate() { return reducedRate; }
    public BigDecimal getSuperReducedRate() { return superReducedRate; }
    public String getTaxIdRegex() { return taxIdRegex; }
    public String getTaxIdExample() { return taxIdExample; }
}