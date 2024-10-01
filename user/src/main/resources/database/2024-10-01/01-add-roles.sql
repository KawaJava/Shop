--liquibase formatted sql
--changeset kawajava:10
ALTER TABLE users ADD COLUMN role VARCHAR(255) DEFAULT 'USER';

--changeset kawajava:11
UPDATE users SET role = 'USER' WHERE role IS NULL;

--changeset kawajava:12
ALTER TABLE users ALTER COLUMN role SET NOT NULL;