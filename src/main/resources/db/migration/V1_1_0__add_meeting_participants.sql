CREATE TABLE IF NOT EXISTS participants(
    pid             character   varying(255)    NOT NULL,
    user_id         character   varying(255)    NOT NULL,
    meeting_id      character   varying(255)    NOT NULL,
    creation_time   TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (pid) ,
    FOREIGN KEY (user_id) REFERENCES j_user(user_id) ON DELETE CASCADE,
    FOREIGN KEY (meeting_id) REFERENCES meetings(mid) ON DELETE CASCADE
)
