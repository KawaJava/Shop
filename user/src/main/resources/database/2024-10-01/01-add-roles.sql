--liquibase formatted sql
--changeset person:10
ALTER TABLE users ADD COLUMN role VARCHAR(255) DEFAULT 'USER';

--changeset person:11
UPDATE users SET role = 'USER' WHERE role IS NULL;

--changeset person:12
ALTER TABLE users ALTER COLUMN role SET NOT NULL;