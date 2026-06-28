package com.epam.capstone.exception;

import com.epam.capstone.dto.UserProfileDto;

public class ProfileValidationException extends RuntimeException {

    private final UserProfileDto profileDto;

    public ProfileValidationException(UserProfileDto profileDto) {

        super("Profile validation failed");
        this.profileDto = profileDto;
    }

    public UserProfileDto getProfileDto() {
        return profileDto;
    }
}
