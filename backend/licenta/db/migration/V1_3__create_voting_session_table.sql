CREATE TABLE IF NOT EXISTS voting_session (
       id                               BIGINT CONSTRAINT voting_session_pkey PRIMARY KEY,
       voting_session_public_key        TEXT NOT NULL,
       released_private_key             TEXT,
       created_at                       TIMESTAMP NOT NULL,
       updated_at                       TIMESTAMP NOT NULL,
       starting_at                      TIMESTAMP NOT NULL,
       ending_at                        TIMESTAMP NOT NULL,
       status                           TEXT NOT NULL
);

CREATE SEQUENCE voting_session_id_seq
    INCREMENT 1
    START 1;

CREATE UNIQUE INDEX one_row_only_uidx ON voting_session (( true ));
