CREATE TABLE IF NOT EXISTS books (
                       id BIGSERIAL PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       description TEXT,
                       genre VARCHAR(255),
                       author VARCHAR(255),
                       price DOUBLE PRECISION NOT NULL,
                       image_id VARCHAR(255)
);