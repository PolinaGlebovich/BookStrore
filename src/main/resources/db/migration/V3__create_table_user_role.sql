CREATE TABLE IF NOT EXISTS user_role (
                           user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                           role VARCHAR(255) NOT NULL,
                           PRIMARY KEY (user_id, role)
);