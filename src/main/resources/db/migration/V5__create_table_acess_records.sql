CREATE TABLE access_records (
                                id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                visitor_id              UUID       NOT NULL REFERENCES visitors(id),
                                unit_id                 UUID       NOT NULL REFERENCES units(id),
                                authorizing_resident_id UUID       REFERENCES residents(id),
                                gatekeeper_id           UUID       NOT NULL REFERENCES users(id),
                                status                  VARCHAR(20)  NOT NULL,
                                entry_time              TIMESTAMP    NOT NULL,
                                exit_time               TIMESTAMP,
                                notes                   TEXT,
                                created_at              TIMESTAMP    NOT NULL,
                                updated_at              TIMESTAMP    NOT NULL
);