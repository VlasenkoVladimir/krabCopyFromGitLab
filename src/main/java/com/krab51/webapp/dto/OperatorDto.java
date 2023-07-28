package com.krab51.webapp.dto;

import java.io.Serializable;

public class OperatorDto implements Serializable {
    public Long id;
    public String userName;
    public String password;
    public RoleDto role;
}