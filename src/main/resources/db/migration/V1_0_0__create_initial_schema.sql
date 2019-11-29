CREATE TABLE IF NOT EXISTS j_user (
  user_id                              character varying(255)        NOT NULL,
  fullname                             character varying(255)        NOT NULL,
  username                             character varying(255) UNIQUE NOT NULL,
  password                             character varying(255)        NOT NULL,

  PRIMARY KEY (user_id)
);