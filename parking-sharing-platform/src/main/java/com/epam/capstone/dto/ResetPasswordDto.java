package com.epam.capstone.dto;

public class ResetPasswordDto {

    private String token;

    private String newPassword;
    private String confirmPassword;

    private String newPasswordError;
    private String confirmPasswordError;
    private String commonError;

    public ResetPasswordDto() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getNewPasswordError() {
        return newPasswordError;
    }

    public void setNewPasswordError(String newPasswordError) {
        this.newPasswordError = newPasswordError;
    }

    public String getConfirmPasswordError() {
        return confirmPasswordError;
    }

    public void setConfirmPasswordError(String confirmPasswordError) {
        this.confirmPasswordError = confirmPasswordError;
    }

    public String getCommonError() {
        return commonError;
    }

    public void setCommonError(String commonError) {
        this.commonError = commonError;
    }

    public boolean hasErrors() {

        return newPasswordError != null || confirmPasswordError != null || commonError != null;
    }
}