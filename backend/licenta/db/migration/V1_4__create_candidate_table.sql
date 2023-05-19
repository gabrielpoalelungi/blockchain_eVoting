CREATE TABLE IF NOT EXISTS candidate (
      id                               BIGINT CONSTRAINT candidate_pkey PRIMARY KEY,
      official_name                    TEXT NOT NULL,
      description                      TEXT,
      created_at                       TIMESTAMP NOT NULL,
      updated_at                       TIMESTAMP NOT NULL,
      number_of_votes                  BIGINT
);

CREATE SEQUENCE candidate_id_seq
    INCREMENT 1
    START 1;