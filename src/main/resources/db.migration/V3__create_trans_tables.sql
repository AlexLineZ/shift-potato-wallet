CREATE TABLE transactions (
      id UUID PRIMARY KEY,
      amount BIGINT NOT NULL,
      transaction_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
      type VARCHAR(255) NOT NULL,
      maintenance_number BIGINT,
      receiver_phone BIGINT,
      status VARCHAR(255) NOT NULL,
      sender_wallet_id UUID,
      receiver_wallet_id UUID,
      FOREIGN KEY (sender_wallet_id) REFERENCES wallets(id),
      FOREIGN KEY (receiver_wallet_id) REFERENCES wallets(id)
);

CREATE TABLE maintenances (
      id UUID PRIMARY KEY,
      amount BIGINT NOT NULL,
      maintenance_number BIGINT NOT NULL,
      status VARCHAR(255) NOT NULL,
      transaction_date DATE NOT NULL,
      comment TEXT,
      sender_wallet_id UUID,
      receiver_wallet_id UUID,
      FOREIGN KEY (sender_wallet_id) REFERENCES wallets(id),
      FOREIGN KEY (receiver_wallet_id) REFERENCES wallets(id)
);