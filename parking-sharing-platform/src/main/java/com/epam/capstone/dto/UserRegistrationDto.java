package com.epam.capstone.dto;

public class UserRegistrationDto {

    private String name;
    private String email;
    private String phone;
    private String password;

    private String nameError;
    private String emailError;
    private String phoneError;
    private String passwordError;

    public UserRegistrationDto() {}
    public UserRegistrationDto(String name, String email, String phone, String password,
                                String nameError, String emailError, String phoneError, String passwordError) {

        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;

        this.nameError = nameError;
        this.emailError = emailError;
        this.phoneError = phoneError;
        this.passwordError = passwordError;
    }

    public String getName() {
        return name;
    }

    public String getNameError() {
        return nameError;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNameError(String nameError) {
        this.nameError = nameError;
    }

    public String getEmail() {
        return email;
    }

    public String getEmailError() {
        return emailError;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmailError(String emailError) {
        this.emailError = emailError;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhoneError() {
        return phoneError;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPhoneError(String phoneError) {
        this.phoneError = phoneError;
    }

    public String getPassword() {
        return password;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordError(String passwordError) {
        this.passwordError = passwordError;
    }
}
