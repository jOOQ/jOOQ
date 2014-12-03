DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS author;

CREATE TABLE author (
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(50),
  last_name VARCHAR(50) NOT NULL,
  date_of_birth DATE,

  CONSTRAINT pk_t_author PRIMARY KEY (ID)
);

CREATE TABLE book (
  id INT NOT NULL AUTO_INCREMENT,
  author_id INT NOT NULL,
  title VARCHAR(400) NOT NULL,
  published_in INT,
  language_id INT,

  CONSTRAINT pk_t_book PRIMARY KEY (id),
  CONSTRAINT fk_t_book_author_id FOREIGN KEY (author_id) REFERENCES author(id) ON DELETE CASCADE,
);

INSERT INTO author VALUES (DEFAULT, 'George', 'Orwell', '1903-06-25');
INSERT INTO author VALUES (DEFAULT, 'Paulo', 'Coelho', '1947-08-24');

INSERT INTO book VALUES (DEFAULT, 1, '1984', 1948, 1);
INSERT INTO book VALUES (DEFAULT, 1, 'Animal Farm', 1945, 1);
INSERT INTO book VALUES (DEFAULT, 2, 'O Alquimista', 1988, 4);
INSERT INTO book VALUES (DEFAULT, 2, 'Brida', 1990, 2);