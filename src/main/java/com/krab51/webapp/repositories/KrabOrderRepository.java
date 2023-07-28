package com.krab51.webapp.repositories;

import com.krab51.webapp.domain.KrabOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KrabOrderRepository extends JpaRepository<KrabOrder, Long> {
}