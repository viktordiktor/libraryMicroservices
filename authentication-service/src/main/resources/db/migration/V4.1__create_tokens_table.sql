CREATE TABLE IF NOT EXISTS tokens (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT,
                        access_token VARCHAR(255) UNIQUE NOT NULL,
                        refresh_token VARCHAR(255) UNIQUE NOT NULL,
                        FOREIGN KEY (user_id) references users(id)
);