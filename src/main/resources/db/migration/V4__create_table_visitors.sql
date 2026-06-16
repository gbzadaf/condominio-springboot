CREATE TABLE visitors (
                          id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          name        VARCHAR(100) NOT NULL,
                          document    VARCHAR(20)  NOT NULL UNIQUE,
                          phone       VARCHAR(20),
                          deleted     BOOLEAN      NOT NULL DEFAULT FALSE,
                          created_at  TIMESTAMP    NOT NULL,
                          updated_at  TIMESTAMP    NOT NULL
);