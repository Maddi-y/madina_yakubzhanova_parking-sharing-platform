package com.epam.capstone.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class PasswordResetToken {

    private Long tokenId;
    private Long userId;
    private String token;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;

    public PasswordResetToken() {
    }

    public PasswordResetToken(Long tokenId, Long userId, String token, LocalDateTime expiresAt, LocalDateTime createdAt) {

        this.tokenId = tokenId;
        this.userId = userId;
        this.token = token;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PasswordResetToken that)) return false;
        return Objects.equals(tokenId, that.tokenId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tokenId);
    }

    @Override
    public String toString() {
        return "PasswordResetToken{" +
                "tokenId=" + tokenId +
                ", userId=" + userId +
                ", token='" + token + '\'' +
                ", expiresAt=" + expiresAt +
                ", createdAt=" + createdAt +
                '}';
    }
}