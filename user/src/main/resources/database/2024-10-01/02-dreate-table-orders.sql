--liquibase formatted sql
--changeset kawajava:13
CREATE TABLE "orders" (

    id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    place_date DATETIME NOT NULL,
    order_status VARCHAR(32) NOT NULL,
    gross_value DECIMAL(6,2) NOT NULL,
    zipcode VARCHAR(6) NOT NULL,
    city VARCHAR(64) NOT NULL,
    street VARCHAR(80) NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
