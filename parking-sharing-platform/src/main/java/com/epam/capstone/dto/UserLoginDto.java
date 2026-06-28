package com.epam.capstone.dto;

public class UserLoginDto {

    private String email;
    private String password;

    private String emailError;
    private String passwordError;
    private String commonError;

    public UserLoginDto() {
    }

    public UserLoginDto(String email, String password, String emailError, String passwordError, String commonError) {

        this.email = email;
        this.password = password;
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.commonError = commonError;
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

    public String getEmailError() {
        return emailError;
    }

    public void setEmailError(String emailError) {
        this.emailError = emailError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }

    public String getCommonError() {
        return commonError;
    }

    public void setCommonError(String commonError) {
        this.commonError = commonError;
    }
}
