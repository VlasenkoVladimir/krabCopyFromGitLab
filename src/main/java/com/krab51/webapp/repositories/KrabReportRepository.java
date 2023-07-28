package com.krab51.webapp.repositories;

import com.krab51.webapp.domain.KrabReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KrabReportRepository extends JpaRepository<KrabReport, Long> {
    List<KrabReport> findByKrabOrderId(Long id);
}