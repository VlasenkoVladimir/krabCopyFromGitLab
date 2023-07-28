package com.krab51.webapp.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import static jakarta.persistence.GenerationType.IDENTITY;

/**
 * Оператор
 */
@Entity
@Table(name = "operators",
        uniqueConstraints = {@UniqueConstraint(name = "uniqueUserName", columnNames = "username")})

@SequenceGenerator(name = "default_generator", sequenceName = "operators_seq", allocationSize = 1)
public class Operator {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Long id;

    @Column(name = "username", nullable = false)
    public String userName;

    @Column(name = "password", nullable = false)
    public String password;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id", nullable = true, foreignKey = @ForeignKey(name = "FK_OPERATORS_ROLES"))
    public Role role;
}