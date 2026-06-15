package com.epam.capstone.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Favourite {
    private Long favouriteId;
    private User user;
    private ParkingSpot parkingSpot;
    private LocalDateTime createdAt;

    public Favourite() {
    }

    public Favourite(Long favouriteId,
                     User user,
                     ParkingSpot parkingSpot,
                     LocalDateTime createdAt) {
        this.favouriteId = favouriteId;
        this.user = user;
        this.parkingSpot = parkingSpot;
        this.createdAt = createdAt;
    }

    public Long getFavouriteId() {
        return favouriteId;
    }

    public void setFavouriteId(Long favouriteId) {
        this.favouriteId = favouriteId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
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

        if (!(o instanceof Favourite favourite)) {
            return false;
        }

        return Objects.equals(favouriteId, favourite.favouriteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(favouriteId);
    }

    @Override
    public String toString() {

        Long userId = null;
        Long parkingSpotId = null;

        if (user != null) {
            userId = user.getUserId();
        }

        if (parkingSpot != null) {
            parkingSpotId = parkingSpot.getSpotId();
        }

        return "Favourite{" +
                "favouriteId=" + favouriteId +
                ", userId=" + userId +
                ", parkingSpotId=" + parkingSpotId +
                ", createdAt=" + createdAt +
                '}';
    }
}
