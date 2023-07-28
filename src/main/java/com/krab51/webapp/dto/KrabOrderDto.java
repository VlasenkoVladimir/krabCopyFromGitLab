package com.krab51.webapp.dto;

import com.krab51.webapp.domain.Company;
import com.krab51.webapp.domain.enums.KrabOrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class KrabOrderDto implements Serializable {
    public Long id;
    public LocalDate regDate;
    public ClientDto client;
    public int enlistedCnt;
    public BigDecimal price;
    public boolean noCashOrder;
    public LocalDateTime beginDate;
    public long expirationDaysNumber;
    public LocalDateTime endDate;
    public Set<String> traps = new HashSet<>();
    public KrabOrderStatus status;
    public Company company;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KrabOrderDto that)) return false;

        if (enlistedCnt != that.enlistedCnt) return false;
        if (noCashOrder != that.noCashOrder) return false;
        if (expirationDaysNumber != that.expirationDaysNumber) return false;
        if (!id.equals(that.id)) return false;
        if (!regDate.equals(that.regDate)) return false;
        if (!client.equals(that.client)) return false;
        if (!price.equals(that.price)) return false;
        if (!beginDate.equals(that.beginDate)) return false;
        if (!endDate.equals(that.endDate)) return false;
        if (!traps.equals(that.traps)) return false;
        if (status != that.status) return false;
        return company.equals(that.company);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + regDate.hashCode();
        result = 31 * result + client.hashCode();
        result = 31 * result + enlistedCnt;
        result = 31 * result + price.hashCode();
        result = 31 * result + (noCashOrder ? 1 : 0);
        result = 31 * result + beginDate.hashCode();
        result = 31 * result + (int) (expirationDaysNumber ^ (expirationDaysNumber >>> 32));
        result = 31 * result + endDate.hashCode();
        result = 31 * result + traps.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + company.hashCode();
        return result;
    }
}