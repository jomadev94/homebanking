package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;

public class LoginResponseDto {
    private String token;
    private String firstName;
    private String lastName;
    private String email;

    public LoginResponseDto(String token, Client client) {
        this.token = token;
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
