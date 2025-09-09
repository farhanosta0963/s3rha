-- Optionally disable foreign key checks during table creation
USE my_app_db;

SET FOREIGN_KEY_CHECKS = 0;

-- Drop tables if they exist (all lowercase)
DROP TABLE IF EXISTS address;
DROP TABLE IF EXISTS social_media;
DROP TABLE IF EXISTS report;
DROP TABLE IF EXISTS search_history;
DROP TABLE IF EXISTS prod_of_offer;
DROP TABLE IF EXISTS prod_of_cart;
DROP TABLE IF EXISTS rating;
DROP TABLE IF EXISTS rating_on_product;
DROP TABLE IF EXISTS rating_on_store;
DROP TABLE IF EXISTS report;
DROP TABLE IF EXISTS report_on_account;
DROP TABLE IF EXISTS report_on_product;
DROP TABLE IF EXISTS user_price;
DROP TABLE IF EXISTS special_offer;
DROP TABLE IF EXISTS store_price;
DROP TABLE IF EXISTS price;
DROP TABLE IF EXISTS shopping_cart;
DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS user_account;
DROP TABLE IF EXISTS store_account;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS refresh_token;
DROP TABLE IF EXISTS verification_code;
DROP TABLE IF EXISTS password_reset_token;
DROP TABLE IF EXISTS down_vote_on_user_price;
DROP TABLE IF EXISTS up_vote_on_user_price;
DROP TABLE IF EXISTS store_reference_by_user;
DROP TABLE IF EXISTS recommendation_log;


-- Create tables (all lowercase table names)

CREATE TABLE IF NOT EXISTS init_flag (
    initialized BOOLEAN PRIMARY KEY
);

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

CREATE TABLE recommendation_log (
    id bigint NOT NULL,
    user_id varchar(255) NOT NULL,
    product_id varchar(255) NOT NULL,
    score decimal(5,2) DEFAULT NULL,
    method varchar(50) DEFAULT NULL,
    timestamp datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



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

-- 6. store_price (references store_account and product)
CREATE TABLE store_price (
    price_id bigint PRIMARY KEY,
    quantity INT,
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
CREATE INDEX idx_price_product_id ON price(product_id);

INSERT INTO `account` (`account_id`, `user_name`, `email`, `password`, `status`, `phone_number`, `image`, `roles`, `oauth_id`, `store_account_flag`, `datetime_of_insert`) VALUES
(1, 'user_c', 'user_c@example.com', 'pass_c', 'active', '555-1111', 'user_c.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:52:54'),
(2, 'user_d', 'user_d@example.com', 'pass_d', 'active', '555-2222', 'user_d.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:52:54'),
(3, 'user_e', 'user_e@example.com', 'pass_e', 'active', '555-3333', 'user_e.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:52:54'),
(4, 'user_f', 'user_f@example.com', 'pass_f', 'active', '555-4444', 'user_f.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:52:54'),
(5, 'user_g', 'user_g@example.com', 'pass_g', 'active', '555-5555', 'user_g.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:52:54'),
(6, 'user_h', 'user_h@example.com', 'pass_h', 'active', '555-6666', 'user_h.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:52:54'),
(7, 'user_i', 'user_i@example.com', 'pass_i', 'active', '555-7777', 'user_i.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:52:54'),
(8, 'user_j', 'user_j@example.com', 'pass_j', 'active', '555-8888', 'user_j.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:52:54'),
(9, 'user_k', 'user_k@example.com', 'pass_k', 'active', '555-9999', 'user_k.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:52:54'),
(10, 'user_l', 'user_l@example.com', 'pass_l', 'active', '555-0001', 'user_l.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:55:45'),
(11, 'user_m', 'user_m@example.com', 'pass_m', 'active', '555-0002', 'user_m.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:55:45'),
(12, 'user_n', 'user_n@example.com', '$2a$10$X5jMYN.NvO80KojjJPHinuPlqyTO.my2zI1Qs3cSGFXc5N4tsOEJu', 'active', '555-0003', 'user_n.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:55:45'),
(13, 'user_o', 'user_o@example.com', 'pass_o', 'active', '555-0004', 'user_o.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:55:45'),
(14, 'user_p', 'user_p@example.com', 'pass_p', 'active', '555-0005', 'user_p.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:55:45'),
(15, 'user_q', 'user_q@example.com', 'pass_q', 'active', '555-0006', 'user_q.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:55:45'),
(16, 'user_r', 'user_r@example.com', 'pass_r', 'active', '555-0007', 'user_r.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:55:45'),
(17, 'user_s', 'user_s@example.com', 'pass_s', 'active', '555-0008', 'user_s.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:55:45'),
(18, 'user_t', 'user_t@example.com', 'pass_t', 'active', '555-0009', 'user_t.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:55:45'),
(19, 'user_u', 'user_u@example.com', 'pass_u', 'active', '555-0010', 'user_u.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 16:55:45'),
(20, 'user_v', 'user_v@example.com', 'pass_v', 'active', '555-0011', 'user_v.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 17:13:27'),
(21, 'user_w', 'user_w@example.com', 'pass_w', 'active', '555-0012', 'user_w.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 17:13:27'),
(22, 'user_x', 'user_x@example.com', 'pass_x', 'active', '555-0013', 'user_x.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 17:13:27'),
(23, 'user_y', 'user_y@example.com', 'pass_y', 'active', '555-0014', 'user_y.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 17:13:27'),
(24, 'user_z', 'user_z@example.com', 'pass_z', 'active', '555-0015', 'user_z.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 17:13:27'),
(25, 'user_aa', 'user_aa@example.com', 'pass_aa', 'active', '555-0016', 'user_aa.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 17:13:27'),
(26, 'user_bb', 'user_bb@example.com', 'pass_bb', 'active', '555-0017', 'user_bb.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 17:13:27'),
(27, 'user_cc', 'user_cc@example.com', 'pass_cc', 'active', '555-0018', 'user_cc.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 17:13:27'),
(28, 'user_dd', 'user_dd@example.com', 'pass_dd', 'active', '555-0019', 'user_dd.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 17:13:27'),
(29, 'user_ee', 'user_ee@example.com', 'pass_ee', 'active', '555-0020', 'user_ee.jpg', 'ROLE_USER', NULL, 0, '2025-06-10 17:13:27'),
(30, 'store_a', 'store_a@example.com', 'pass_store_a', 'active', '555-0030', 'store_a.jpg', 'ROLE_STORE', NULL, 1, '2025-08-25 11:14:16'),
(31, 'store_b', 'store_b@example.com', 'pass_store_b', 'active', '555-0031', 'store_b.jpg', 'ROLE_STORE', NULL, 1, '2025-08-25 11:14:16'),
(32, 'store_c', 'store_c@example.com', 'pass_store_c', 'active', '555-0032', 'store_c.jpg', 'ROLE_STORE', NULL, 1, '2025-08-25 11:14:16'),
(33, 'store_d', 'store_d@example.com', 'pass_store_d', 'active', '555-0033', 'store_d.jpg', 'ROLE_STORE', NULL, 1, '2025-08-25 11:14:16'),
(34, 'store_e', 'store_e@example.com', 'pass_store_e', 'active', '555-0034', 'store_e.jpg', 'ROLE_STORE', NULL, 1, '2025-08-25 11:14:16'),
(35, 'new_user_1', 'new_user_1@example.com', 'pass_new_user_1', 'active', '555-0035', 'new_user_1.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(36, 'new_user_2', 'new_user_2@example.com', 'pass_new_user_2', 'active', '555-0036', 'new_user_2.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(37, 'new_user_3', 'new_user_3@example.com', 'pass_new_user_3', 'active', '555-0037', 'new_user_3.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(38, 'new_user_4', 'new_user_4@example.com', 'pass_new_user_4', 'active', '555-0038', 'new_user_4.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(39, 'new_user_5', 'new_user_5@example.com', 'pass_new_user_5', 'active', '555-0039', 'new_user_5.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(40, 'new_user_6', 'new_user_6@example.com', 'pass_new_user_6', 'active', '555-0040', 'new_user_6.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(41, 'new_user_7', 'new_user_7@example.com', 'pass_new_user_7', 'active', '555-0041', 'new_user_7.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(42, 'new_user_8', 'new_user_8@example.com', 'pass_new_user_8', 'active', '555-0042', 'new_user_8.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(43, 'new_user_9', 'new_user_9@example.com', 'pass_new_user_9', 'active', '555-0043', 'new_user_9.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(44, 'new_user_10', 'new_user_10@example.com', 'pass_new_user_10', 'active', '555-0044', 'new_user_10.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(45, 'new_user_11', 'new_user_11@example.com', 'pass_new_user_11', 'active', '555-0045', 'new_user_11.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(46, 'new_user_12', 'new_user_12@example.com', 'pass_new_user_12', 'active', '555-0046', 'new_user_12.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(47, 'new_user_13', 'new_user_13@example.com', 'pass_new_user_13', 'active', '555-0047', 'new_user_13.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(48, 'new_user_14', 'new_user_14@example.com', 'pass_new_user_14', 'active', '555-0048', 'new_user_14.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(49, 'new_user_15', 'new_user_15@example.com', 'pass_new_user_15', 'active', '555-0049', 'new_user_15.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(50, 'new_user_16', 'new_user_16@example.com', 'pass_new_user_16', 'active', '555-0050', 'new_user_16.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(51, 'new_user_17', 'new_user_17@example.com', 'pass_new_user_17', 'active', '555-0051', 'new_user_17.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(52, 'new_user_18', 'new_user_18@example.com', 'pass_new_user_18', 'active', '555-0052', 'new_user_18.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(53, 'new_user_19', 'new_user_19@example.com', 'pass_new_user_19', 'active', '555-0053', 'new_user_19.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(54, 'new_user_20', 'new_user_20@example.com', 'pass_new_user_20', 'active', '555-0054', 'new_user_20.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(55, 'new_user_21', 'new_user_21@example.com', 'pass_new_user_21', 'active', '555-0055', 'new_user_21.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(56, 'new_user_22', 'new_user_22@example.com', 'pass_new_user_22', 'active', '555-0056', 'new_user_22.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(57, 'new_user_23', 'new_user_23@example.com', 'pass_new_user_23', 'active', '555-0057', 'new_user_23.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(58, 'new_user_24', 'new_user_24@example.com', 'pass_new_user_24', 'active', '555-0058', 'new_user_24.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(59, 'new_user_25', 'new_user_25@example.com', 'pass_new_user_25', 'active', '555-0059', 'new_user_25.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(60, 'new_user_26', 'new_user_26@example.com', 'pass_new_user_26', 'active', '555-0060', 'new_user_26.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(61, 'new_user_27', 'new_user_27@example.com', 'pass_new_user_27', 'active', '555-0061', 'new_user_27.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(62, 'new_user_28', 'new_user_28@example.com', 'pass_new_user_28', 'active', '555-0062', 'new_user_28.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(63, 'new_user_29', 'new_user_29@example.com', 'pass_new_user_29', 'active', '555-0063', 'new_user_29.jpg', 'ROLE_USER', NULL, 0, '2025-08-25 12:00:00'),
(64, 'user_ff', 'user_ff@example.com', 'pass_ff', 'active', '555-0064', 'user_ff.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 11:00:00'),
(65, 'user_gg', 'user_gg@example.com', 'pass_gg', 'active', '555-0065', 'user_gg.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 11:05:00'),
(66, 'user_hh', 'user_hh@example.com', 'pass_hh', 'active', '555-0066', 'user_hh.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 11:10:00'),
(67, 'user_ii', 'user_ii@example.com', 'pass_ii', 'active', '555-0067', 'user_ii.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 11:15:00'),
(68, 'user_jj', 'user_jj@example.com', 'pass_jj', 'active', '555-0068', 'user_jj.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 11:20:00'),
(69, 'user_kk', 'user_kk@example.com', 'pass_kk', 'active', '555-0069', 'user_kk.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 15:00:00'),
(70, 'user_ll', 'user_ll@example.com', 'pass_ll', 'active', '555-0070', 'user_ll.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 15:05:00'),
(71, 'user_mm', 'user_mm@example.com', 'pass_mm', 'active', '555-0071', 'user_mm.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 15:10:00'),
(72, 'user_nn', 'user_nn@example.com', 'pass_nn', 'active', '555-0072', 'user_nn.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 15:15:00'),
(73, 'user_oo', 'user_oo@example.com', 'pass_oo', 'active', '555-0073', 'user_oo.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 15:20:00'),
(74, 'user_pp', 'user_pp@example.com', 'pass_pp', 'active', '555-0074', 'user_pp.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 18:00:00'),
(75, 'user_qq', 'user_qq@example.com', 'pass_qq', 'active', '555-0075', 'user_qq.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 18:05:00'),
(76, 'user_rr', 'user_rr@example.com', 'pass_rr', 'active', '555-0076', 'user_rr.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 18:10:00'),
(77, 'user_ss', 'user_ss@example.com', 'pass_ss', 'active', '555-0077', 'user_ss.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 18:15:00'),
(78, 'user_tt', 'user_tt@example.com', 'pass_tt', 'active', '555-0078', 'user_tt.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 18:20:00'),
(79, 'user_uu', 'user_uu@example.com', 'pass_uu', 'active', '555-0079', 'user_uu.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 18:25:00'),
(80, 'user_vv', 'user_vv@example.com', 'pass_vv', 'active', '555-0080', 'user_vv.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 18:30:00'),
(81, 'user_ww', 'user_ww@example.com', 'pass_ww', 'active', '555-0081', 'user_ww.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 18:35:00'),
(82, 'user_xx', 'user_xx@example.com', 'pass_xx', 'active', '555-0082', 'user_xx.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 18:40:00'),
(83, 'user_yy', 'user_yy@example.com', 'pass_yy', 'active', '555-0083', 'user_yy.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 18:45:00'),
(84, 'user_zz', 'user_zz@example.com', 'pass_zz', 'active', '555-0084', 'user_zz.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 14:00:00'),
(85, 'user_aa2', 'user_aa2@example.com', 'pass_aa2', 'active', '555-0085', 'user_aa2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 14:05:00'),
(86, 'user_bb2', 'user_bb2@example.com', 'pass_bb2', 'active', '555-0086', 'user_bb2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 14:10:00'),
(87, 'user_cc2', 'user_cc2@example.com', 'pass_cc2', 'active', '555-0087', 'user_cc2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 14:15:00'),
(88, 'user_dd2', 'user_dd2@example.com', 'pass_dd2', 'active', '555-0088', 'user_dd2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 14:20:00'),
(89, 'user_ee2', 'user_ee2@example.com', 'pass_ee2', 'active', '555-0089', 'user_ee2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 14:25:00'),
(90, 'user_ff2', 'user_ff2@example.com', 'pass_ff2', 'active', '555-0090', 'user_ff2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 14:30:00'),
(91, 'user_gg2', 'user_gg2@example.com', 'pass_gg2', 'active', '555-0091', 'user_gg2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 14:35:00'),
(92, 'user_hh2', 'user_hh2@example.com', 'pass_hh2', 'active', '555-0092', 'user_hh2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 14:40:00'),
(93, 'user_ii2', 'user_ii2@example.com', 'pass_ii2', 'active', '555-0093', 'user_ii2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 14:45:00'),
(94, 'user_jj2', 'user_jj2@example.com', 'pass_jj2', 'active', '555-0094', 'user_jj2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 14:50:00'),
(95, 'user_kk2', 'user_kk2@example.com', 'pass_kk2', 'active', '555-0095', 'user_kk2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 14:55:00'),
(96, 'user_ll2', 'user_ll2@example.com', 'pass_ll2', 'active', '555-0096', 'user_ll2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 15:00:00'),
(97, 'user_mm2', 'user_mm2@example.com', 'pass_mm2', 'active', '555-0097', 'user_mm2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 15:05:00'),
(98, 'user_nn2', 'user_nn2@example.com', 'pass_nn2', 'active', '555-0098', 'user_nn2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 15:10:00'),
(99, 'user_oo2', 'user_oo2@example.com', 'pass_oo2', 'active', '555-0099', 'user_oo2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 15:15:00'),
(100, 'user_pp2', 'user_pp2@example.com', 'pass_pp2', 'active', '555-0100', 'user_pp2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 15:20:00'),
(101, 'user_qq2', 'user_qq2@example.com', 'pass_qq2', 'active', '555-0101', 'user_qq2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 15:25:00'),
(102, 'user_rr2', 'user_rr2@example.com', 'pass_rr2', 'active', '555-0102', 'user_rr2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 15:30:00'),
(103, 'user_ss2', 'user_ss2@example.com', 'pass_ss2', 'active', '555-0103', 'user_ss2.jpg', 'ROLE_USER', NULL, 0, '2025-09-06 15:35:00');


INSERT INTO `user_account` (`account_id`, `fname`, `lname`, `score_of_activity`, `score_of_integrity`) VALUES
(64, 'FirstFF', 'LastFF', 85, 90),
(65, 'FirstGG', 'LastGG', 78, 82),
(66, 'FirstHH', 'LastHH', 92, 95),
(67, 'FirstII', 'LastII', 70, 75),
(68, 'FirstJJ', 'LastJJ', 88, 91),
(69, 'FirstKK', 'LastKK', 82, 85),
(70, 'FirstLL', 'LastLL', 90, 92),
(71, 'FirstMM', 'LastMM', 75, 78),
(72, 'FirstNN', 'LastNN', 88, 90),
(73, 'FirstOO', 'LastOO', 70, 73),
(74, 'FirstPP', 'LastPP', 85, 88),
(75, 'FirstQQ', 'LastQQ', 90, 92),
(76, 'FirstRR', 'LastRR', 65, 70),
(77, 'FirstSS', 'LastSS', 95, 98),
(78, 'FirstTT', 'LastTT', 75, 78),
(79, 'FirstUU', 'LastUU', 82, 85),
(80, 'FirstVV', 'LastVV', 88, 90),
(81, 'FirstWW', 'LastWW', 70, 73),
(82, 'FirstXX', 'LastXX', 92, 95),
(83, 'FirstYY', 'LastYY', 78, 80),
(84, 'FirstZZ', 'LastZZ', 87, 89),
(85, 'FirstAA2', 'LastAA2', 91, 93),
(86, 'FirstBB2', 'LastBB2', 68, 72),
(87, 'FirstCC2', 'LastCC2', 94, 96),
(88, 'FirstDD2', 'LastDD2', 76, 79),
(89, 'FirstEE2', 'LastEE2', 83, 86),
(90, 'FirstFF2', 'LastFF2', 89, 91),
(91, 'FirstGG2', 'LastGG2', 71, 74),
(92, 'FirstHH2', 'LastHH2', 93, 95),
(93, 'FirstII2', 'LastII2', 79, 81),
(94, 'FirstJJ2', 'LastJJ2', 85, 88),
(95, 'FirstKK2', 'LastKK2', 90, 92),
(96, 'FirstLL2', 'LastLL2', 70, 73),
(97, 'FirstMM2', 'LastMM2', 88, 90),
(98, 'FirstNN2', 'LastNN2', 75, 77),
(99, 'FirstOO2', 'LastOO2', 92, 94),
(100, 'FirstPP2', 'LastPP2', 80, 82),
(101, 'FirstQQ2', 'LastQQ2', 86, 89),
(102, 'FirstRR2', 'LastRR2', 95, 97),
(103, 'FirstSS2', 'LastSS2', 73, 76);

-- --------------------------------------------------------
INSERT INTO `product` (`product_id`, `account_id`, `name`, `brand`, `image`, `category`, `bar_code`, `description`, `datetime_of_insert`) VALUES
(1, 1, 'Laptop XYZ', 'BrandA', 'https://images.pexels.com/photos/812264/pexels-photo-812264.jpeg', 'Electronics', '123456789', 'High-performance laptop with fast processor', '2025-09-06 10:00:00'),
(2, 2, 'Smartphone ABC', 'BrandB', 'https://images.pexels.com/photos/1786433/pexels-photo-1786433.jpeg', 'Electronics', '987654321', 'Latest smartphone with advanced features', '2025-09-06 10:05:00'),
(3, 3, 'Book: Python Programming', 'AuthorC', 'https://placehold.co/400x300/F0F0F0/333333?text=Python+Book', 'Books', '111222333', 'Beginner guide to Python coding skills', '2025-09-06 10:10:00'),
(4, 4, 'Coffee Maker', 'BrandD', 'https://images.pexels.com/photos/20768096/pexels-photo-20768096.jpeg', 'Home Appliances', '444555666', 'Automatic coffee maker with timer function', '2025-09-06 10:15:00'),
(5, 5, 'Running Shoes', 'BrandE', 'https://placehold.co/400x300/F0F0F0/333333?text=Running+Shoes', 'Sports', '777888999', 'Comfortable running shoes with cushion support', '2025-09-06 10:20:00'),
(6, 30, 'Wireless Headphones', 'BrandF', 'https://placehold.co/400x300/F0F0F0/333333?text=Headphones', 'Electronics', '123123123', 'Noise-cancelling headphones with long battery', '2025-09-06 10:25:00'),
(7, 31, 'Fitness Tracker', 'BrandG', 'https://placehold.co/400x300/F0F0F0/333333?text=Fitness+Tracker', 'Wearables', '456456456', 'Track your fitness with smart features', '2025-09-06 10:30:00'),
(8, 32, 'Novel: Sci-Fi Adventure', 'AuthorH', 'https://placehold.co/400x300/F0F0F0/333333?text=Sci-Fi+Novel', 'Books', '789789789', 'Exciting sci-fi story with thrilling plot', '2025-09-06 10:35:00'),
(9, 33, 'Blender', 'BrandI', 'https://images.pexels.com/photos/17890637/pexels-photo-17890637.jpeg', 'Home Appliances', '147147147', 'High-speed blender with multiple settings', '2025-09-06 10:40:00'),
(10, 34, 'Yoga Mat', 'BrandJ', 'https://images.pexels.com/photos/4325462/pexels-photo-4325462.jpeg', 'Sports', '258258258', 'Non-slip yoga mat with thick padding', '2025-09-06 10:45:00'),
(11, 45, 'Tablet DEF', 'BrandK', 'https://placehold.co/400x300/F0F0F0/333333?text=Tablet', 'Electronics', '369369369', 'Portable tablet with high-resolution screen', '2025-09-06 15:00:00'),
(12, 46, 'Camera GHI', 'BrandL', 'https://images.pexels.com/photos/51383/photo-camera-subject-photographer-51383.jpeg', 'Electronics', '258147369', 'Professional camera with zoom lens', '2025-09-06 15:05:00'),
(13, 47, 'Cookbook: Healthy Recipes', 'AuthorM', 'https://placehold.co/400x300/F0F0F0/333333?text=Cookbook', 'Books', '963852741', 'Guide to healthy and easy recipes', '2025-09-06 15:10:00'),
(14, 48, 'Vacuum Cleaner', 'BrandN', 'https://placehold.co/400x300/F0F0F0/333333?text=Vacuum', 'Home Appliances', '147258369', 'Powerful vacuum with HEPA filter', '2025-09-06 15:15:00'),
(15, 49, 'Bicycle Helmet', 'BrandO', 'https://placehold.co/400x300/F0F0F0/333333?text=Helmet', 'Sports', '852963741', 'Safe helmet with adjustable straps', '2025-09-06 15:20:00'),
(16, 50, 'Smart Watch', 'BrandP', 'https://placehold.co/400x300/F0F0F0/333333?text=Smart+Watch', 'Wearables', '741852963', 'Smart watch with heart rate monitor', '2025-09-06 15:25:00'),
(17, 51, 'Earbuds', 'BrandQ', 'https://images.pexels.com/photos/248510/pexels-photo-248510.jpeg', 'Electronics', '369258147', 'Wireless earbuds with clear sound', '2025-09-06 15:30:00'),
(18, 52, 'Mystery Novel', 'AuthorR', 'https://placehold.co/400x300/F0F0F0/333333?text=Mystery+Novel', 'Books', '147369258', 'Thrilling mystery with unexpected twists', '2025-09-06 15:35:00'),
(19, 53, 'Toaster Oven', 'BrandS', 'https://placehold.co/400x300/F0F0F0/333333?text=Toaster+Oven', 'Home Appliances', '963741852', 'Compact toaster oven with multiple functions', '2025-09-06 15:40:00'),
(20, 54, 'Tennis Racket', 'BrandT', 'https://placehold.co/400x300/F0F0F0/333333?text=Tennis+Racket', 'Sports', '258369147', 'Lightweight racket for better control', '2025-09-06 15:45:00'),
(21, 55, 'Gaming Mouse', 'BrandU', 'https://placehold.co/400x300/F0F0F0/333333?text=Gaming+Mouse', 'Electronics', '147258369', 'Ergonomic gaming mouse with RGB lighting', '2025-09-06 19:00:00'),
(22, 56, 'History Book', 'AuthorV', 'https://placehold.co/400x300/F0F0F0/333333?text=History+Book', 'Books', '258369147', 'Detailed history of ancient civilizations', '2025-09-06 19:05:00'),
(23, 57, 'Basketball', 'BrandW', 'https://placehold.co/400x300/F0F0F0/333333?text=Basketball', 'Sports', '369147258', 'Durable basketball with good grip', '2025-09-06 19:10:00'),
(24, 58, 'Microwave Oven', 'BrandX', 'https://placehold.co/400x300/F0F0F0/333333?text=Microwave', 'Home Appliances', '147369258', 'Compact microwave with quick defrost', '2025-09-06 19:15:00'),
(25, 59, 'T-Shirt', 'BrandY', 'https://placehold.co/400x300/F0F0F0/333333?text=T-Shirt', 'Fashion', '258147369', 'Comfortable cotton t-shirt in various colors', '2025-09-06 19:20:00'),
(26, 60, 'Protein Bar', 'BrandZ', 'https://placehold.co/400x300/F0F0F0/333333?text=Protein+Bar', 'Food', '369258147', 'Nutritious protein bar with chocolate flavor', '2025-09-06 19:25:00'),
(27, 61, 'External Hard Drive', 'BrandAA', 'https://placehold.co/400x300/F0F0F0/333333?text=Hard+Drive', 'Electronics', '147147147', 'Portable hard drive with large storage', '2025-09-06 19:30:00'),
(28, 62, 'Fantasy Novel', 'AuthorBB', 'https://placehold.co/400x300/F0F0F0/333333?text=Fantasy+Novel', 'Books', '258258258', 'Epic fantasy adventure with magical elements', '2025-09-06 19:35:00'),
(29, 63, 'Soccer Ball', 'BrandCC', 'https://placehold.co/400x300/F0F0F0/333333?text=Soccer+Ball', 'Sports', '369369369', 'Professional soccer ball with durable material', '2025-09-06 19:40:00'),
(30, 30, 'Air Fryer', 'BrandDD', 'https://placehold.co/400x300/F0F0F0/333333?text=Air+Fryer', 'Home Appliances', '147258147', 'Healthy air fryer with easy controls', '2025-09-06 19:45:00'),
(31, 31, 'Jeans', 'BrandEE', 'https://placehold.co/400x300/F0F0F0/333333?text=Jeans', 'Fashion', '258369258', 'Slim-fit jeans with stretch fabric', '2025-09-06 19:50:00'),
(32, 32, 'Energy Drink', 'BrandFF', 'https://placehold.co/400x300/F0F0F0/333333?text=Energy+Drink', 'Food', '369147147', 'Refreshing energy drink with vitamins', '2025-09-06 19:55:00'),
(33, 33, 'Wireless Keyboard', 'BrandGG', 'https://placehold.co/400x300/F0F0F0/333333?text=Keyboard', 'Electronics', '147369147', 'Compact wireless keyboard with backlit keys', '2025-09-06 20:00:00'),
(34, 34, 'Biography Book', 'AuthorHH', 'https://placehold.co/400x300/F0F0F0/333333?text=Biography+Book', 'Books', '258147147', 'Inspiring biography of famous leaders', '2025-09-06 20:05:00'),
(35, 35, 'Tennis Shoes', 'BrandII', 'https://placehold.co/400x300/F0F0F0/333333?text=Tennis+Shoes', 'Sports', '369258258', 'Lightweight tennis shoes with shock absorption', '2025-09-06 20:10:00'),
(36, 36, 'Iron', 'BrandJJ', 'https://placehold.co/400x300/F0F0F0/333333?text=Iron', 'Home Appliances', '147147258', 'Steam iron with non-stick soleplate', '2025-09-06 20:15:00'),
(37, 37, 'Hat', 'BrandKK', 'https://placehold.co/400x300/F0F0F0/333333?text=Hat', 'Fashion', '258258369', 'Stylish hat with adjustable strap', '2025-09-06 20:20:00'),
(38, 38, 'Granola Bar', 'BrandLL', 'https://images.pexels.com/photos/3065512/pexels-photo-3065512.jpeg', 'Food', '369369147', 'Healthy granola bar with nuts and fruits', '2025-09-06 20:25:00'),
(39, 39, 'Monitor', 'BrandMM', 'https://placehold.co/400x300/F0F0F0/333333?text=Monitor', 'Electronics', '147258258', 'High-resolution monitor with slim bezels', '2025-09-06 20:30:00'),
(40, 40, 'Cookbook: Desserts', 'AuthorNN', 'https://placehold.co/400x300/F0F0F0/333333?text=Dessert+Cookbook', 'Books', '258369369', 'Delicious dessert recipes for beginners', '2025-09-06 20:35:00'),
(41, 41, 'Smart TV', 'BrandNN', 'https://images.pexels.com/photos/5202925/pexels-photo-5202925.jpeg', 'Electronics', '741741741', 'Smart TV with high-resolution 4K display', '2025-09-06 16:00:00'),
(42, 42, 'Sci-Fi Book', 'AuthorOO', 'https://placehold.co/400x300/F0F0F0/333333?text=Sci-Fi+Book', 'Books', '852852852', 'Engaging sci-fi book with futuristic plot', '2025-09-06 16:05:00'),
(43, 43, 'Football Gloves', 'BrandPP', 'https://placehold.co/400x300/F0F0F0/333333?text=Football+Gloves', 'Sports', '963963963', 'Durable football gloves with grip enhancement', '2025-09-06 16:10:00'),
(44, 44, 'Blender Pro', 'BrandQQ', 'https://placehold.co/400x300/F0F0F0/333333?text=Blender+Pro', 'Home Appliances', '147147369', 'Powerful blender pro with multiple settings', '2025-09-06 16:15:00'),
(45, 45, 'Leather Jacket', 'BrandRR', 'https://placehold.co/400x300/F0F0F0/333333?text=Leather+Jacket', 'Fashion', '258258741', 'Stylish leather jacket with warm lining', '2025-09-06 16:20:00'),
(46, 46, 'Energy Drink Mix', 'BrandSS', 'https://placehold.co/400x300/F0F0F0/333333?text=Drink+Mix', 'Food', '369369852', 'Refreshing energy drink mix with vitamins', '2025-09-06 16:25:00'),
(47, 47, 'Wireless Router', 'BrandTT', 'https://placehold.co/400x300/F0F0F0/333333?text=Router', 'Electronics', '147258852', 'High-speed wireless router with wide range', '2025-09-06 16:30:00'),
(48, 48, 'Poetry Book', 'AuthorUU', 'https://placehold.co/400x300/F0F0F0/333333?text=Poetry+Book', 'Books', '258369963', 'Beautiful poetry book with emotional verses', '2025-09-06 16:35:00'),
(49, 49, 'Running Shorts', 'BrandVV', 'https://images.pexels.com/photos/3766201/pexels-photo-3766201.jpeg', 'Sports', '369147741', 'Lightweight running shorts with breathable fabric', '2025-09-06 16:40:00'),
(50, 50, 'Electric Kettle', 'BrandWW', 'https://placehold.co/400x300/F0F0F0/333333?text=Electric+Kettle', 'Home Appliances', '147369852', 'Fast electric kettle with auto shut-off', '2025-09-06 16:45:00'),
(51, 51, 'Sunglasses', 'BrandXX', 'https://placehold.co/400x300/F0F0F0/333333?text=Sunglasses', 'Fashion', '258147963', 'Trendy sunglasses with UV protection lenses', '2025-09-06 16:50:00'),
(52, 52, 'Healthy Snack', 'BrandYY', 'https://images.pexels.com/photos/3065512/pexels-photo-3065512.jpeg', 'Food', '369258741', 'Healthy snack with natural ingredients mix', '2025-09-06 16:55:00'),
(53, 53, 'Gaming Console', 'BrandZZ', 'https://images.pexels.com/photos/5202925/pexels-photo-5202925.jpeg', 'Electronics', '147147963', 'Advanced gaming console with immersive graphics', '2025-09-06 17:00:00'),
(54, 54, 'Travel Guide', 'AuthorAAA', 'https://placehold.co/400x300/F0F0F0/333333?text=Travel+Guide', 'Books', '258258852', 'Comprehensive travel guide with destination tips', '2025-09-06 17:05:00'),
(55, 55, 'Yoga Pants', 'BrandBBB', 'https://placehold.co/400x300/F0F0F0/333333?text=Yoga+Pants', 'Sports', '369369741', 'Comfortable yoga pants with stretch fabric', '2025-09-06 17:10:00'),
(56, 56, 'Toaster Deluxe', 'BrandCCC', 'https://placehold.co/400x300/F0F0F0/333333?text=Toaster', 'Home Appliances', '147258963', 'Deluxe toaster with multiple cooking options', '2025-09-06 17:15:00'),
(57, 57, 'Watch Strap', 'BrandDDD', 'https://placehold.co/400x300/F0F0F0/333333?text=Watch+Strap', 'Fashion', '258369852', 'Durable watch strap with adjustable fit', '2025-09-06 17:20:00'),
(58, 58, 'Nut Mix', 'BrandEEE', 'https://placehold.co/400x300/F0F0F0/333333?text=Nut+Mix', 'Food', '369147963', 'Healthy nut mix with rich flavors', '2025-09-06 17:25:00'),
(59, 59, 'Bluetooth Speaker', 'BrandFFF', 'https://placehold.co/400x300/F0F0F0/333333?text=Speaker', 'Electronics', '147369741', 'Portable Bluetooth speaker with rich sound', '2025-09-06 17:30:00'),
(60, 60, 'Self-Help Book', 'AuthorGGG', 'https://placehold.co/400x300/F0F0F0/333333?text=Self-Help+Book', 'Books', '258147852', 'Inspiring self-help book with practical advice', '2025-09-06 17:35:00'),
(61, 61, 'Swimming Goggles', 'BrandHHH', 'https://placehold.co/400x300/F0F0F0/333333?text=Goggles', 'Sports', '369258963', 'Waterproof swimming goggles with clear vision', '2025-09-06 17:40:00'),
(62, 62, 'Pressure Cooker', 'BrandIII', 'https://placehold.co/400x300/F0F0F0/333333?text=Pressure+Cooker', 'Home Appliances', '147147852', 'Efficient pressure cooker with safety features', '2025-09-06 17:45:00'),
(63, 63, 'Scarf', 'BrandJJJ', 'https://placehold.co/400x300/F0F0F0/333333?text=Scarf', 'Fashion', '258258963', 'Warm scarf with elegant design patterns', '2025-09-06 17:50:00'),
(64, 64, 'Fruit Smoothie Mix', 'BrandKKK', 'https://images.pexels.com/photos/3065512/pexels-photo-3065512.jpeg', 'Food', '369369852', 'Tasty fruit smoothie mix with vitamins', '2025-09-06 17:55:00'),
(65, 65, 'Smart Watch Pro', 'BrandLLL', 'https://placehold.co/400x300/F0F0F0/333333?text=Smart+Watch+Pro', 'Wearables', '147258741', 'Smart watch pro with fitness tracking', '2025-09-06 18:00:00'),
(66, 66, 'Headphones Pro', 'BrandMMM', 'https://placehold.co/400x300/F0F0F0/333333?text=Headphones+Pro', 'Electronics', '258147369', 'Professional headphones with noise cancellation', '2025-09-06 18:05:00'),
(67, 67, 'Cooking Book', 'AuthorNNN', 'https://images.pexels.com/photos/3065512/pexels-photo-3065512.jpeg', 'Books', '369258741', 'Detailed cooking book with recipe variety', '2025-09-06 18:10:00'),
(68, 68, 'Bicycle Pump', 'BrandOOO', 'https://placehold.co/400x300/F0F0F0/333333?text=Bicycle+Pump', 'Sports', '147369852', 'Portable bicycle pump with pressure gauge', '2025-09-06 18:15:00'),
(69, 69, 'Waffle Maker', 'BrandPPP', 'https://placehold.co/400x300/F0F0F0/333333?text=Waffle+Maker', 'Home Appliances', '258147963', 'Electric waffle maker with non-stick surface', '2025-09-06 18:20:00'),
(70, 70, 'Backpack', 'BrandQQQ', 'https://placehold.co/400x300/F0F0F0/333333?text=Backpack', 'Fashion', '369369741', 'Stylish backpack with multiple compartments', '2025-09-06 18:25:00');


INSERT INTO `rating` (`rate_id`, `stars_rate`, `feedback`, `datetime_of_insert`, `account_id`) VALUES
(645, 4, 'جيد', '2025-09-06 18:00:00', 1),
(646, 5, 'ممتاز', '2025-09-06 18:01:00', 2),
(647, 3, 'مقبول', '2025-09-06 18:02:00', 3),
(648, 4, 'جيد جدًا', '2025-09-06 18:03:00', 4),
(649, 5, 'رائع', '2025-09-06 18:04:00', 5),
(650, 4, 'جيد', '2025-09-06 18:05:00', 6),
(651, 3, 'مقبول', '2025-09-06 18:06:00', 7),
(652, 5, 'ممتاز', '2025-09-06 18:07:00', 8),
(653, 4, 'جيد جدًا', '2025-09-06 18:08:00', 9),
(654, 3, 'مقبول', '2025-09-06 18:09:00', 10),
(655, 5, 'رائع', '2025-09-06 18:10:00', 11),
(656, 4, 'جيد', '2025-09-06 18:11:00', 12),
(657, 5, 'ممتاز', '2025-09-06 18:12:00', 13),
(658, 3, 'مقبول', '2025-09-06 18:13:00', 14),
(659, 4, 'جيد جدًا', '2025-09-06 18:14:00', 15),
(660, 5, 'رائع', '2025-09-06 18:15:00', 16),
(661, 4, 'جيد', '2025-09-06 18:16:00', 17),
(662, 3, 'مقبول', '2025-09-06 18:17:00', 18),
(663, 5, 'ممتاز', '2025-09-06 18:18:00', 19),
(664, 4, 'جيد جدًا', '2025-09-06 18:19:00', 20),
(763, 5, 'Excellent product!', '2025-09-06 12:00:00', 1),
(764, 4, 'Good value', '2025-09-06 12:05:00', 2),
(765, 3, 'Average', '2025-09-06 12:10:00', 3),
(766, 5, 'Highly recommend', '2025-09-06 12:15:00', 4),
(767, 4, 'Satisfied', '2025-09-06 12:20:00', 5),
(768, 2, 'Not as expected', '2025-09-06 12:25:00', 6),
(769, 5, 'Perfect', '2025-09-06 12:30:00', 7),
(770, 4, 'Decent', '2025-09-06 12:35:00', 8),
(771, 3, 'Okay', '2025-09-06 12:40:00', 9),
(772, 5, 'Love it', '2025-09-06 12:45:00', 10),
(773, 4, 'Good', '2025-09-06 12:50:00', 35),
(774, 5, 'Awesome', '2025-09-06 12:55:00', 36),
(775, 3, 'Fair', '2025-09-06 13:00:00', 37),
(776, 4, 'Nice', '2025-09-06 13:05:00', 38),
(777, 5, 'Great', '2025-09-06 13:10:00', 39),
(778, 2, 'Disappointing', '2025-09-06 13:15:00', 40),
(779, 5, 'Superb', '2025-09-06 13:20:00', 41),
(780, 4, 'Solid', '2025-09-06 13:25:00', 42),
(781, 3, 'Mediocre', '2025-09-06 13:30:00', 43),
(782, 5, 'Fantastic', '2025-09-06 13:35:00', 44),
(783, 4, 'Reliable', '2025-09-06 13:40:00', 64),
(784, 5, 'Outstanding', '2025-09-06 13:45:00', 65),
(785, 3, 'Passable', '2025-09-06 13:50:00', 66),
(786, 4, 'Acceptable', '2025-09-06 13:55:00', 67),
(787, 5, 'Exceptional', '2025-09-06 14:00:00', 68),
(813, 5, 'Amazing device', '2025-09-06 16:00:00', 45),
(814, 4, 'Reliable performance', '2025-09-06 16:05:00', 46),
(815, 3, 'Standard quality', '2025-09-06 16:10:00', 47),
(816, 5, 'Best purchase', '2025-09-06 16:15:00', 48),
(817, 4, 'Meets expectations', '2025-09-06 16:20:00', 49),
(818, 2, 'Below average', '2025-09-06 16:25:00', 50),
(819, 5, 'Highly satisfied', '2025-09-06 16:30:00', 51),
(820, 4, 'Good features', '2025-09-06 16:35:00', 52),
(821, 3, 'Adequate', '2025-09-06 16:40:00', 53),
(822, 5, 'Excellent value', '2025-09-06 16:45:00', 54),
(823, 4, 'Solid build', '2025-09-06 16:50:00', 69),
(824, 5, 'Impressive', '2025-09-06 16:55:00', 70),
(825, 3, 'Fair enough', '2025-09-06 17:00:00', 71),
(826, 4, 'Useful item', '2025-09-06 17:05:00', 72),
(827, 5, 'Top-notch', '2025-09-06 17:10:00', 73),
(828, 2, 'Not recommended', '2025-09-06 17:15:00', 1),
(829, 5, 'Outstanding quality', '2025-09-06 17:20:00', 2),
(830, 4, 'Worth it', '2025-09-06 17:25:00', 3),
(831, 3, 'Moderate', '2025-09-06 17:30:00', 4),
(832, 5, 'Fantastic buy', '2025-09-06 17:35:00', 5),
(833, 4, 'Dependable', '2025-09-06 17:40:00', 6),
(834, 5, 'Super', '2025-09-06 17:45:00', 7),
(835, 3, 'Acceptable quality', '2025-09-06 17:50:00', 8),
(836, 4, 'Fine product', '2025-09-06 17:55:00', 9),
(837, 5, 'Exceptional performance', '2025-09-06 18:00:00', 10),
(838, 5, 'Incredible quality', '2025-09-06 20:00:00', 74),
(839, 4, 'Very useful', '2025-09-06 20:05:00', 75),
(840, 3, 'Standard item', '2025-09-06 20:10:00', 76),
(841, 5, 'Highly effective', '2025-09-06 20:15:00', 77),
(842, 4, 'Meets needs', '2025-09-06 20:20:00', 78),
(843, 2, 'Below par', '2025-09-06 20:25:00', 79),
(844, 5, 'Top quality', '2025-09-06 20:30:00', 80),
(845, 4, 'Good design', '2025-09-06 20:35:00', 81),
(846, 3, 'Adequate performance', '2025-09-06 20:40:00', 82),
(847, 5, 'Excellent choice', '2025-09-06 20:45:00', 83),
(848, 4, 'Reliable product', '2025-09-06 20:50:00', 1),
(849, 5, 'Impressive features', '2025-09-06 20:55:00', 2),
(850, 3, 'Fair quality', '2025-09-06 21:00:00', 3),
(851, 4, 'Useful daily', '2025-09-06 21:05:00', 4),
(852, 5, 'Great value', '2025-09-06 21:10:00', 5),
(853, 2, 'Not durable', '2025-09-06 21:15:00', 6),
(854, 5, 'Outstanding performance', '2025-09-06 21:20:00', 7),
(855, 4, 'Solid choice', '2025-09-06 21:25:00', 8),
(856, 3, 'Moderate satisfaction', '2025-09-06 21:30:00', 9),
(857, 5, 'Fantastic item', '2025-09-06 21:35:00', 10),
(858, 4, 'Dependable quality', '2025-09-06 21:40:00', 35),
(859, 5, 'Super product', '2025-09-06 21:45:00', 36),
(860, 3, 'Acceptable level', '2025-09-06 21:50:00', 37),
(861, 4, 'Fine features', '2025-09-06 21:55:00', 38),
(862, 5, 'Exceptional value', '2025-09-06 22:00:00', 39),
(863, 1, 'Poor quality', '2025-09-06 22:05:00', 40),
(864, 5, 'Amazing buy', '2025-09-06 22:10:00', 41),
(865, 4, 'Good functionality', '2025-09-06 22:15:00', 42),
(866, 3, 'Average experience', '2025-09-06 22:20:00', 43),
(867, 5, 'Highly rated', '2025-09-06 22:25:00', 44),
(868, 4, 'Satisfactory product', '2025-09-06 22:30:00', 64),
(869, 5, 'Brilliant design', '2025-09-06 22:35:00', 65),
(870, 3, 'Passable item', '2025-09-06 22:40:00', 66),
(871, 4, 'Acceptable performance', '2025-09-06 22:45:00', 67),
(872, 5, 'Superb choice', '2025-09-06 22:50:00', 68),
(873, 2, 'Disappointing result', '2025-09-06 22:55:00', 69),
(874, 5, 'Outstanding item', '2025-09-06 23:00:00', 70),
(875, 4, 'Worth buying', '2025-09-06 23:05:00', 71),
(876, 3, 'Mediocre quality', '2025-09-06 23:10:00', 72),
(877, 5, 'Fantastic value', '2025-09-06 23:15:00', 73),
(878, 4, 'Reliable and durable', '2025-09-06 23:20:00', 74),
(879, 5, 'Impressive overall', '2025-09-06 23:25:00', 75),
(880, 3, 'Fair but limited', '2025-09-06 23:30:00', 76),
(881, 4, 'Nice addition', '2025-09-06 23:35:00', 77),
(882, 5, 'Great experience', '2025-09-06 23:40:00', 78),
(883, 1, 'Very poor', '2025-09-06 23:45:00', 79),
(884, 5, 'Top recommendation', '2025-09-06 23:50:00', 80),
(885, 4, 'Solid performance', '2025-09-06 23:55:00', 81),
(886, 3, 'Okay but average', '2025-09-07 00:00:00', 82),
(887, 5, 'Exceptional quality', '2025-09-07 00:05:00', 83),
(888, 4, 'Great accessory', '2025-09-06 13:00:00', 1),
(889, 5, 'Love the design', '2025-09-06 13:05:00', 2),
(890, 3, 'Decent performance', '2025-09-06 13:10:00', 5),
(891, 4, 'Good book', '2025-09-06 13:15:00', 3),
(892, 5, 'Excellent shoes', '2025-09-06 13:20:00', 4),
(893, 2, 'Not my style', '2025-09-06 13:25:00', 6),
(894, 5, 'Amazing tech', '2025-09-06 13:30:00', 7),
(895, 4, 'Useful appliance', '2025-09-06 13:35:00', 8),
(896, 3, 'Average quality', '2025-09-06 13:40:00', 9),
(897, 5, 'Fantastic food', '2025-09-06 13:45:00', 10),
(898, 4, 'Solid product', '2025-09-06 13:50:00', 1),
(899, 5, 'Highly recommend', '2025-09-06 13:55:00', 2),
(900, 3, 'Okay experience', '2025-09-06 14:00:00', 5),
(901, 4, 'Good value', '2025-09-06 14:05:00', 11),
(902, 5, 'Perfect fit', '2025-09-06 14:10:00', 12),
(903, 5, 'Amazing technology', '2025-09-06 18:30:00', 1),
(904, 4, 'Good read', '2025-09-06 18:35:00', 2),
(905, 3, 'Nice sport gear', '2025-09-06 18:40:00', 5),
(906, 5, 'Excellent appliance', '2025-09-06 18:45:00', 3),
(907, 4, 'Stylish fashion', '2025-09-06 18:50:00', 4),
(908, 2, 'Average taste', '2025-09-06 18:55:00', 6),
(909, 5, 'Great device', '2025-09-06 19:00:00', 7),
(910, 4, 'Useful book', '2025-09-06 19:05:00', 8),
(911, 3, 'Decent quality', '2025-09-06 19:10:00', 9),
(912, 5, 'Love the food', '2025-09-06 19:15:00', 10),
(913, 4, 'Solid product', '2025-09-06 19:20:00', 84),
(914, 5, 'Highly recommend', '2025-09-06 19:25:00', 85),
(915, 3, 'Okay performance', '2025-09-06 19:30:00', 86),
(916, 4, 'Good value', '2025-09-06 19:35:00', 87),
(917, 5, 'Perfect fit', '2025-09-06 19:40:00', 88),
(918, 4, 'Great tech', '2025-09-06 19:45:00', 89),
(919, 3, 'Average book', '2025-09-06 19:50:00', 90),
(920, 5, 'Excellent sport', '2025-09-06 19:55:00', 91),
(921, 4, 'Useful appliance', '2025-09-06 20:00:00', 92),
(922, 2, 'Not my style', '2025-09-06 20:05:00', 93),
(923, 5, 'Amazing design', '2025-09-06 20:10:00', 94),
(924, 4, 'Good food', '2025-09-06 20:15:00', 95),
(925, 3, 'Decent wear', '2025-09-06 20:20:00', 96),
(926, 5, 'Great electronics', '2025-09-06 20:25:00', 97),
(927, 4, 'Solid book', '2025-09-06 20:30:00', 98),
(928, 3, 'Average sport', '2025-09-06 20:35:00', 99),
(929, 5, 'Excellent appliance', '2025-09-06 20:40:00', 100),
(930, 4, 'Good fashion', '2025-09-06 20:45:00', 101),
(931, 2, 'Poor taste', '2025-09-06 20:50:00', 102),
(932, 5, 'Amazing tech', '2025-09-06 20:55:00', 103),
(933, 4, 'Nice read', '2025-09-06 21:00:00', 1),
(934, 5, 'Great sport gear', '2025-09-06 21:05:00', 2),
(935, 3, 'Okay appliance', '2025-09-06 21:10:00', 5),
(936, 4, 'Good book', '2025-09-06 21:15:00', 3),
(937, 5, 'Excellent fashion', '2025-09-06 21:20:00', 4),
(938, 2, 'Bad taste', '2025-09-06 21:25:00', 6),
(939, 5, 'Amazing device', '2025-09-06 21:30:00', 7),
(940, 4, 'Useful product', '2025-09-06 21:35:00', 8),
(941, 3, 'Average quality', '2025-09-06 21:40:00', 9),
(942, 5, 'Love the food', '2025-09-06 21:45:00', 10),
(943, 4, 'Solid tech', '2025-09-06 21:50:00', 84),
(944, 5, 'Highly recommend', '2025-09-06 21:55:00', 85),
(945, 3, 'Okay performance', '2025-09-06 22:00:00', 86),
(946, 4, 'Good value', '2025-09-06 22:05:00', 87),
(947, 5, 'Perfect fit', '2025-09-06 22:10:00', 88),
(948, 4, 'Great design', '2025-09-06 22:15:00', 89),
(949, 3, 'Average book', '2025-09-06 22:20:00', 90),
(950, 5, 'Excellent sport', '2025-09-06 22:25:00', 91),
(951, 4, 'Useful appliance', '2025-09-06 22:30:00', 92),
(952, 2, 'Not my taste', '2025-09-06 22:35:00', 93),
(953, 5, 'Amazing product', '2025-09-06 22:40:00', 94),
(954, 4, 'Good food', '2025-09-06 22:45:00', 95),
(955, 3, 'Decent wear', '2025-09-06 22:50:00', 96),
(956, 5, 'Great electronics', '2025-09-06 22:55:00', 97),
(957, 4, 'Solid book', '2025-09-06 23:00:00', 98),
(958, 3, 'Average sport', '2025-09-06 23:05:00', 99),
(959, 5, 'Excellent appliance', '2025-09-06 23:10:00', 100),
(960, 4, 'Good fashion', '2025-09-06 23:15:00', 101),
(961, 2, 'Poor quality', '2025-09-06 23:20:00', 102),
(962, 5, 'Amazing tech', '2025-09-06 23:25:00', 103),
(963, 4, 'Nice read', '2025-09-06 23:30:00', 1),
(964, 5, 'Great sport gear', '2025-09-06 23:35:00', 2),
(965, 3, 'Okay appliance', '2025-09-06 23:40:00', 5),
(966, 4, 'Good book', '2025-09-06 23:45:00', 3),
(967, 5, 'Excellent fashion', '2025-09-06 23:50:00', 4),
(968, 2, 'Bad taste', '2025-09-06 23:55:00', 6),
(969, 5, 'Amazing device', '2025-09-07 00:00:00', 7),
(970, 4, 'Useful product', '2025-09-07 00:05:00', 8),
(971, 3, 'Average quality', '2025-09-07 00:10:00', 9),
(972, 5, 'Love the food', '2025-09-07 00:15:00', 10),
(973, 4, 'Solid tech', '2025-09-07 00:20:00', 84),
(974, 5, 'Highly recommend', '2025-09-07 00:25:00', 85),
(975, 3, 'Okay performance', '2025-09-07 00:30:00', 86),
(976, 4, 'Good value', '2025-09-07 00:35:00', 87),
(977, 5, 'Perfect fit', '2025-09-07 00:40:00', 88),
(978, 4, 'Great design', '2025-09-07 00:45:00', 89),
(979, 3, 'Average book', '2025-09-07 00:50:00', 90),
(980, 5, 'Excellent sport', '2025-09-07 00:55:00', 91),
(981, 4, 'Useful appliance', '2025-09-07 01:00:00', 92),
(982, 2, 'Not my taste', '2025-09-07 01:05:00', 93),
(983, 5, 'Amazing product', '2025-09-07 01:10:00', 94),
(984, 4, 'Good food', '2025-09-07 01:15:00', 95),
(985, 3, 'Decent wear', '2025-09-07 01:20:00', 96),
(986, 5, 'Great electronics', '2025-09-07 01:25:00', 97),
(987, 4, 'Solid book', '2025-09-07 01:30:00', 98),
(988, 3, 'Average sport', '2025-09-07 01:35:00', 99),
(989, 5, 'Excellent appliance', '2025-09-07 01:40:00', 100),
(990, 4, 'Good fashion', '2025-09-07 01:45:00', 101),
(991, 2, 'Poor quality', '2025-09-07 01:50:00', 102),
(992, 5, 'Amazing tech', '2025-09-07 01:55:00', 103),
(993, 4, 'Nice read', '2025-09-07 02:00:00', 1),
(994, 5, 'Great sport gear', '2025-09-07 02:05:00', 2),
(995, 3, 'Okay appliance', '2025-09-07 02:10:00', 5),
(996, 4, 'Good book', '2025-09-07 02:15:00', 3),
(997, 5, 'Excellent fashion', '2025-09-07 02:20:00', 4),
(998, 2, 'Bad taste', '2025-09-07 02:25:00', 6),
(999, 5, 'Amazing device', '2025-09-07 02:30:00', 7),
(1000, 4, 'Useful product', '2025-09-07 02:35:00', 8),
(1001, 3, 'Average quality', '2025-09-07 02:40:00', 9),
(1002, 5, 'Love the food', '2025-09-07 02:45:00', 10);

INSERT INTO `rating_on_product` (`rate_id`, `product_id`) VALUES
(813, 11),
(823, 11),
(833, 11),
(814, 12),
(828, 12),
(834, 12),
(815, 13),
(824, 13),
(835, 13),
(816, 14),
(829, 14),
(836, 14),
(817, 15),
(825, 15),
(837, 15),
(818, 16),
(830, 16),
(819, 17),
(826, 17),
(820, 18),
(831, 18),
(821, 19),
(827, 19),
(822, 20),
(832, 20),
(838, 21),
(858, 21),
(873, 21),
(894, 21),
(933, 21),
(839, 22),
(863, 22),
(874, 22),
(891, 22),
(949, 22),
(840, 23),
(859, 23),
(875, 23),
(901, 23),
(934, 23),
(841, 24),
(864, 24),
(876, 24),
(895, 24),
(935, 24),
(842, 25),
(860, 25),
(877, 25),
(888, 25),
(937, 25),
(843, 26),
(865, 26),
(878, 26),
(897, 26),
(938, 26),
(844, 27),
(861, 27),
(879, 27),
(889, 27),
(939, 27),
(845, 28),
(866, 28),
(880, 28),
(896, 28),
(936, 28),
(846, 29),
(862, 29),
(881, 29),
(847, 30),
(867, 30),
(882, 30),
(890, 30),
(940, 30),
(848, 31),
(868, 31),
(883, 31),
(893, 31),
(849, 32),
(884, 32),
(941, 32),
(850, 33),
(869, 33),
(885, 33),
(898, 33),
(943, 33),
(851, 34),
(886, 34),
(852, 35),
(870, 35),
(887, 35),
(892, 35),
(944, 35),
(853, 36),
(900, 36),
(945, 36),
(854, 37),
(871, 37),
(902, 37),
(946, 37),
(855, 38),
(942, 38),
(856, 39),
(872, 39),
(899, 39),
(947, 39),
(857, 40),
(948, 40),
(903, 41),
(978, 41),
(904, 42),
(979, 42),
(905, 43),
(950, 43),
(980, 43),
(906, 44),
(951, 44),
(981, 44),
(907, 45),
(952, 45),
(982, 45),
(908, 46),
(953, 46),
(983, 46),
(909, 47),
(954, 47),
(984, 47),
(910, 48),
(955, 48),
(985, 48),
(911, 49),
(956, 49),
(986, 49),
(912, 50),
(957, 50),
(987, 50),
(913, 51),
(958, 51),
(988, 51),
(914, 52),
(959, 52),
(989, 52),
(915, 53),
(960, 53),
(990, 53),
(916, 54),
(961, 54),
(991, 54),
(917, 55),
(962, 55),
(992, 55),
(918, 56),
(963, 56),
(993, 56),
(919, 57),
(964, 57),
(994, 57),
(920, 58),
(965, 58),
(995, 58),
(921, 59),
(966, 59),
(996, 59),
(922, 60),
(967, 60),
(997, 60),
(923, 61),
(968, 61),
(998, 61),
(924, 62),
(969, 62),
(999, 62),
(925, 63),
(970, 63),
(1000, 63),
(926, 64),
(971, 64),
(1001, 64),
(927, 65),
(972, 65),
(1002, 65),
(928, 66),
(973, 66),
(929, 67),
(974, 67),
(930, 68),
(975, 68),
(931, 69),
(976, 69),
(932, 70),
(977, 70);




-- Populate Account table (users and stores)
INSERT INTO account (user_name, email, password, status, phone_number, image, datetime_of_insert) VALUES
('john_doe', 'john@example.com', '$2a$10$X5jMYN.NvO80KojjJPHinuPlqyTO.my2zI1Qs3cSGFXc5N4tsOEJu', 'active', '555-1234', 'john.jpg', NOW()),
('jane_smith', 'jane@example.com', '$2a$10$X5jMYN.NvO80KojjJPHinuPlqyTO.my2zI1Qs3cSGFXc5N4tsOEJu', 'active', '555-5678', 'jane.jpg', NOW()),
('tech_store', 'tech@store.com', '$2a$10$X5jMYN.NvO80KojjJPHinuPlqyTO.my2zI1Qs3cSGFXc5N4tsOEJu', 'verified', '555-8765', 'techstore.jpg', NOW()),
('farhan', 'fashion@shop.com', '$2a$10$X5jMYN.NvO80KojjJPHinuPlqyTO.my2zI1Qs3cSGFXc5N4tsOEJu', 'pending', '555-4321', 'fashion.jpg', NOW()) ;



-- Populate User_account
INSERT INTO user_account (account_id, fname, lname, score_of_activity, score_of_integrity) VALUES
-- (1, 'John', 'Doe', 85, 90),
-- (2, 'Jane', 'Smith', 92, 88),
(104, 'Alice', 'Brown', 70, 75),
(105, 'Bob', 'Green', 80, 82),
(106, 'Charlie', 'White', 65, 70),
(107, 'Fashion Plus','White', 65,66) ;


-- Populate Store_account
INSERT INTO store_account (account_id, name, verified_flag) VALUES
-- (3, 'Tech Haven', 1),
-- (4, 'Fashion Plus', 0),
(10, 'user10', 0),
(11, 'user11', 0),
(12, 'Tech Haven 12 ', 1),
(5, 'user5', 0),
(8, 'user8', 0),
(9, 'user9', 0);

update account set store_account_flag = 1 where account_id in (10,11,12,5,8,9);

update account set roles = 'ROLE_USER' where account_id >=0 ;



-- Populate Price
INSERT INTO price (price, datetime_of_insert, currency, unit_of_measure, store_account_id, Product_id) VALUES
(1499.99, NOW(), 'USD', 'Each', 3, 1),
(49.99, NOW(), 'USD', 'Each', 3, 2),
(89.99, NOW(), 'USD', 'Each', 4, 3);

-- Populate Store_price
INSERT INTO store_price (price_id, quantity) VALUES
(1, 15),
(2, 50),
(3, 30);

-- Populate Shopping_cart
INSERT INTO shopping_cart (name, datetime_of_insert, description, image, public_private_flag, user_account_id) VALUES
('My Tech Cart', NOW(), 'Tech items', 'cart1.jpg', 1, 1),
('Clothing Picks', NOW(), 'Fashion items', 'cart2.jpg', 0, 2);

-- Populate Prod_of_cart
INSERT INTO prod_of_cart (Product_id, Shopping_Cart_id, quantity) VALUES
(1, 1, 2),
(3, 2, 1);

-- Populate User_price
INSERT INTO user_price (price_id, up_vote_count, down_vote_count, user_account_id) VALUES
(1, 15, 2, 104),
(3, 8, 1, 105);



-- Populate Social_media
INSERT INTO social_media (social_media_account, store_account_id, account_type, datetime_of_insert) VALUES
('@techhaven', 3, 'Instagram', NOW()),
('@fashionplus', 4, 'Facebook', NOW());

-- Populate Address
INSERT INTO address (address, store_account_id, datetime_of_insert) VALUES
('123 Tech Street', 3, NOW()),
('456 Fashion Ave', 4, NOW());

-- Populate Search_history
INSERT INTO search_history (search_data, datetime_of_insert, account_id) VALUES
('gaming laptops', NOW(), 1),
('designer jeans', NOW(), 2);

-- Populate Report
INSERT INTO report (datetime_of_insert, description, account_id) VALUES
(NOW(), 'Inappropriate content', 1),
(NOW(), 'Fake product', 2);

-- Populate Report_on_product
INSERT INTO report_on_product (report_id, Product_id) VALUES
(2, 3);

update account set roles = "ROLE_MODERATOR" where account_id = 106;

INSERT INTO rating (stars_rate, feedback, datetime_of_insert, account_id) VALUES
(4, 'Friendly staff and good prices!', NOW(), 11),
(5, 'A lot of items and easy to find everything!', NOW(), 12),
(3, 'Average experience, could be better.', NOW(), 13);

INSERT INTO rating_on_store (rate_id, store_account_id) VALUES
(1002, 5),
(1003, 5),
(1004, 5),
(1005, 3);




-- last insert

INSERT INTO rating (rate_id, stars_rate, feedback, datetime_of_insert, account_id) VALUES
(1, 4.5, 'Great product!', '2025-09-05 12:00:00', 1),
(2, 3.8, 'Good, but could be better.', '2025-09-05 12:01:00', 1),
(3, 5.0, 'Perfect!', '2025-09-05 12:02:00', 2),
(4, 4.0, 'Very useful.', '2025-09-05 12:03:00', 3),
(5, 4.5, 'Excellent quality.', '2025-09-05 12:04:00', 3),
(6, 4.2, 'Satisfied.', '2025-09-05 12:05:00', 8),
(7, 3.5, 'Average.', '2025-09-05 12:06:00', 9),
(8, 4.0, 'Good value for money.', '2025-09-05 12:07:00', 9),
(9, 5.0, 'Amazing!', '2025-09-05 12:08:00', 1),
(10, 4.8, 'Highly recommended.', '2025-09-05 12:09:00', 2),
(11, 4.3, 'Works well.', '2025-09-05 12:10:00', 4),
(12, 3.9, 'Decent product.', '2025-09-05 12:11:00', 5),
(13, 4.7, 'Impressive.', '2025-09-05 12:12:00', 6),
(14, 4.1, 'Satisfactory.', '2025-09-05 12:13:00', 7),
(15, 4.6, 'Excellent!', '2025-09-05 12:14:00', 10),
(16, 3.7, 'Okay.', '2025-09-05 12:15:00', 11),
(17, 4.4, 'Very good.', '2025-09-05 12:16:00', 12),
(18, 4.9, 'Outstanding.', '2025-09-05 12:17:00', 13),
(19, 3.6, 'Fair.', '2025-09-05 12:18:00', 14),
(20, 4.5, 'Great!', '2025-09-05 12:19:00', 15),
(21, 4.0, 'Good.', '2025-09-05 12:20:00', 16),
(22, 4.2, 'Nice product.', '2025-09-05 12:21:00', 17),
(23, 3.5, 'Average quality.', '2025-09-05 12:22:00', 18),
(24, 4.8, 'Highly recommend.', '2025-09-05 12:23:00', 19),
(25, 4.1, 'Satisfied.', '2025-09-05 12:24:00', 20),
(26, 4.7, 'Excellent value.', '2025-09-05 12:25:00', 21),
(27, 3.9, 'Decent.', '2025-09-05 12:26:00', 22),
(28, 4.6, 'Very good quality.', '2025-09-05 12:27:00', 23),
(29, 4.3, 'Works as expected.', '2025-09-05 12:28:00', 24),
(30, 3.8, 'Good but not great.', '2025-09-05 12:29:00', 25),
(31, 4.4, 'Recommended.', '2025-09-05 12:30:00', 26),
(32, 4.9, 'Fantastic!', '2025-09-05 12:31:00', 27),
(33, 3.7, 'Okay product.', '2025-09-05 12:32:00', 28),
(34, 4.5, 'Great purchase.', '2025-09-05 12:33:00', 29),
(35, 4.0, 'Satisfactory.', '2025-09-05 12:34:00', 30),
(36, 4.2, 'Very nice.', '2025-09-05 12:35:00', 31),
(37, 3.5, 'Average.', '2025-09-05 12:36:00', 32),
(38, 4.8, 'Excellent!', '2025-09-05 12:37:00', 33),
(39, 4.1, 'Good value.', '2025-09-05 12:38:00', 34),
(40, 4.7, 'Highly recommended.', '2025-09-05 12:39:00', 35),
(41, 3.9, 'Decent product.', '2025-09-05 12:40:00', 36),
(42, 4.6, 'Very good.', '2025-09-05 12:41:00', 37),
(43, 4.3, 'Works well.', '2025-09-05 12:42:00', 38),
(44, 3.8, 'Good but could be better.', '2025-09-05 12:43:00', 39),
(45, 4.4, 'Recommended.', '2025-09-05 12:44:00', 40),
(46, 4.9, 'Outstanding.', '2025-09-05 12:45:00', 41),
(47, 3.6, 'Fair.', '2025-09-05 12:46:00', 42),
(48, 4.5, 'Great!', '2025-09-05 12:47:00', 43),
(49, 4.0, 'Good.', '2025-09-05 12:48:00', 44),
(50, 4.2, 'Nice product.', '2025-09-05 12:49:00', 45),
(51, 3.5, 'Average quality.', '2025-09-05 12:50:00', 46),
(52, 4.8, 'Highly recommend.', '2025-09-05 12:51:00', 47),
(53, 4.1, 'Satisfied.', '2025-09-05 12:52:00', 48),
(54, 4.7, 'Excellent value.', '2025-09-05 12:53:00', 49),
(55, 3.9, 'Decent.', '2025-09-05 12:54:00', 50),
(56, 4.6, 'Very good quality.', '2025-09-05 12:55:00', 51),
(57, 4.3, 'Works as expected.', '2025-09-05 12:56:00', 52),
(58, 3.8, 'Good but not great.', '2025-09-05 12:57:00', 53),
(59, 4.4, 'Recommended.', '2025-09-05 12:58:00', 54),
(60, 4.9, 'Fantastic!', '2025-09-05 12:59:00', 55),
(61, 3.7, 'Okay product.', '2025-09-05 13:00:00', 56),
(62, 4.5, 'Great purchase.', '2025-09-05 13:01:00', 57),
(63, 4.0, 'Satisfactory.', '2025-09-05 13:02:00', 58),
(64, 4.2, 'Very nice.', '2025-09-05 13:03:00', 59),
(65, 3.5, 'Average.', '2025-09-05 13:04:00', 60),
(66, 4.8, 'Excellent!', '2025-09-05 13:05:00', 61),
(67, 4.1, 'Good value.', '2025-09-05 13:06:00', 62),
(68, 4.7, 'Highly recommended.', '2025-09-05 13:07:00', 63),
(69, 3.9, 'Decent product.', '2025-09-05 13:08:00', 64),
(70, 4.6, 'Very good.', '2025-09-05 13:09:00', 65),
(71, 4.3, 'Works well.', '2025-09-05 13:10:00', 66),
(72, 3.8, 'Good but could be better.', '2025-09-05 13:11:00', 67),
(73, 4.4, 'Recommended.', '2025-09-05 13:12:00', 68),


(74, 4.9, 'Outstanding.', '2025-09-05 13:13:00', 69),
(75, 3.6, 'Fair.', '2025-09-05 13:14:00', 70),
(76, 4.5, 'Great!', '2025-09-05 13:15:00', 71),
(77, 4.0, 'Good.', '2025-09-05 13:16:00', 72);


INSERT INTO rating_on_product (rate_id, product_id) VALUES
(1, 1),
(2, 43),
(3, 27),
(4, 23),
(5, 16),
(6, 44),
(7, 33),
(8, 55),
(9, 70),
(10, 59),
(11, 40),
(12, 47),
(13, 62),
(14, 37),
(15, 60),
(16, 45),
(17, 19),
(18, 27),
(19, 34),
(20, 28),
(21, 1),
(22, 43),
(23, 27),
(24, 23),
(25, 16),
(26, 44),
(27, 33),
(28, 55),
(29, 70),
(30, 59),
(31, 40),
(32, 47),
(33, 62),
(34, 37),
(35, 60),
(36, 45),
(37, 19),
(38, 27),
(39, 34),
(40, 28),
(41, 1),
(42, 43),
(43, 27),
(44, 23),
(45, 16),
(46, 44),
(47, 33),
(48, 55),
(49, 70),
(50, 59),
(51, 40),
(52, 47),
(53, 62),
(54, 37),
(55, 60),
(56, 45),
(57, 19),
(58, 27),
(59, 34),
(60, 28),
(61, 1),
(62, 43),
(63, 27),
(64, 23),
(65, 16),
(66, 44),
(67, 33),
(68, 55),
(69, 70),
(70, 59),
(71, 40),
(72, 47),
(73, 62),
(74, 37),
(75, 60),
(76, 45),
(77, 19);


INSERT INTO rating (rate_id, stars_rate, feedback, datetime_of_insert, account_id) VALUES
(78, 4.2, 'Really good product!', '2025-09-05 13:17:00', 1),
(79, 3.9, 'Works fine, but not perfect.', '2025-09-05 13:18:00', 2),
(80, 4.8, 'Amazing quality!', '2025-09-05 13:19:00', 3),
(81, 4.0, 'Good value for price.', '2025-09-05 13:20:00', 4),
(82, 4.5, 'Highly recommend!', '2025-09-05 13:21:00', 5),
(83, 3.7, 'Decent, could improve.', '2025-09-05 13:22:00', 6),
(84, 4.3, 'Satisfied with purchase.', '2025-09-05 13:23:00', 7),
(85, 4.9, 'Outstanding product!', '2025-09-05 13:24:00', 8),
(86, 4.1, 'Very useful item.', '2025-09-05 13:25:00', 9),
(87, 4.6, 'Great performance!', '2025-09-05 13:26:00', 10),
(88, 3.8, 'Okay, but expected better.', '2025-09-05 13:27:00', 11),
(89, 4.4, 'Very nice product.', '2025-09-05 13:28:00', 12),
(90, 4.7, 'Excellent choice!', '2025-09-05 13:29:00', 13),
(91, 3.6, 'Fair quality.', '2025-09-05 13:30:00', 14),
(92, 4.5, 'Really worth it!', '2025-09-05 13:31:00', 15),
(93, 4.0, 'Good product.', '2025-09-05 13:32:00', 16),
(94, 4.2, 'Happy with this.', '2025-09-05 13:33:00', 17),
(95, 3.5, 'Average performance.', '2025-09-05 13:34:00', 18),
(96, 4.8, 'Fantastic buy!', '2025-09-05 13:35:00', 19),
(97, 4.3, 'Works as expected.', '2025-09-05 13:36:00', 20);


INSERT INTO rating_on_product (rate_id, product_id) VALUES
(78, 9),
(79, 18),
(80, 21),
(81, 42),
(82, 57),
(83, 9),
(84, 18),
(85, 21),
(86, 42),
(87, 57),
(88, 1),
(89, 16),
(90, 23),
(91, 27),
(92, 28),
(93, 33),
(94, 34),
(95, 37),
(96, 40),
(97, 44);



-- last eddit
INSERT INTO `price` (`price`, `datetime_of_insert`, `currency`, `unit_of_measure`, `store_account_id`, `product_id`, `is_store_price`) VALUES
(999.99, '2025-09-07 10:00:00', 'USD', 'unit', 5, 1, FALSE),
(950.00, '2025-09-07 10:05:00', 'USD', 'unit', 5, 1, FALSE),
(799.99, '2025-09-07 10:10:00', 'USD', 'unit', 8, 2, FALSE),
(780.00, '2025-09-07 10:15:00', 'USD', 'unit', 8, 2, FALSE),
(39.99, '2025-09-07 10:20:00', 'USD', 'unit', 9, 3, FALSE),
(35.00, '2025-09-07 10:25:00', 'USD', 'unit', 9, 3, FALSE),
(120.00, '2025-09-07 10:30:00', 'USD', 'unit', 10, 4, FALSE),
(115.00, '2025-09-07 10:35:00', 'USD', 'unit', 10, 4, FALSE),
(70.00, '2025-09-07 10:40:00', 'USD', 'pair', 11, 5, FALSE),
(65.00, '2025-09-07 10:45:00', 'USD', 'pair', 11, 5, FALSE),
(150.00, '2025-09-07 10:50:00', 'USD', 'unit', 12, 6, FALSE),
(140.00, '2025-09-07 10:55:00', 'USD', 'unit', 12, 6, FALSE),
(99.00, '2025-09-07 11:00:00', 'USD', 'unit', 5, 7, FALSE),
(95.00, '2025-09-07 11:05:00', 'USD', 'unit', 5, 7, FALSE),
(15.00, '2025-09-07 11:10:00', 'USD', 'unit', 8, 8, FALSE),
(12.00, '2025-09-07 11:15:00', 'USD', 'unit', 8, 8, FALSE),
(89.99, '2025-09-07 11:20:00', 'USD', 'unit', 9, 9, FALSE),
(85.00, '2025-09-07 11:25:00', 'USD', 'unit', 9, 9, FALSE),
(25.00, '2025-09-07 11:30:00', 'USD', 'unit', 10, 10, FALSE),
(22.00, '2025-09-07 11:35:00', 'USD', 'unit', 10, 10, FALSE),
(299.00, '2025-09-07 11:40:00', 'USD', 'unit', 11, 1, FALSE),
(285.00, '2025-09-07 11:45:00', 'USD', 'unit', 11, 1, FALSE),
(550.00, '2025-09-07 11:50:00', 'USD', 'unit', 12, 2, FALSE),
(540.00, '2025-09-07 11:55:00', 'USD', 'unit', 12, 2, FALSE),
(25.00, '2025-09-07 12:00:00', 'USD', 'unit', 5, 3, FALSE),
(20.00, '2025-09-07 12:05:00', 'USD', 'unit', 5, 3, FALSE),
(200.00, '2025-09-07 12:10:00', 'USD', 'unit', 8, 4, FALSE),
(190.00, '2025-09-07 12:15:00', 'USD', 'unit', 8, 4, FALSE),
(45.00, '2025-09-07 12:20:00', 'USD', 'unit', 9, 5, FALSE),
(40.00, '2025-09-07 12:25:00', 'USD', 'unit', 9, 5, FALSE),
(180.00, '2025-09-07 12:30:00', 'USD', 'unit', 10, 6, FALSE),
(170.00, '2025-09-07 12:35:00', 'USD', 'unit', 10, 6, FALSE),
(59.99, '2025-09-07 12:40:00', 'USD', 'unit', 11, 7, FALSE),
(55.00, '2025-09-07 12:45:00', 'USD', 'unit', 11, 7, FALSE),
(19.99, '2025-09-07 12:50:00', 'USD', 'unit', 12, 8, FALSE),
(18.00, '2025-09-07 12:55:00', 'USD', 'unit', 12, 8, FALSE),
(120.00, '2025-09-07 13:00:00', 'USD', 'unit', 5, 9, FALSE),
(115.00, '2025-09-07 13:05:00', 'USD', 'unit', 5, 9, FALSE),
(75.00, '2025-09-07 13:10:00', 'USD', 'unit', 8, 10, FALSE),
(70.00, '2025-09-07 13:15:00', 'USD', 'unit', 8, 10, FALSE);

INSERT INTO `user_price` (`price_id`, `up_vote_count`, `down_vote_count`, `user_account_id`) VALUES
(4, 25, 5, 78),
(5, 12, 1, 89),
(6, 45, 10, 65),
(7, 3, 22, 90),
(8, 33, 8, 71),
(9, 18, 2, 85),
(10, 50, 15, 69),
(11, 29, 3, 80),
(12, 10, 30, 75),
(13, 36, 7, 88),
(14, 2, 40, 67),
(15, 41, 11, 82),
(16, 20, 4, 73),
(17, 7, 18, 86),
(18, 48, 9, 70),
(19, 14, 25, 79),
(20, 31, 6, 83),
(21, 5, 35, 66),
(22, 44, 13, 81),
(23, 16, 2, 74),
(24, 27, 19, 87),
(25, 8, 42, 68),
(26, 39, 14, 76),
(27, 11, 28, 84),
(28, 47, 5, 72),
(29, 23, 1, 89),
(30, 32, 21, 65),
(31, 1, 46, 90),
(32, 40, 10, 71),
(33, 19, 2, 85),
(34, 5, 33, 69),
(35, 26, 4, 80),
(36, 15, 20, 75),
(37, 38, 9, 88),
(38, 9, 45, 67),
(39, 43, 12, 82),
(40, 21, 3, 73),
(41, 6, 29, 86),
(42, 49, 16, 70),
(43, 13, 24, 79),
(44, 30, 7, 83),
(45, 17, 39, 66),
(46, 34, 1, 81),
(47, 24, 26, 74);



INSERT INTO `price` (`price`, `datetime_of_insert`, `currency`, `unit_of_measure`, `store_account_id`, `product_id`, `is_store_price`) VALUES
(999.99, '2025-09-07 10:00:00', 'USD', 'unit', 5, 11, TRUE),
(950.00, '2025-09-07 10:05:00', 'USD', 'unit', 5, 11, TRUE),
(799.99, '2025-09-07 10:10:00', 'USD', 'unit', 8, 11, TRUE),
(780.00, '2025-09-07 10:15:00', 'USD', 'unit', 8, 11, TRUE);

INSERT INTO `store_price` (`price_id`, `quantity`) VALUES
(51, 2),
(52, 3),
(53, 1),
(54, 5);

SET FOREIGN_KEY_CHECKS = 1;