package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Client;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

public class RegisterDTO {

    @NotBlank(message = "Firstname is required")
    @Size(min = 4, max = 20, message = "Firstname must be less than 20 and greater than 4")
    private String firstName;
    @NotBlank(message = "Lastname is required")
    @Size(min = 4, max = 20, message = "Lastname must be less than 20 and greater than 4 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid format")
    private String email;
    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]+$", message = "Incorrect password format")
    @Size(min = 8, max = 20, message = "Password must be less than 20 and greater than 8 characters")
    private String password;

    public RegisterDTO() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
