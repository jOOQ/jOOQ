DROP TABLE IF EXISTS book_to_book_store;
DROP TABLE IF EXISTS book_store;
DROP TABLE IF EXISTS book;
DROP TABLE IF EXISTS author;

DROP SEQUENCE IF EXISTS s_author_id;
CREATE SEQUENCE s_author_id START WITH 1;

CREATE TABLE author (
  id INT NOT NULL,
  first_name VARCHAR(50),
  last_name VARCHAR(50) NOT NULL,
  date_of_birth DATE,
  year_of_birth INT,
  address VARCHAR(50),

  CONSTRAINT pk_t_author PRIMARY KEY (ID)
);

CREATE TABLE book (
  id INT NOT NULL,
  author_id INT NOT NULL,
  co_author_id INT,
  details_id INT,
  title VARCHAR(400) NOT NULL,
  published_in INT,
  language_id INT,
  content_text CLOB,
  content_pdf BLOB,

  rec_version INT,
  rec_timestamp TIMESTAMP,

  CONSTRAINT pk_t_book PRIMARY KEY (id),
  CONSTRAINT fk_t_book_author_id FOREIGN KEY (author_id) REFERENCES author(id),
  CONSTRAINT fk_t_book_co_author_id FOREIGN KEY (co_author_id) REFERENCES author(id)
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