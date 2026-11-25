package com.retail.compliance.tax.repository;

import com.retail.compliance.tax.model.TaxRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TaxRuleRepository extends JpaRepository<TaxRule, Long> {
    Optional<TaxRule> findByCountryCodeIgnoreCase(String countryCode);
}