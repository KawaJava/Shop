--liquibase formatted sql
--changeset kawajava:2
CREATE TABLE Authorities(
    username  VARCHAR(255) NOT NULL,
    authority VARCHAR(255) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES Users (username)
);
--changeset kawajava:3
CREATE UNIQUE INDEX ix_auth_username ON Authorities (username, authority);
