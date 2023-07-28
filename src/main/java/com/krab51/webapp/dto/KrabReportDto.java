package com.krab51.webapp.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class KrabReportDto implements Serializable {
    public Long id;
    public KrabOrderDto krabOrder; // ??? Dto
    public LocalDateTime startDate;
    public LocalDateTime endDate;
    public int actuallyCnt;
    public BigDecimal actuallyKgs;
    public int releasedCnt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KrabReportDto entity = (KrabReportDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.krabOrder, entity.krabOrder) &&
                Objects.equals(this.startDate, entity.startDate) &&
                Objects.equals(this.endDate, entity.endDate) &&
                Objects.equals(this.actuallyCnt, entity.actuallyCnt) &&
                Objects.equals(this.actuallyKgs, entity.actuallyKgs) &&
                Objects.equals(this.releasedCnt, entity.releasedCnt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, krabOrder, startDate, endDate, actuallyCnt, actuallyKgs, releasedCnt);
    }
}