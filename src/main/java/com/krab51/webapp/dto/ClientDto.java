package com.krab51.webapp.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class ClientDto implements Serializable {
    public Long id;
    public String docType;
    public String docNumber;
    public String docAuthority;
    public LocalDate docDate;
    public String firstName;
    public String middleName;
    public String lastName;
    public String phoneNumber;
    public String registration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientDto entity = (ClientDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.docType, entity.docType) &&
                Objects.equals(this.docNumber, entity.docNumber) &&
                Objects.equals(this.docAuthority, entity.docAuthority) &&
                Objects.equals(this.docDate, entity.docDate) &&
                Objects.equals(this.firstName, entity.firstName) &&
                Objects.equals(this.middleName, entity.middleName) &&
                Objects.equals(this.lastName, entity.lastName) &&
                Objects.equals(this.phoneNumber, entity.phoneNumber) &&
                Objects.equals(this.registration, entity.registration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, docType, docNumber, docAuthority, docDate, firstName, middleName, lastName, phoneNumber,
                registration);
    }
}