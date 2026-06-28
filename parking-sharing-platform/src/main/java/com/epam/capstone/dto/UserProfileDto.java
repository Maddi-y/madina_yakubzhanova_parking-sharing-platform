package com.epam.capstone.dto;

public class UserProfileDto {

    private String name;
    private String email;
    private String phone;

    private String nameError;
    private String emailError;
    private String phoneError;
    private String commonError;

    public UserProfileDto() {
    }

    public UserProfileDto(String name, String email, String phone, String nameError, String emailError, String phoneError, String commonError) {

        this.name = name;
        this.email = email;
        this.phone = phone;

        this.nameError = nameError;
        this.emailError = emailError;
        this.phoneError = phoneError;
        this.commonError = commonError;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNameError() {
        return nameError;
    }

    public void setNameError(String nameError) {
        this.nameError = nameError;
    }

    public String getEmailError() {
        return emailError;
    }

    public void setEmailError(String emailError) {
        this.emailError = emailError;
    }

    public String getPhoneError() {
        return phoneError;
    }

    public void setPhoneError(String phoneError) {
        this.phoneError = phoneError;
    }

    public String getCommonError() {
        return commonError;
    }

    public void setCommonError(String commonError) {
        this.commonError = commonError;
    }
}
