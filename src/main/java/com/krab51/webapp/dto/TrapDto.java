package com.krab51.webapp.dto;

import com.krab51.webapp.domain.enums.TrapStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class TrapDto implements Serializable {
    public String regNumber;
    public TrapStatus status;
    public LocalDate regDate;
    public TrapOwnerDto trapOwner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrapDto entity = (TrapDto) o;
        return Objects.equals(this.regNumber, entity.regNumber) &&
                Objects.equals(this.status, entity.status) &&
                Objects.equals(this.regDate, entity.regDate) &&
                Objects.equals(this.trapOwner, entity.trapOwner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regNumber, status, regDate, trapOwner);
    }
}