CREATE TABLE IF NOT EXISTS slots
(
    slot_id  character varying(255) UNIQUE NOT NULL ,
--     mid      character varying(255)        NOT NULL ,
    start_time    TIMESTAMP              NOT NULL,
    end_time      TIMESTAMP              NOT NULL,

    PRIMARY KEY (slot_id)
--     FOREIGN KEY (mid) REFERENCES meetings(mid) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS meetings
(
    mid      character varying(255) UNIQUE NOT NULL,
    title    character varying(255)        NOT NULL,
    creator  character varying(255)        NOT NULL,
    room_id  INTEGER                           NULL,
    slot_id   character varying(255)            NULL,
    state    INTEGER                       NOT NULL ,

    PRIMARY KEY (mid),
    FOREIGN KEY (creator) REFERENCES j_user(user_id),
    FOREIGN KEY (slot_id) REFERENCES slots(slot_id)
);

CREATE TABLE IF NOT EXISTS user_choices
(
    id  character varying(255)  UNIQUE NOT NULL ,
    user_id  character varying(255)     NOT NULL ,
    slot_id  character varying(255)     NOT NULL ,
    state   INTEGER                    NOT NULL ,

    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES j_user(user_id) ON DELETE CASCADE ,
    FOREIGN KEY (slot_id) REFERENCES slots(slot_id) ON DELETE CASCADE
)