CREATE TABLE IF NOT EXISTS voters (
                                      voter_id                           BIGINT CONSTRAINT voter_pkey PRIMARY KEY,
                                      public_key                   TEXT NOT NULL,
                                      has_voted                    BOOLEAN default FALSE
);

CREATE INDEX IF NOT EXISTS voter_index
    ON voters(public_key);

CREATE SEQUENCE voters_id_seq
    INCREMENT 1
    START 1;

CREATE SEQUENCE users_id_seq
    INCREMENT 1
    START 1;

CREATE TABLE users (
                                     user_id                      BIGINT CONSTRAINT user_pkey PRIMARY KEY,
                                     email                        TEXT NOT NULL,
                                     password                     TEXT NOT NULL,
                                     identity_card                TEXT NOT NULL,
                                     role                         TEXT NOT NULL,
                                     voter_id                     BIGINT NOT NULL,
                                     CONSTRAINT voter_id_fk
                                         FOREIGN KEY(voter_id)
                                         REFERENCES voters(voter_id)

);

