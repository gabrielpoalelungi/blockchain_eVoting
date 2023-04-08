CREATE SEQUENCE user_table_id_seq
    INCREMENT 1
    START 1;

CREATE TABLE user_table (
       id                           BIGINT CONSTRAINT user_table_pkey PRIMARY KEY,
       email                        TEXT NOT NULL,
       phone_number                 TEXT NOT NULL,
       password                     TEXT NOT NULL,
       hashed_identity_card         TEXT NOT NULL,
       role                         TEXT NOT NULL
);

