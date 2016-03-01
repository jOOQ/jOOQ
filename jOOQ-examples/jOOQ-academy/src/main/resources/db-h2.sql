DROP TABLE IF EXISTS book_to_book_store
;
DROP TABLE IF EXISTS book_store
;
DROP TABLE IF EXISTS book
;
DROP TABLE IF EXISTS author
;

DROP SEQUENCE IF EXISTS s_author_id
;
CREATE SEQUENCE s_author_id START WITH 1
;

CREATE TABLE author (
  id INT NOT NULL,
  first_name VARCHAR(50),
  last_name VARCHAR(50) NOT NULL,
  date_of_birth DATE,

  CONSTRAINT pk_t_author PRIMARY KEY (ID)
)
;

CREATE TABLE book (
  id INT NOT NULL,
  author_id INT NOT NULL,
  title VARCHAR(400) NOT NULL,
  published_in INT,

  rec_timestamp TIMESTAMP,

  CONSTRAINT pk_t_book PRIMARY KEY (id),
  CONSTRAINT fk_t_book_author_id FOREIGN KEY (author_id) REFERENCES author(id),
)
;

CREATE TABLE book_store (
  name VARCHAR(400) NOT NULL,

  CONSTRAINT uk_t_book_store_name PRIMARY KEY(name)
)
;

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
)
;

INSERT INTO author VALUES (next value for s_author_id, 'George', 'Orwell', '1903-06-25')
;
INSERT INTO author VALUES (next value for s_author_id, 'Paulo', 'Coelho', '1947-08-24')
;

INSERT INTO book VALUES (1, 1, '1984'        , 1948, null)
;
INSERT INTO book VALUES (2, 1, 'Animal Farm' , 1945, null)
;
INSERT INTO book VALUES (3, 2, 'O Alquimista', 1988, null)
;
INSERT INTO book VALUES (4, 2, 'Brida'       , 1990, null)
;

INSERT INTO book_store (name) VALUES
	('Amazon'),
	('Barnes and Noble'),
	('Payot')
;

INSERT INTO book_to_book_store VALUES
	('Amazon', 1, 10),
	('Amazon', 2, 10),
	('Amazon', 3, 10),
	('Barnes and Noble', 1, 1),
	('Barnes and Noble', 3, 2),
	('Payot', 3, 1)
;

DROP ALIAS IF EXISTS count_books
;

CREATE OR REPLACE ALIAS count_books AS $$
int countBooks(Connection c, int authorID) throws SQLException {
    try (PreparedStatement s = c.prepareStatement("SELECT COUNT(*) FROM book WHERE author_id = ?")) {
        s.setInt(1, authorID);

        try (ResultSet rs = s.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        }
    }
}
$$
;