CREATE TABLE IF NOT EXISTS user_notification_management(
    id  CHARACTER VARYING(255)    NOT NULL,
    meeting_room_reservation   BOOLEAN DEFAULT TRUE,
    meeting_invitation      BOOLEAN DEFAULT TRUE,
    adding_slot                BOOLEAN DEFAULT TRUE,
    removing_slot             BOOLEAN DEFAULT TRUE,
    adding_participant         BOOLEAN DEFAULT TRUE,
    removing_participant      BOOLEAN DEFAULT TRUE,
    vote                    BOOLEAN DEFAULT TRUE,

    FOREIGN KEY (id) REFERENCES j_user(username)    ON DELETE CASCADE
)