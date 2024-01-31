CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    amount BIGINT NOT NULL,
    transaction_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    type VARCHAR(255) NOT NULL,
    receiver_phone BIGINT,
    maintenance_number BIGINT,
    status VARCHAR(255) NOT NULL,
    wallet_id UUID,
    FOREIGN KEY (wallet_id) REFERENCES wallets(id)
);
