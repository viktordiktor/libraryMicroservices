CREATE TABLE IF NOT EXISTS  books (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       isbn VARCHAR(255) UNIQUE,
                       title VARCHAR(255),
                       genre VARCHAR(255),
                       description VARCHAR(1000),
                       author VARCHAR(255)
);