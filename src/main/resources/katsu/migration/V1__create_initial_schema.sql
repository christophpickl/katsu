CREATE TABLE client
(
    id        IDENTITY     NOT NULL PRIMARY KEY,
    firstName VARCHAR(128) NOT NULL,
    notes     TEXT         NOT NULL
);

CREATE TABLE treatment
(
    id    IDENTITY NOT NULL PRIMARY KEY,
    date  DATETIME NOT NULL,
    notes TEXT     NOT NULL
);

CREATE TABLE client_treatment
(
    client_id     INT NOT NULL,
    treatments_id INT NOT NULL
);
