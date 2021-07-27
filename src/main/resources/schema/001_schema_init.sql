CREATE SCHEMA IF NOT EXISTS healenium;

DROP TABLE IF EXISTS healenium.healenium_info;

CREATE TABLE healenium.healenium_info
(
    id          BIGSERIAL                 NOT NULL PRIMARY KEY,
    name        VARCHAR(256)              NOT NULL,
    description VARCHAR(256)              NOT NULL,
    active      BOOLEAN      DEFAULT TRUE NOT NULL
);

INSERT INTO healenium.healenium_info(name, description)
VALUES ('Healenium', 'Self-healing library for Selenium Web-based tests');
