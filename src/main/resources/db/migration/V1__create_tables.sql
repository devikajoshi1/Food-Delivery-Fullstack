CREATE TABLE users (
                       id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name          VARCHAR(100) NOT NULL,
                       email         VARCHAR(255) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role          VARCHAR(20)  NOT NULL,
                       created_at    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6)
);

CREATE TABLE restaurants (
                             id      BIGINT AUTO_INCREMENT PRIMARY KEY,
                             name    VARCHAR(150) NOT NULL,
                             cuisine VARCHAR(100),
                             address VARCHAR(255),
                             active  BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE menu_items (
                            id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                            restaurant_id BIGINT NOT NULL,
                            name          VARCHAR(150)  NOT NULL,
                            description   VARCHAR(500),
                            price         DECIMAL(10,2) NOT NULL,
                            available     BOOLEAN NOT NULL DEFAULT TRUE,
                            CONSTRAINT fk_menu_items_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id)
);

CREATE TABLE orders (
                        id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id             BIGINT NOT NULL,
                        restaurant_id       BIGINT NOT NULL,
                        status              VARCHAR(30)   NOT NULL,
                        total_amount        DECIMAL(10,2) NOT NULL,
                        delivery_address    VARCHAR(255)  NOT NULL,
                        razorpay_order_id   VARCHAR(64),
                        razorpay_payment_id VARCHAR(64),
                        created_at          DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
                        CONSTRAINT fk_orders_user       FOREIGN KEY (user_id)       REFERENCES users (id),
                        CONSTRAINT fk_orders_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurants (id)
);

CREATE TABLE order_items (
                             id           BIGINT AUTO_INCREMENT PRIMARY KEY,
                             order_id     BIGINT NOT NULL,
                             menu_item_id BIGINT NOT NULL,
                             quantity     INT NOT NULL CHECK (quantity > 0),
                             unit_price   DECIMAL(10,2) NOT NULL,
                             CONSTRAINT fk_order_items_order     FOREIGN KEY (order_id)     REFERENCES orders (id),
                             CONSTRAINT fk_order_items_menu_item FOREIGN KEY (menu_item_id) REFERENCES menu_items (id)
);
