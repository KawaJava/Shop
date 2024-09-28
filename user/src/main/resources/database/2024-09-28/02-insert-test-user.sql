--liquibase formatted sql
--changeset kawajava:4
INSERT INTO users (id, first_name, last_name, username, email, phone_number, password)
VALUES (1, 'Admin', 'Admin', 'admin', 'admin@example.com',
        '123456789', '$2a$12$z1lfb03DJRdUs2fa263ADOXZM1XMAGj5dlLXHpDdoWH.Wr/QTXhHK');

INSERT INTO authorities (username, authority)
VALUES ('admin', 'ROLE_ADMIN');
