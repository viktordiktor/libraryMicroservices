CREATE TABLE IF NOT EXISTS notes (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       book_id BIGINT,
                       borrowed_date DATETIME,
                       return_date DATETIME,
                       FOREIGN KEY (book_id) REFERENCES books(id)
);