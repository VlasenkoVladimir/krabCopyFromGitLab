package com.krab51.webapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Учетные данные юридического лица
 */

@Entity
public class Company {

    /** Внутренний Id */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "company_id")
    public Long id;

    /** Наименование юр.лица */
    @Column(nullable = false)
    public String companyName;

    /** Дата регистрации в системе */
    @Column(nullable = false)
    public LocalDate companyRegDate;

    /** Юридический адрес */
    @Column(nullable = false)
    public String companyAddress;

    /** Рыболовный участок */ // TODO может ли их быть несколько (Set<String>) ?
    @Column(nullable = false, columnDefinition = "TEXT")
    public String fishingArea;

    /** Разрешение на добычу */
    @Column(nullable = false)
    public String miningPermit;
}
