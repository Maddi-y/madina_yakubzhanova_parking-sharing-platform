package com.epam.capstone.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class Parking {

    private Long parkingId;

    private Long ownerId;

    private Long addressId;

    private String title;

    private String description;

    private BigDecimal hourlyRate;

    private LocalDateTime createdAt;

    public Parking() {
    }

    public Parking(Long parkingId,
                   Long ownerId,
                   Long addressId,
                   String title,
                   String description,
                   BigDecimal hourlyRate,
                   LocalDateTime createdAt) {

        this.parkingId = parkingId;
        this.ownerId = ownerId;
        this.addressId = addressId;
        this.title = title;
        this.description = description;
        this.hourlyRate = hourlyRate;
        this.createdAt = createdAt;
    }

    public Long getParkingId() {
        return parkingId;
    }

    public void setParkingId(Long parkingId) {
        this.parkingId = parkingId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof Parking parking)) {
            return false;
        }

        return Objects.equals(parkingId, parking.parkingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parkingId);
    }

    @Override
    public String toString() {
        return "Parking{" +
                "parkingId=" + parkingId +
                ", ownerId=" + ownerId +
                ", addressId=" + addressId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", hourlyRate=" + hourlyRate +
                ", createdAt=" + createdAt +
                '}';
    }
}