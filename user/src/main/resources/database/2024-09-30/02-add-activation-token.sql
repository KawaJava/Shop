--liquibase formatted sql
--changeset kawajava:9
ALTER TABLE users ADD COLUMN activation_token VARCHAR(255);
--changeset kawajava:10
ALTER TABLE users ADD COLUMN activation_token_date TIMESTAMP NULL;