package com.krab51.webapp.repositories;

import com.krab51.webapp.domain.Trap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrapRepository extends JpaRepository<Trap, Long> {

    Trap findByRegNumber(String regNumber);
}