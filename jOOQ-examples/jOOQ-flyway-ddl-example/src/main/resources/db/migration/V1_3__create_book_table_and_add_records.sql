CREATE TABLE flyway_test.book (
  id INT NOT NULL,
  author_id INT NOT NULL,
  title VARCHAR(400) NOT NULL,

  CONSTRAINT pk_t_book PRIMARY KEY (id),
  CONSTRAINT fk_t_book_author_id FOREIGN KEY (author_id) REFERENCES flyway_test.author(id)
);
