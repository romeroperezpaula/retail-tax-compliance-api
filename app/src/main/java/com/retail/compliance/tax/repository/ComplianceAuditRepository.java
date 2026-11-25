package com.retail.compliance.tax.repository;

import com.retail.compliance.tax.model.ComplianceAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComplianceAuditRepository extends JpaRepository<ComplianceAudit, Long> {
    List<ComplianceAudit> findByCountryCodeIgnoreCase(String countryCode);
    List<ComplianceAudit> findByValid(boolean valid);
    List<ComplianceAudit> findByCountryCodeIgnoreCaseAndValid(String countryCode, boolean valid);
}