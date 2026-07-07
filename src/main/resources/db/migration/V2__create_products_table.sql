CREATE TABLE products
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL UNIQUE,
    description TEXT,
    price       NUMERIC(10, 2) NOT NULL CHECK (price >= 0)
);