CREATE TYPE bankcard_status AS ENUM ('ACTIVE','BLOCKED','EXPIRED');
CREATE TYPE user_role AS ENUM ('ADMIN', 'USER');

CREATE TABLE bank_user
(
    user_id    BIGINT GENERATED ALWAYS AS IDENTITY,
    login      VARCHAR(255) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name  VARCHAR(255) NOT NULL,
    role       user_role    NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE bankcard
(
    card_id         BIGINT GENERATED ALWAYS AS IDENTITY,
    number          VARCHAR(16)                 NOT NULL UNIQUE,
    user_id         BIGINT REFERENCES bank_user (user_id),
    expiration_date TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    status          bankcard_status             NOT NULL,
    balance         NUMERIC(12, 2)              NOT NULL,
    block_requested BOOLEAN DEFAULT false,
    PRIMARY KEY (card_id)
);
