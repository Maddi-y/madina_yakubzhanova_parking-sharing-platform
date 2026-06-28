package com.epam.capstone.model;

import com.epam.capstone.model.enums.ParkingSpotStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class ParkingSpot {

    private Long spotId;
    private Long parkingId;
    private Integer spotNumber;
    private ParkingSpotStatus status;
    private LocalDateTime createdAt;

    public ParkingSpot() {
    }

    public ParkingSpot(Long spotId,
                       Long parkingId,
                       Integer spotNumber,
                       ParkingSpotStatus status,
                       LocalDateTime createdAt) {

        this.spotId = spotId;
        this.parkingId = parkingId;
        this.spotNumber = spotNumber;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getSpotId() {
        return spotId;
    }

    public void setSpotId(Long spotId) {
        this.spotId = spotId;
    }

    public Long getParkingId() {
        return parkingId;
    }

    public void setParkingId(Long parkingId) {
        this.parkingId = parkingId;
    }

    public Integer getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(Integer spotNumber) {
        this.spotNumber = spotNumber;
    }

    public ParkingSpotStatus getStatus() {
        return status;
    }

    public void setStatus(ParkingSpotStatus status) {
        this.status = status;
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
                ", parkingId=" + parkingId +
                ", spotNumber=" + spotNumber +
                ", status=" + status +
                '}';
    }
}
