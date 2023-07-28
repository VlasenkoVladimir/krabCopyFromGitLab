package com.krab51.webapp.dto;


import java.io.Serializable;

public class RoleDto implements Serializable {
    public Long id;
    public String title;
    public String description;

    public RoleDto() {
    }

    public RoleDto(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return "RoleDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
