package com.epam.capstone.dto;

public class ChangePasswordDto {

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    private String currentPasswordError;
    private String newPasswordError;
    private String confirmPasswordError;
    private String commonError;

    public ChangePasswordDto() {
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
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

    public String getCurrentPasswordError() {
        return currentPasswordError;
    }

    public void setCurrentPasswordError(String currentPasswordError) {
        this.currentPasswordError = currentPasswordError;
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
}
