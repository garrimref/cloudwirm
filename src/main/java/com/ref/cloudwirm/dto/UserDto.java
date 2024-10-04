package com.ref.cloudwirm.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto {

    @NotBlank(message = "Username is required and cannot be blank.")
    @Size(min = 3, max = 25, message = "Username must be between 3 and 25 characters")
    private String username;

    @NotBlank(message = "Password is required and cannot be blank.")
    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;

    public UserDto() {
    }

    public UserDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
