package com.krab51.webapp.repositories;

import com.krab51.webapp.domain.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorRepository extends JpaRepository<Operator, Long> {
    Operator findUserByUserName(String userName);
}