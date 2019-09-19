CREATE TABLE client
(
    id        IDENTITY     NOT NULL PRIMARY KEY,
    firstName VARCHAR(128) NOT NULL,
    note      TEXT         NOT NULL
);
