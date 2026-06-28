package com.epam.capstone.dto;

public class ForgotPasswordDto {

    private String email;
    private String emailError;
    private String commonError;

    public ForgotPasswordDto() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailError() {
        return emailError;
    }

    public void setEmailError(String emailError) {
        this.emailError = emailError;
    }

    public String getCommonError() {
        return commonError;
    }

    public void setCommonError(String commonError) {
        this.commonError = commonError;
    }
}