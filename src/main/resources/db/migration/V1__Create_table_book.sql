CREATE TABLE book (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) NOT NULL,
    publication_year INT NOT NULL,
    description VARCHAR(1000)  NOT NULL
);
