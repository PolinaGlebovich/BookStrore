CREATE TABLE IF NOT EXISTS cart_item (
                           id BIGSERIAL PRIMARY KEY,
                           cart_id BIGINT REFERENCES carts(id) ON DELETE CASCADE,
                           book_id BIGINT REFERENCES books(id) ON DELETE CASCADE
);