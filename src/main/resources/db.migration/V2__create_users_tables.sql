CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       wallet_id UUID NOT NULL,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       email VARCHAR(30) NOT NULL UNIQUE,
                       age INTEGER NOT NULL CHECK (age >= 18 AND age <= 100),
                       registration_date TIMESTAMP NOT NULL,
                       last_update_date TIMESTAMP,
                       phone BIGINT UNIQUE,
                       password VARCHAR(255) NOT NULL
);