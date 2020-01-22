ALTER TABLE comments
    ADD CONSTRAINT comments_reply_to_fkey FOREIGN KEY (reply_to) REFERENCES comments ON DELETE CASCADE ;