--liquibase formatted sql
--changeset kawajava:5
ALTER TABLE users ADD hash VARCHAR(120);

--changeset kawajava:6
ALTER TABLE users ADD hash_date DATETIME;

INSERT INTO users (id, first_name, last_name, username, email, phone_number, password)
VALUES (2, 'Admin1', 'Admin1', 'admin1', 'admin1@example.com',
        '123456788', '$2a$12$z1lfb03DJRdUs2fa263ADOXZM1XMAGj5dlLXHpDdoWH.Wr/QTXhHK');

INSERT INTO authorities (username, authority)
VALUES ('admin1', 'ROLE_ADMIN');