CREATE TABLE IF NOT EXISTS slots
(
    slotId  character varying(255) UNIQUE NOT NULL ,
    startTime    TIMESTAMP              NOT NULL DEFAULT CURRENT_TIMESTAMP,
    endTime TIMESTAMP               NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (slotId)
);

CREATE TABLE IF NOT EXISTS meetings
(
    mid      character varying(255) UNIQUE NOT NULL,
    title    character varying(255)        NOT NULL,
    creator  character varying(255)        NOT NULL,
    roomId   INTEGER                           NULL,
    slotId   character varying(255)            NULL,
    state    INTEGER                       NOT NULL ,

    PRIMARY KEY (mid),
    FOREIGN KEY (creator) REFERENCES j_user(user_id),
    FOREIGN KEY (slotId) REFERENCES slots(slotId)
);

CREATE TABLE IF NOT EXISTS user_choices
(
    id  character varying(255)  UNIQUE NOT NULL ,
    userId  character varying(255)     NOT NULL ,
    slotId  character varying(255)     NOT NULL ,
    state   INTEGER                    NOT NULL ,

    PRIMARY KEY (id),
    FOREIGN KEY (userId) REFERENCES j_user(user_id) ON DELETE CASCADE ,
    FOREIGN KEY (slotId) REFERENCES slots(slotId) ON DELETE CASCADE
)