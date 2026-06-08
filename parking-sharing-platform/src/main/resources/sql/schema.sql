
-- Parking Sharing Platform
-- Database Schema
-- PostgreSQL

CREATE TABLE roles (
role_id BIGSERIAL PRIMARY KEY,
name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
user_id BIGSERIAL PRIMARY KEY,
role_id BIGINT NOT NULL,

name VARCHAR(100) NOT NULL,

email VARCHAR(255) NOT NULL UNIQUE,

phone VARCHAR(20) UNIQUE,

password_hash VARCHAR(255) NOT NULL,

status VARCHAR(20) NOT NULL,

created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

CONSTRAINT fk_users_role
    FOREIGN KEY (role_id)
    REFERENCES roles(role_id)
    ON DELETE RESTRICT,

CONSTRAINT chk_users_status
    CHECK (
        status IN (
            'ACTIVE',
            'BLOCKED',
            'DELETED'
        )
    ),

CONSTRAINT chk_users_email
    CHECK (
        POSITION('@' IN email) > 1
    )

);

CREATE TABLE parking_spots (
spot_id BIGSERIAL PRIMARY KEY,

owner_id BIGINT NOT NULL,

title VARCHAR(100) NOT NULL,

address VARCHAR(255) NOT NULL,

description TEXT,

hourly_rate DECIMAL(10,2) NOT NULL,

status VARCHAR(20) NOT NULL,

latitude DECIMAL(10,7),

longitude DECIMAL(10,7),

created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

CONSTRAINT fk_parking_spots_owner
    FOREIGN KEY (owner_id)
    REFERENCES users(user_id)
    ON DELETE CASCADE,

CONSTRAINT chk_hourly_rate
    CHECK (hourly_rate > 0),

CONSTRAINT chk_spot_status
    CHECK (
        status IN (
            'AVAILABLE',
            'UNAVAILABLE',
            'ARCHIVED'
        )
    ),

CONSTRAINT chk_latitude
    CHECK (
        latitude IS NULL
        OR latitude BETWEEN -90 AND 90
    ),

CONSTRAINT chk_longitude
    CHECK (
        longitude IS NULL
        OR longitude BETWEEN -180 AND 180
    )
);

CREATE TABLE bookings (
booking_id BIGSERIAL PRIMARY KEY,

driver_id BIGINT NOT NULL,

spot_id BIGINT NOT NULL,

start_time TIMESTAMP NOT NULL,

end_time TIMESTAMP NOT NULL,

total_price DECIMAL(10,2) NOT NULL,

status VARCHAR(20) NOT NULL,

created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

CONSTRAINT fk_bookings_driver
    FOREIGN KEY (driver_id)
    REFERENCES users(user_id)
    ON DELETE CASCADE,

CONSTRAINT fk_bookings_spot
    FOREIGN KEY (spot_id)
    REFERENCES parking_spots(spot_id)
    ON DELETE CASCADE,

CONSTRAINT chk_booking_time
    CHECK (end_time > start_time),

CONSTRAINT chk_booking_price
    CHECK (total_price >= 0),

CONSTRAINT chk_booking_status
    CHECK (
        status IN (
            'PENDING',
            'CONFIRMED',
            'CANCELLED',
            'COMPLETED'
        )
    )
);

CREATE TABLE favourites (
favourite_id BIGSERIAL PRIMARY KEY,

user_id BIGINT NOT NULL,

spot_id BIGINT NOT NULL,

created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

CONSTRAINT fk_favourites_user
    FOREIGN KEY (user_id)
    REFERENCES users(user_id)
    ON DELETE CASCADE,

CONSTRAINT fk_favourites_spot
    FOREIGN KEY (spot_id)
    REFERENCES parking_spots(spot_id)
    ON DELETE CASCADE,

CONSTRAINT uq_favourites
    UNIQUE (user_id, spot_id)
);

CREATE TABLE reviews (
review_id BIGSERIAL PRIMARY KEY,

booking_id BIGINT NOT NULL UNIQUE,

author_id BIGINT NOT NULL,

rating INTEGER NOT NULL,

comment TEXT,

created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

CONSTRAINT fk_reviews_booking
    FOREIGN KEY (booking_id)
    REFERENCES bookings(booking_id)
    ON DELETE CASCADE,

CONSTRAINT fk_reviews_author
    FOREIGN KEY (author_id)
    REFERENCES users(user_id)
    ON DELETE CASCADE,

CONSTRAINT chk_rating
    CHECK (
        rating BETWEEN 1 AND 5
    )
);
