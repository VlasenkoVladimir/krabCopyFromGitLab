package com.krab51.webapp.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Отчет о вылове по выданной путевке
 */
@Entity
public class KrabReport {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Long id;

    /** Заявка на вылов связанная с отчетом */
    @ManyToOne
    @JoinColumn(name = "kraborder_id")
    public KrabOrder krabOrder;

    /** Фактическая дата начала лова */
    @Column(nullable = false)
    public LocalDateTime startDate;

    /** Фактическая дата окончания лова */
    @Column(nullable = false)
    public LocalDateTime endDate;

    /** Сколько штук оставлено себе */
    @Column(nullable = false)
    public int actuallyCnt;

    /** Сколько итого кг в оставленном себе улове */
    @Column(nullable = false)
    public BigDecimal actuallyKgs;

    /** Сколько отпущено штук */
    @Column
    public int releasedCnt;

    @Override
    public String toString() {
        return "KrabReport{" +
                "id=" + id +
                ", krabOrder=" + krabOrder +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", actuallyCnt=" + actuallyCnt +
                ", actuallyKgs=" + actuallyKgs +
                ", releasedCnt=" + releasedCnt +
                '}';
    }
}