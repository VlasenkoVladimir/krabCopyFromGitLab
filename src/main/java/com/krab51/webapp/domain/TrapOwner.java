package com.krab51.webapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Владелец ловушек для ловли крабов
 */
@Entity
public class TrapOwner {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Long id;

    /** Название организации */
    @Column(nullable = false)
    public String name;
}