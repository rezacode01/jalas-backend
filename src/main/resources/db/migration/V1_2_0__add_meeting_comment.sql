CREATE TABLE IF NOT EXISTS comments(
    cid             character   varying(255)    NOT NULL ,
    user_id         character   varying(255)    NOT NULL ,
    meeting_id      character   varying(255)    NOT NULL ,
    message         character   varying(255)    NOT NULL ,
    creation_time   TIMESTAMP                   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reply_to        character   varying(255)    NULL ,

    PRIMARY KEY (cid) ,
    FOREIGN KEY (user_id) REFERENCES j_user(user_id) ON DELETE CASCADE ,
    FOREIGN KEY (meeting_id) REFERENCES meetings(mid) ON DELETE CASCADE ,
    FOREIGN KEY (reply_to) REFERENCES comments(cid) ON DELETE SET NULL
)
