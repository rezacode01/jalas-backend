ALTER TABLE slots
    ADD mid      character varying(255)        NOT NULL default '',
    ADD FOREIGN KEY (mid) REFERENCES meetings(mid) ON DELETE CASCADE;
