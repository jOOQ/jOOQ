DROP TABLE IF EXISTS book_to_book_store;
DROP TABLE IF EXISTS book_store;
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

CREATE TABLE book_store (
  name VARCHAR(400) NOT NULL,

  CONSTRAINT uk_t_book_store_name PRIMARY KEY(name)
);

CREATE TABLE book_to_book_store (
  book_store_name VARCHAR(400) NOT NULL,
  book_id INTEGER NOT NULL,
  stock INTEGER,

  CONSTRAINT pk_b2bs PRIMARY KEY(book_store_name, book_id),
  CONSTRAINT fk_b2bs_bs_name FOREIGN KEY (book_store_name)
                             REFERENCES book_store (name)
                             ON DELETE CASCADE,
  CONSTRAINT fk_b2bs_b_id    FOREIGN KEY (book_id)
                             REFERENCES book (id)
                             ON DELETE CASCADE
);

INSERT INTO author VALUES (DEFAULT, 'George', 'Orwell', '1903-06-25');
INSERT INTO author VALUES (DEFAULT, 'Paulo', 'Coelho', '1947-08-24');

INSERT INTO book VALUES (DEFAULT, 1, '1984', 1948, 1);
INSERT INTO book VALUES (DEFAULT, 1, 'Animal Farm', 1945, 1);
INSERT INTO book VALUES (DEFAULT, 2, 'O Alquimista', 1988, 4);
INSERT INTO book VALUES (DEFAULT, 2, 'Brida', 1990, 2);

INSERT INTO book_store (name) VALUES
	('Orell Füssli'),
	('Ex Libris'),
	('Buchhandlung im Volkshaus');

INSERT INTO book_to_book_store VALUES
	('Orell Füssli', 1, 10),
	('Orell Füssli', 2, 10),
	('Orell Füssli', 3, 10),
	('Ex Libris', 1, 1),
	('Ex Libris', 3, 2),
	('Buchhandlung im Volkshaus', 3, 1);
