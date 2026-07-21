CREATE TABLE orders
(
    id         BIGSERIAL PRIMARY KEY,
    created_at TIMESTAMP    NOT NULL,
    status     VARCHAR(50)  NOT NULL,
    client_id  BIGINT,

    CONSTRAINT fk_orders_client
        FOREIGN KEY (client_id) REFERENCES clients (id) ON DELETE SET NULL
);