package com.krab51.webapp.repositories;

import com.krab51.webapp.domain.TrapOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrapOwnerRepository extends JpaRepository<TrapOwner, Long> {
}