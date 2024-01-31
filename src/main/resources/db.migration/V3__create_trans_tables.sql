CREATE TABLE transactions (
     id UUID PRIMARY KEY,
     amount BIGINT NOT NULL,
     transaction_date TIMESTAMP NOT NULL,
     maintenance_number BIGINT,
     status VARCHAR(50) NOT NULL,
     sender_wallet_id UUID,
     receiver_wallet_id UUID,
     FOREIGN KEY (sender_wallet_id) REFERENCES wallets (id),
     FOREIGN KEY (receiver_wallet_id) REFERENCES wallets (id)
);

CREATE TABLE maintenances (
     id UUID PRIMARY KEY,
     type VARCHAR(50) NOT NULL,
     amount BIGINT NOT NULL,
     status VARCHAR(50) NOT NULL,
     transaction_date DATE NOT NULL,
     sender_wallet_id UUID,
     receiver_wallet_id UUID,
     FOREIGN KEY (sender_wallet_id) REFERENCES wallets (id),
     FOREIGN KEY (receiver_wallet_id) REFERENCES wallets (id)
);
