DROP VIEW IF EXISTS v_library/
DROP VIEW IF EXISTS v_author/
DROP VIEW IF EXISTS v_book/
DROP TABLE IF EXISTS t_book_to_book_store/
DROP TABLE IF EXISTS t_book_store/
DROP TABLE IF EXISTS t_book/
DROP TABLE IF EXISTS t_book_details/
DROP TABLE IF EXISTS t_author/
DROP TABLE IF EXISTS t_language/
DROP TABLE IF EXISTS x_test_case_85/
DROP TABLE IF EXISTS x_test_case_71/
DROP TABLE IF EXISTS x_test_case_64_69/
DROP TABLE IF EXISTS x_unused/
DROP TABLE IF EXISTS t_639_numbers_table/
DROP TABLE IF EXISTS t_658_ref/
DROP TABLE IF EXISTS t_658_11/
DROP TABLE IF EXISTS t_658_21/
DROP TABLE IF EXISTS t_658_31/
DROP TABLE IF EXISTS t_658_12/
DROP TABLE IF EXISTS t_658_22/
DROP TABLE IF EXISTS t_658_32/
DROP TABLE IF EXISTS t_725_lob_test/

DROP PROCEDURE IF EXISTS p_unused/
DROP PROCEDURE IF EXISTS p_author_exists/
DROP PROCEDURE IF EXISTS p_create_author/ 
DROP PROCEDURE IF EXISTS p_create_author_by_name/ 
DROP PROCEDURE IF EXISTS p391/
DROP FUNCTION IF EXISTS f_author_exists/
DROP FUNCTION IF EXISTS f_one/
DROP FUNCTION IF EXISTS f_number/
DROP FUNCTION IF EXISTS f317/

CREATE TABLE t_language (
  cd CHAR(2) NOT NULL,
  description VARCHAR(50) NULL,
  description_english VARCHAR(50) NULL,
  id INTEGER NOT NULL,
  
  CONSTRAINT pk_t_language PRIMARY KEY (ID)
)
/
COMMENT ON TABLE t_language IS 'An entity holding language master data'
/
COMMENT ON COLUMN t_language.id IS 'The language ID'
/
COMMENT ON COLUMN t_language.cd IS 'The language ISO code'
/
COMMENT ON COLUMN t_language.description IS 'The language description'
/
CREATE TABLE t_658_11 (
  id CHAR(3) NOT NULL,
  
  CONSTRAINT pk_t_658_11 PRIMARY KEY (id)
)
/

CREATE TABLE t_658_21 (
  id INT NOT NULL,
  
  CONSTRAINT pk_t_658_21 PRIMARY KEY (id)
)
/

CREATE TABLE t_658_31 (
  id BIGINT NOT NULL,
  
  CONSTRAINT pk_t_658_31 PRIMARY KEY (id)
)
/

CREATE TABLE t_658_12 (
  id CHAR(3) NOT NULL,
  cd CHAR(3) NOT NULL,
  
  CONSTRAINT pk_t_658_12 PRIMARY KEY (id)
)
/

CREATE TABLE t_658_22 (
  id INT NOT NULL,
  cd INT NOT NULL,
  
  CONSTRAINT pk_t_658_22 PRIMARY KEY (id)
)
/

CREATE TABLE t_658_32 (
  id BIGINT NOT NULL,
  cd BIGINT NOT NULL,
  
  CONSTRAINT pk_t_658_32 PRIMARY KEY (id)
)
/

CREATE TABLE t_658_ref (
  ref_11 char(3),
  ref_21 int,
  ref_31 bigint,
  ref_12 char(3),
  ref_22 int,
  ref_32 bigint,

  CONSTRAINT fk_t_658_11 FOREIGN KEY (ref_11) REFERENCES t_658_11(id),
  CONSTRAINT fk_t_658_21 FOREIGN KEY (ref_21) REFERENCES t_658_21(id),
  CONSTRAINT fk_t_658_31 FOREIGN KEY (ref_31) REFERENCES t_658_31(id),
  CONSTRAINT fk_t_658_12 FOREIGN KEY (ref_12) REFERENCES t_658_12(id),
  CONSTRAINT fk_t_658_22 FOREIGN KEY (ref_22) REFERENCES t_658_22(id),
  CONSTRAINT fk_t_658_32 FOREIGN KEY (ref_32) REFERENCES t_658_32(id)
)
/

CREATE TABLE t_725_lob_test (
  ID int NOT NULL,
  LOB LONG BINARY NULL,
  
  CONSTRAINT pk_t_725_lob_test PRIMARY KEY (id)
)
/

CREATE TABLE t_author (
  ID INT NOT NULL,
  FIRST_NAME VARCHAR(50) NULL,
  LAST_NAME VARCHAR(50) NULL,
  DATE_OF_BIRTH DATE NULL,
  YEAR_OF_BIRTH INT NULL,
  ADDRESS VARCHAR(200) NULL,
  
  CONSTRAINT pk_t_author PRIMARY KEY (ID)
) 
/
COMMENT ON TABLE t_author IS 'An entity holding authors of books'
/
COMMENT ON COLUMN t_author.id IS 'The author ID'
/
COMMENT ON COLUMN t_author.first_name IS 'The author''s first name'
/
COMMENT ON COLUMN t_author.last_name IS 'The author''s last name'
/
COMMENT ON COLUMN t_author.date_of_birth IS 'The author''s date of birth'
/
COMMENT ON COLUMN t_author.year_of_birth IS 'The author''s year of birth'
/
COMMENT ON COLUMN t_author.address IS 'The author''s address'
/

CREATE TABLE t_book_details (
  ID INT NOT NULL,
    
  CONSTRAINT pk_t_book_details PRIMARY KEY (ID)
)
/

CREATE TABLE t_book (
  ID INT NOT NULL,
  AUTHOR_ID INT NOT NULL,
  CO_AUTHOR_ID INT NULL,
  DETAILS_ID INT NULL,
  TITLE VARCHAR(400) NOT NULL,
  PUBLISHED_IN INT NOT NULL,
  LANGUAGE_ID INT NOT NULL,
  CONTENT_TEXT LONG VARCHAR NULL,
  CONTENT_PDF LONG BINARY NULL,
  
  CONSTRAINT pk_t_book PRIMARY KEY (ID),
  CONSTRAINT fk_t_book_author_id FOREIGN KEY (AUTHOR_ID) REFERENCES T_AUTHOR(ID),
  CONSTRAINT fk_t_book_co_author_id FOREIGN KEY (CO_AUTHOR_ID) REFERENCES T_AUTHOR(ID),
  CONSTRAINT fk_t_book_details_id FOREIGN KEY (DETAILS_ID) REFERENCES T_BOOK_DETAILS(ID), 
  CONSTRAINT fk_t_book_language_id FOREIGN KEY (LANGUAGE_ID) REFERENCES T_LANGUAGE(ID)
)
/
COMMENT ON TABLE t_book IS 'An entity holding books'
/
COMMENT ON COLUMN t_book.id IS 'The book ID'
/
COMMENT ON COLUMN t_book.author_id IS 'The author ID in entity ''author'''
/
COMMENT ON COLUMN t_book.title IS 'The book''s title'
/
COMMENT ON COLUMN t_book.published_in IS  'The year the book was published in'
/
COMMENT ON COLUMN t_book.language_id IS  'The language of the book'
/
COMMENT ON COLUMN t_book.content_text IS 'Some textual content of the book'
/
COMMENT ON COLUMN t_book.content_pdf IS 'Some binary content of the book'
/

CREATE TABLE t_book_store (
  id INTEGER NOT NULL IDENTITY,
  name VARCHAR(400) NOT NULL,
  
  CONSTRAINT uk_t_book_store_name UNIQUE(name) 
) 
/
COMMENT ON TABLE t_book_store IS 'A book store'
/
COMMENT ON COLUMN t_book_store.name IS 'The books store name'
/
CREATE TABLE t_book_to_book_store (
  book_store_name VARCHAR(400) NOT NULL,
  book_id INTEGER NOT NULL,
  stock INTEGER,
   
  CONSTRAINT pk_b2bs PRIMARY KEY(book_store_name, book_id),
  CONSTRAINT fk_b2bs_bs_name FOREIGN KEY (book_store_name)
                             REFERENCES t_book_store (name)
                             ON DELETE CASCADE,
  CONSTRAINT fk_b2bs_b_id    FOREIGN KEY (book_id)
                             REFERENCES t_book (id)
                             ON DELETE CASCADE
) 
COMMENT ON TABLE t_book_to_book_store IS 'An m:n relation between books and book stores'
/
COMMENT ON COLUMN t_book_to_book_store.book_store_name IS 'The book store name'
/
COMMENT ON COLUMN t_book_to_book_store.book_id IS 'The book ID'
/
COMMENT ON COLUMN t_book_to_book_store.stock IS 'The number of books on stock'
/
CREATE TABLE x_unused (
  ID INT NOT NULL,
  NAME VARCHAR(10) NOT NULL,
  BIG_INTEGER DECIMAL(25),
  ID_REF INT,
  NAME_REF VARCHAR(10),
  CLASS INT,
  FIELDS INT,
  CONFIGURATION INT,
  U_D_T INT,
  META_DATA INT,
  TYPE0 INT,
  PRIMARY_KEY INT,
  PRIMARYKEY INT,	
  "FIELD 737" DECIMAL(25, 2),
	
  CONSTRAINT pk_x_unused PRIMARY KEY(ID, NAME),
  CONSTRAINT uk_x_unused_id UNIQUE(ID),
  CONSTRAINT fk_x_unused_self FOREIGN KEY(ID_REF, NAME_REF) REFERENCES X_UNUSED(ID, NAME)
) 
/

CREATE TABLE t_639_numbers_table (
  ID INT NOT NULL,
  BYTE TINYINT,
  SHORT SMALLINT,
  "INTEGER" INT,
  "LONG" BIGINT,
  BYTE_DECIMAL DECIMAL(2, 0),
  SHORT_DECIMAL DECIMAL(4, 0),
  INTEGER_DECIMAL DECIMAL(9, 0),
  LONG_DECIMAL DECIMAL(18, 0),
  BIG_INTEGER DECIMAL(22, 0),
  BIG_DECIMAL DECIMAL(22, 5),
  "FLOAT" REAL,
  "DOUBLE" DOUBLE,
  
  CONSTRAINT pk_t_639_numbers_table PRIMARY KEY(ID)
)
/

CREATE TABLE x_test_case_64_69 (
  ID INT NOT NULL,
  UNUSED_ID INT,
   
  CONSTRAINT pk_x_test_case_64_69 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_64_69 FOREIGN KEY(UNUSED_ID) REFERENCES X_UNUSED(ID)
) 
/

CREATE TABLE x_test_case_71 (
  ID INT NOT NULL,
  TEST_CASE_64_69_ID INT,
 
  CONSTRAINT pk_x_test_case_71 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_71 FOREIGN KEY(TEST_CASE_64_69_ID) REFERENCES X_TEST_CASE_64_69(ID)
) 
/

CREATE TABLE x_test_case_85 (
  id int NOT NULL,
  x_unused_id int,
  x_unused_name VARCHAR(10),
	
  CONSTRAINT pk_x_test_case_85 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_85 FOREIGN KEY(x_unused_id, x_unused_name) REFERENCES X_UNUSED(id, name)
)
/

CREATE OR REPLACE VIEW V_LIBRARY (AUTHOR, TITLE) AS
SELECT A.FIRST_NAME || ' ' || A.LAST_NAME, B.TITLE
FROM T_AUTHOR A JOIN T_BOOK B ON B.AUTHOR_ID = A.ID;
/

CREATE VIEW v_author AS
SELECT * FROM t_author
/

CREATE VIEW v_book AS
SELECT * FROM t_book
/

CREATE PROCEDURE p_unused (IN in1 VARCHAR(50), OUT out1 INT, INOUT out2 INT)
BEGIN
END
/

CREATE PROCEDURE p_create_author_by_name (IN p_first_name VARCHAR(50), IN p_last_name VARCHAR(50))
BEGIN
	INSERT INTO t_author (id, first_name, last_name)
	VALUES ((select max(id) + 1 from t_author), p_first_name, p_last_name)
END
/

CREATE PROCEDURE p_create_author()
BEGIN
	call p_create_author_by_name('William', 'Shakespeare')
END
/

CREATE PROCEDURE p_author_exists (IN author_name VARCHAR(50), OUT result INT)
BEGIN
  SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
    INTO result
    FROM t_author 
   WHERE first_name LIKE author_name 
      OR last_name LIKE author_name
END 
/

CREATE PROCEDURE p391 (
	IN i1 INT, INOUT io1 INT, OUT o1 INT,
	OUT o2 INT, INOUT io2 INT, IN i2 INT)
BEGIN
  set o1 = io1;
  set io1 = i1;

  set o2 = io2;
  set io2 = i2;
END
/

CREATE FUNCTION f_author_exists (author_name VARCHAR(50))
  RETURNS INT
BEGIN
  declare @result INT;

  SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
    INTO @result
    FROM t_author 
   WHERE first_name LIKE author_name 
      OR last_name LIKE author_name;
      
  return @result;
END 
/

CREATE FUNCTION f_one ()
  RETURNS INT
BEGIN
  RETURN 1
END
/

CREATE FUNCTION f_number (n int)
  RETURNS INT
BEGIN
  RETURN n
END
/

CREATE FUNCTION f317 (p1 int, p2 int, p3 int, p4 int)
  RETURNS INT
BEGIN
  RETURN 1000 * p1 + 100 * p2 + p4
END
/
