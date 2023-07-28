package com.krab51.webapp.dto;

public class LoginDto {
    public String userName;
    public String password;

    @Override
    public String toString() {
        return "LoginDTO{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
