CREATE TABLE residents (
                           id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           name        VARCHAR(100) NOT NULL,
                           cpf         VARCHAR(14)  NOT NULL UNIQUE,
                           phone       VARCHAR(20)  NOT NULL UNIQUE,
                           unit_id     UUID      NOT NULL REFERENCES units(id),
                           user_id     UUID       REFERENCES users(id),
                           deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
                           created_at  TIMESTAMP    NOT NULL,
                           updated_at  TIMESTAMP    NOT NULL
);