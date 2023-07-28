package com.krab51.webapp.domain;

import com.krab51.webapp.domain.enums.TrapStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;

/**
 * Ловушка для ловли крабов
 */
@Entity
public class Trap {
    /** Регистрационный номер */
    @Id
    public String regNumber;

    /** Статус ловушки */
    @Column(nullable = false)
    @Enumerated(STRING)
    public TrapStatus status;

    /** Дата регистрации */
    @Column(nullable = false)
    public LocalDate regDate;

    /** Владелец */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "trapowner_id", nullable = false)
    public TrapOwner trapOwner;
}