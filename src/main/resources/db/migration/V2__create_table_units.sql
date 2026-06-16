CREATE TABLE units (
                       id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       block       VARCHAR(10)  NOT NULL,
                       number      VARCHAR(10)  NOT NULL,
                       floor       INTEGER      NOT NULL,
                       deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
                       created_at  TIMESTAMP    NOT NULL,
                       updated_at  TIMESTAMP    NOT NULL
);