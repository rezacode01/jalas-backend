ALTER TABLE participants
    ADD CONSTRAINT multiple_participants UNIQUE (user_id, meeting_id);
