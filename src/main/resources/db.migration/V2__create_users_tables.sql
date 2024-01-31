CREATE TABLE wallets (
                         id UUID PRIMARY KEY,
                         amount BIGINT NOT NULL,
                         last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       wallet_id UUID UNIQUE,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       email VARCHAR(30) UNIQUE NOT NULL,
                       age INTEGER NOT NULL CHECK (age >= 18 AND age <= 100),
                       registration_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       last_update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       phone BIGINT UNIQUE,
                       password VARCHAR NOT NULL,
                       FOREIGN KEY (wallet_id) REFERENCES wallets(id)
);