--liquibase formatted sql
--changeset kawajava:7
ALTER TABLE users ADD COLUMN is_active BOOLEAN DEFAULT true;
UPDATE users SET is_active = true;
--changeset kawajava:8
ALTER TABLE users ALTER COLUMN is_active SET NOT NULL;