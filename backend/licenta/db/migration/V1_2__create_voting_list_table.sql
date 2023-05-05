CREATE TABLE IF NOT EXISTS voter (
      id                     BIGINT CONSTRAINT voter_pkey PRIMARY KEY,
      public_key             TEXT NOT NULL,
      private_key            TEXT NOT NULL,
      created_at             TIMESTAMP NOT NULL,
      user_id                BIGINT NOT NULL,
      is_registered          BOOLEAN NOT NULL DEFAULT FALSE,
      CONSTRAINT user_table_id_fk
          FOREIGN KEY(user_id)
              REFERENCES user_table(id)
);

CREATE SEQUENCE voter_id_seq
    INCREMENT 1
    START 1;