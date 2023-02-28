CREATE TABLE IF NOT EXISTS identity_cards (
                                      identity_card_id             BIGINT CONSTRAINT identity_card_pkey PRIMARY KEY,
                                      citizenship                  TEXT NOT NULL,
                                      cnp                          TEXT NOT NULL,
                                      id_card_number               TEXT NOT NULL,
                                      expiration_date              DATE NOT NULL
);

CREATE INDEX IF NOT EXISTS identity_card_index
    ON identity_cards(cnp);

CREATE TABLE IF NOT EXISTS voters (
                                      voter_id                     BIGINT CONSTRAINT voter_pkey PRIMARY KEY,
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

CREATE SEQUENCE identity_cards_id_seq
    INCREMENT 1
    START 1;

CREATE TABLE users (
                                     user_id                      BIGINT CONSTRAINT user_pkey PRIMARY KEY,
                                     email                        TEXT NOT NULL,
                                     password                     TEXT NOT NULL,
                                     role                         TEXT NOT NULL,
                                     voter_id                     BIGINT NOT NULL,
                                     identity_card_id             BIGINT NOT NULL,
                                     CONSTRAINT voter_id_fk
                                         FOREIGN KEY(voter_id)
                                         REFERENCES voters(voter_id),
                                     CONSTRAINT identity_card_id_fk
                                         FOREIGN KEY(identity_card_id)
                                             REFERENCES identity_cards(identity_card_id)

);

