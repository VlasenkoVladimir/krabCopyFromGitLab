package com.krab51.webapp.dto;

import java.io.Serializable;
import java.util.Objects;

public class TrapOwnerDto implements Serializable {
    public Long id;
    public String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrapOwnerDto entity = (TrapOwnerDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}