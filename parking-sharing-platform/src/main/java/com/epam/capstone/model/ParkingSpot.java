package com.epam.capstone.model;

import com.epam.capstone.model.enums.ParkingSpotStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class ParkingSpot {

    private Long spotId;
    private User owner;
    private String title;
    private String address;
    private String description;
    private BigDecimal hourlyRate;
    private ParkingSpotStatus status;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private LocalDateTime createdAt;

    public ParkingSpot() {
    }

    public ParkingSpot(Long spotId,
                       User owner,
                       String title,
                       String address,
                       String description,
                       BigDecimal hourlyRate,
                       ParkingSpotStatus status,
                       BigDecimal latitude,
                       BigDecimal longitude,
                       LocalDateTime createdAt) {

        this.spotId = spotId;
        this.owner = owner;
        this.title = title;
        this.address = address;
        this.description = description;
        this.hourlyRate = hourlyRate;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.createdAt = createdAt;
    }

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public ParkingSpotStatus getStatus() {
        return status;
    }

    public void setStatus(ParkingSpotStatus status) {
        this.status = status;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ParkingSpot parkingSpot)) {
            return false;
        }

        return Objects.equals(spotId, parkingSpot.spotId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(spotId);
    }

    @Override
    public String toString() {
        return "ParkingSpot{" +
                "spotId=" + spotId +
                ", title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", hourlyRate=" + hourlyRate +
                ", status=" + status +
                '}';
    }
}
