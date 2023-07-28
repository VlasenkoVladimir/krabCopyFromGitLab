package com.krab51.webapp.domain;

import com.krab51.webapp.domain.enums.KrabOrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.krab51.webapp.domain.enums.KrabOrderStatus.CLOSED;
import static com.krab51.webapp.domain.enums.KrabOrderStatus.CLOSED_EXPIRED;
import static com.krab51.webapp.domain.enums.KrabOrderStatus.EXPIRED;
import static com.krab51.webapp.domain.enums.KrabOrderStatus.OPENED;
import static com.krab51.webapp.domain.enums.KrabOrderStatus.WARNING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.GenerationType.IDENTITY;
import static java.time.LocalDateTime.now;
import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Путевка
 */
@Entity
public class KrabOrder {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Long id;

    /** Дата оформления путевки */
    @Column(nullable = false)
    public LocalDate regDate;

    /** Клиент, которому выдана путевка */
    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "client_id", nullable = false)
    public Client client;

    /** Количество единиц краба для вылова */
    @Column(nullable = false)
    public int enlistedCnt;

    /** Цена путевки */
    @Column(nullable = false)
    public BigDecimal price;

    /** Признак оплаты по безналичному рассчету */
    @Column
    public boolean noCashOrder;

    /** Дата с которой началось действие путевки */
    @Column(nullable = false)
    public LocalDateTime beginDate;

    /** Количество суток на которое действует разрешение на вылов */
    @Column(nullable = false)
    public long expirationDaysNumber;

    /** Дата закрытия путевки, как признак завершения */
    @Column
    public LocalDateTime endDate;

    /** Ловушки используемые для лова */
    @ElementCollection(fetch = EAGER)
    @CollectionTable(joinColumns = @JoinColumn(name = "kraborder_id"))
    @Column(name="regNumber")
    public Set<String> traps = new HashSet<>(); //TODO fix to Set<Trap>

    /** Юр.лицо выдавшее заявку */
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "company_id", nullable = false)
    public Company company;

    /**
     * Статус заявки
     */
    public KrabOrderStatus getStatus() {
        long daysBetween = DAYS.between(beginDate, now());

        if (endDate != null) {
            if (DAYS.between(beginDate, endDate) > expirationDaysNumber) return CLOSED_EXPIRED;
            return CLOSED;
        }

        if (expirationDaysNumber < daysBetween) return EXPIRED;
        else if (expirationDaysNumber - daysBetween <= 1) return WARNING;

        return OPENED;
    }
}