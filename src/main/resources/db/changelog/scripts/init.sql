-- Optionally disable foreign key checks during table creation
USE my_app_db;




-- Drop tables if they exist (all lowercase)
-- DROP TABLE IF EXISTS address;
-- DROP TABLE IF EXISTS social_media;
-- DROP TABLE IF EXISTS report;
-- DROP TABLE IF EXISTS search_history;
-- DROP TABLE IF EXISTS prod_of_offer;
-- DROP TABLE IF EXISTS prod_of_cart;
-- DROP TABLE IF EXISTS rating;
-- DROP TABLE IF EXISTS rating_on_product;
-- DROP TABLE IF EXISTS rating_on_store;
-- DROP TABLE IF EXISTS report;
-- DROP TABLE IF EXISTS report_on_account;
-- DROP TABLE IF EXISTS report_on_product;
-- DROP TABLE IF EXISTS user_price;
-- DROP TABLE IF EXISTS special_offer;
-- DROP TABLE IF EXISTS store_price;
-- DROP TABLE IF EXISTS price;
-- DROP TABLE IF EXISTS shopping_cart;
-- DROP TABLE IF EXISTS product;
-- DROP TABLE IF EXISTS user_account;
-- DROP TABLE IF EXISTS store_account;
-- DROP TABLE IF EXISTS account;
-- DROP TABLE IF EXISTS refresh_token;
-- DROP TABLE IF EXISTS verification_code;
-- DROP TABLE IF EXISTS password_reset_token;
-- DROP TABLE IF EXISTS down_vote_on_user_price;
-- DROP TABLE IF EXISTS up_vote_on_user_price;
-- DROP TABLE IF EXISTS store_reference_by_user;
-- DROP TABLE IF EXISTS recommendation_log;


-- Create tables (all lowercase table names)

-- 1. account (referenced by search_history and report)
CREATE TABLE account (
    account_id bigint AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(255) unique,
    email VARCHAR(255),
    password VARCHAR(255),
    status VARCHAR(50),
    phone_number VARCHAR(20),
    image VARCHAR(255),
    roles varchar(100),
    oauth_id decimal(38,0),
    store_account_flag boolean,
    datetime_of_insert DATETIME
);
-- 3. user_account (referenced by shopping_cart, user_prices, rating)
CREATE TABLE user_account (
    account_id bigint PRIMARY KEY,
    fname VARCHAR(255),
    lname VARCHAR(255),
    score_of_activity INT,
    score_of_integrity INT,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
CREATE TABLE verification_code (
    id bigint auto_increment primary key,
    verification_code text,
    verification_code_expire_time datetime,
    account_id bigint,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- 2. store_account (referenced by user_prices, rating, social_media, address and linked to store_prices)
CREATE TABLE store_account (
    account_id bigint PRIMARY KEY,
    name VARCHAR(255),
    verified_flag BOOLEAN,
    reference_made_by_user_flag BOOLEAN,
    owner_proof_doc TEXT,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE store_reference_by_user (
    reference_id bigint AUTO_INCREMENT primary key,
    referencing_account_id bigint,
    referenced_store_id bigint,
    name VARCHAR(255),
    verified_flag BOOLEAN,
    reference_made_by_user_flag BOOLEAN,
    owner_proof_doc TEXT,
    FOREIGN KEY (referenced_store_id) REFERENCES store_account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (referencing_account_id) REFERENCES account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE password_reset_token (
    id bigint auto_increment primary key,
    token varchar(10000),
    expiry_date datetime,
    account_id bigint,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE refresh_token (
    id bigint auto_increment primary key,
    refresh_token varchar(10000),
    revoked boolean,
    account_id bigint,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);



-- 4. product (referenced by user_prices, rating, prod_of_cart, prod_of_offer)
CREATE TABLE product (
    product_id bigint AUTO_INCREMENT PRIMARY KEY,
    account_id bigint,
    name VARCHAR(255),
    brand VARCHAR(255),
    image VARCHAR(255),
    category VARCHAR(255),
    bar_code VARCHAR(255),
    description TEXT,
    datetime_of_insert DATETIME,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
        ON UPDATE SET NULL
        ON DELETE SET NULL
);

-- 5. shopping_cart (references user_account)
CREATE TABLE shopping_cart (
    shopping_cart_id bigint AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    datetime_of_insert DATETIME,
    description TEXT,
    image VARCHAR(255),
    public_private_flag BOOLEAN,
    user_account_id bigint,
    FOREIGN KEY (user_account_id) REFERENCES user_account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);



-- 6. store_price (references store_account and product)
CREATE TABLE store_price (
    price_id bigint PRIMARY KEY,
    quantity INT,
    FOREIGN KEY (price_id) REFERENCES price (price_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- 7. special_offer (references store_prices)
CREATE TABLE special_offer (
    offer_id bigint AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    description TEXT,
    image VARCHAR(255),
    datetime_of_start DATETIME,
    datetime_of_end DATETIME,
    datetime_of_insert DATETIME,
    price DECIMAL(10, 2),
    account_id bigint,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE price (
    price_id bigint AUTO_INCREMENT PRIMARY KEY,
    price DECIMAL(10, 2),
    datetime_of_insert DATETIME,
    currency VARCHAR(10),
    unit_of_measure VARCHAR(50),
    store_account_id bigint,
    product_id bigint,
    is_store_price boolean,
    FOREIGN KEY (store_account_id) REFERENCES store_account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product (product_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- 8. user_price
CREATE TABLE user_price (
    price_id bigint PRIMARY KEY,
    up_vote_count INT,
    down_vote_count INT,
    user_account_id bigint,
    FOREIGN KEY (user_account_id) REFERENCES user_account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (price_id) REFERENCES price (price_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE up_vote_on_user_price (
    id bigint auto_increment primary key,
    datetime_of_insert datetime DEFAULT CURRENT_TIMESTAMP,
    account_id bigint,
    price_id bigint,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (price_id) REFERENCES price (price_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE down_vote_on_user_price (
    id bigint auto_increment primary key,
    datetime_of_insert datetime DEFAULT CURRENT_TIMESTAMP,
    account_id bigint,
    price_id bigint,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (price_id) REFERENCES price (price_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- 9. rating (references user_account, product and store_account)
CREATE TABLE rating (
    rate_id bigint AUTO_INCREMENT PRIMARY KEY,
    stars_rate INT,
    feedback TEXT,
    datetime_of_insert DATETIME,
    account_id bigint,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE rating_on_product (
    rate_id bigint PRIMARY KEY,
    product_id bigint,
    FOREIGN KEY (product_id) REFERENCES product (product_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (rate_id) REFERENCES rating (rate_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE rating_on_store (
    rate_id bigint PRIMARY KEY,
    store_account_id bigint,
    FOREIGN KEY (store_account_id) REFERENCES store_account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (rate_id) REFERENCES rating (rate_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- 10. prod_of_cart (junction table between product and shopping_cart)
CREATE TABLE prod_of_cart (
    id bigint AUTO_INCREMENT PRIMARY KEY,
    product_id bigint,
    shopping_cart_id bigint,
    quantity INT,
    FOREIGN KEY (product_id) REFERENCES product (product_id)
        ON UPDATE SET NULL
        ON DELETE SET NULL,
    FOREIGN KEY (shopping_cart_id) REFERENCES shopping_cart (shopping_cart_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- 11. prod_of_offer (junction table between product and special_offers)
CREATE TABLE prod_of_offer (
    id bigint AUTO_INCREMENT PRIMARY KEY,
    product_id bigint,
    offer_id bigint,
    quantity INT,
    FOREIGN KEY (product_id) REFERENCES product (product_id)
        ON UPDATE SET NULL
        ON DELETE SET NULL,
    FOREIGN KEY (offer_id) REFERENCES special_offer (offer_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- 12. search_history (references account)
CREATE TABLE search_history (
    search_id bigint AUTO_INCREMENT PRIMARY KEY,
    search_data TEXT,
    datetime_of_insert DATETIME,
    account_id bigint,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

-- 13. report (references account)
CREATE TABLE report (
    report_id bigint AUTO_INCREMENT PRIMARY KEY,
    datetime_of_insert DATETIME,
    description TEXT,
    account_id bigint,
    screen_shot TEXT,
    FOREIGN KEY (account_id) REFERENCES account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE report_on_account (
    report_id bigint PRIMARY KEY,
    reported_account_id bigint,
    FOREIGN KEY (reported_account_id) REFERENCES account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (report_id) REFERENCES report (report_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE report_on_product (
    report_id bigint PRIMARY KEY,
    product_id bigint,
    FOREIGN KEY (product_id) REFERENCES product (product_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (report_id) REFERENCES report (report_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);

CREATE TABLE social_media (
    social_media_id bigint AUTO_INCREMENT PRIMARY KEY,
    social_media_account VARCHAR(255),
    store_account_id bigint,
    account_type VARCHAR(50),
    datetime_of_insert DATETIME,
    FOREIGN KEY (store_account_id) REFERENCES store_account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE

);

CREATE TABLE address (
    address_id bigint AUTO_INCREMENT PRIMARY KEY,
    latitude DECIMAL(11, 8),
    longitude DECIMAL(11, 8),
    address VARCHAR(255),
    store_account_id bigint,
    datetime_of_insert DATETIME,
    FOREIGN KEY (store_account_id) REFERENCES store_account (account_id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
CREATE TABLE recommendation_log (
    id bigint NOT NULL,
    user_id varchar(255) NOT NULL,
    product_id varchar(255) NOT NULL,
    score decimal(5,2) DEFAULT NULL,
    method varchar(50) DEFAULT NULL,
    timestamp datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;





