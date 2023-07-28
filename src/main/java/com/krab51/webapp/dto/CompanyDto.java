package com.krab51.webapp.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class CompanyDto implements Serializable {
    public Long id;
    public String companyName;
    public LocalDate companyRegDate;
    public String companyAddress;
    public String fishingArea;
    public String miningPermit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyDto that)) return false;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(companyName, that.companyName)) return false;
        if (!Objects.equals(companyRegDate, that.companyRegDate))
            return false;
        if (!Objects.equals(companyAddress, that.companyAddress))
            return false;
        if (!Objects.equals(fishingArea, that.fishingArea)) return false;
        return Objects.equals(miningPermit, that.miningPermit);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (companyName != null ? companyName.hashCode() : 0);
        result = 31 * result + (companyRegDate != null ? companyRegDate.hashCode() : 0);
        result = 31 * result + (companyAddress != null ? companyAddress.hashCode() : 0);
        result = 31 * result + (fishingArea != null ? fishingArea.hashCode() : 0);
        result = 31 * result + (miningPermit != null ? miningPermit.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CompanyDto{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", companyRegDate=" + companyRegDate +
                ", companyAddress='" + companyAddress + '\'' +
                ", fishingArea='" + fishingArea + '\'' +
                ", miningPermit='" + miningPermit + '\'' +
                '}';
    }
}
