CREATE TABLE IF NOT EXISTS carts (
                       id BIGSERIAL PRIMARY KEY,
                       user_id BIGINT UNIQUE REFERENCES users(id) ON DELETE CASCADE
);