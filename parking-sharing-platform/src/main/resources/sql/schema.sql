--Users--
CREATE TABLE users
(
    user_id BIG SERIAL PRIMARY KEY,

    role VARCHAR(20) NOT NULL,

    name VARCHAR(50) NOT NULL,

    email VARCHAR(100) NOT NULL UNIQUE,

    phone VARCHAR(20) UNIQUE,

    password VARCHAR(255) NOT NULL,

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_users_role
        CHECK (role IN ('ADMIN','OWNER','DRIVER')),

    CONSTRAINT chk_users_status
        CHECK (status IN ('ACTIVE','BLOCKED','DELETED')),

    CONSTRAINT chk_users_email
        CHECK (POSITION('@' IN email) > 1)
);

--Address--
CREATE TABLE address
(
    address_id BIG SERIAL PRIMARY KEY,

    country VARCHAR(255) NOT NULL,

    city VARCHAR(255) NOT NULL,

    street VARCHAR(255) NOT NULL,

    house_number VARCHAR(10) NOT NULL,

    latitude DECIMAL(10,7),

    longitude DECIMAL(10,7)
);

--Parking--
CREATE TABLE parking
(
    parking_id BIG SERIAL PRIMARY KEY,

    owner_id BIGINT NOT NULL,

    address_id BIGINT NOT NULL,

    title VARCHAR(100) NOT NULL,

    description TEXT,

    hourly_rate DECIMAL(10,2) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_parking_owner
        FOREIGN KEY (owner_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_parking_address
        FOREIGN KEY (address_id)
        REFERENCES address(address_id),

    CONSTRAINT chk_hourly_rate
        CHECK (hourly_rate > 0)
);

--Parking Spot--
CREATE TABLE parking_spot
(
    spot_id BIG SERIAL PRIMARY KEY,

    parking_id BIGINT NOT NULL,

    spot_number INTEGER NOT NULL,

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_parking_spot_parking
        FOREIGN KEY (parking_id)
        REFERENCES parking(parking_id)
        ON DELETE CASCADE,

    CONSTRAINT uq_parking_spot
        UNIQUE (parking_id, spot_number),

    CONSTRAINT chk_spot_status
        CHECK (
            status IN (
                'AVAILABLE',
                'OCCUPIED',
                'OUT_OF_SERVICE'
            )
        )
);

--Bookings--
CREATE TABLE bookings
(
    booking_id BIG SERIAL PRIMARY KEY,

    driver_id BIGINT NOT NULL,

    spot_id BIGINT NOT NULL,

    start_time TIMESTAMP NOT NULL,

    end_time TIMESTAMP NOT NULL,

    total_price DECIMAL(10,2) NOT NULL,

    status VARCHAR(20) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_booking_driver
        FOREIGN KEY (driver_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_booking_spot
        FOREIGN KEY (spot_id)
        REFERENCES parking_spot(spot_id)
        ON DELETE CASCADE,

    CONSTRAINT chk_booking_time
        CHECK (end_time > start_time),

    CONSTRAINT chk_booking_price
        CHECK (total_price > 0),

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

--Reviews--
CREATE TABLE reviews
(
    review_id BIG SERIAL PRIMARY KEY,

    booking_id BIGINT NOT NULL UNIQUE,

    author_id BIGINT NOT NULL,

    rating INTEGER NOT NULL,

    comment TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_review_booking
        FOREIGN KEY (booking_id)
        REFERENCES bookings(booking_id)
        ON DELETE CASCADE,

    CONSTRAINT fk_review_author
        FOREIGN KEY (author_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE,

    CONSTRAINT chk_rating
        CHECK (rating BETWEEN 1 AND 5)
);

--Payments--
CREATE TABLE payments
(
    payment_id BIG SERIAL PRIMARY KEY,

    booking_id BIGINT NOT NULL UNIQUE,

    amount DECIMAL(10,2) NOT NULL,

    payment_method VARCHAR(20) NOT NULL,

    status VARCHAR(20) NOT NULL,

    transaction_id VARCHAR(255) UNIQUE NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payment_booking
        FOREIGN KEY (booking_id)
        REFERENCES bookings(booking_id)
        ON DELETE CASCADE,

    CONSTRAINT chk_payment_amount
        CHECK (amount > 0),

    CONSTRAINT chk_payment_method
        CHECK (payment_method = 'CARD'),

    CONSTRAINT chk_payment_status
    CHECK (
        status IN (
            'PAID',
            'DECLINED',
            'CANCELLED',
            'REFUNDED'
        )
    )
);

--Password reset token--
CREATE TABLE password_reset_token
(
    token_id BIG SERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    token VARCHAR(255) NOT NULL UNIQUE,

    expires_at TIMESTAMP NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    used BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_password_reset_user
        FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);
