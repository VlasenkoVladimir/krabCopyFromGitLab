package com.krab51.webapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Клиент
 */
@Entity
//@Table(name = "clients", uniqueConstraints = {@UniqueConstraint(name = "docNumber", columnNames = "docnumber")})
public class Client {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Long id;

    /** Тип документа */
    @Column(nullable = false)
    public String docType;

    /** Номер документа */
    @Column(nullable = false)
    public String docNumber;

    /** Кем выдан документ */
    @Column(nullable = false)
    public String docAuthority;

    /** Дата выдачи документа */
    @Column(nullable = false)
    public LocalDate docDate;

    /** Имя */
    @Column(nullable = false)
    public String firstName;

    /** Отчество */
    @Column
    public String middleName;

    /** Фамилия */
    @Column(nullable = false)
    public String lastName;

    /** Номер телефона */
    @Column
    public String phoneNumber;

    /** Адрес регистрации */
    @Column(nullable = false)
    public String registration;
}