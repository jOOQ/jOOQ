DROP VIEW IF EXISTS v_library/
DROP VIEW IF EXISTS v_author/
DROP VIEW IF EXISTS v_book/

DROP PROCEDURE IF EXISTS p_unused/
DROP PROCEDURE IF EXISTS p_author_exists/
DROP PROCEDURE IF EXISTS p_create_author/
DROP PROCEDURE IF EXISTS p_create_author_by_name/
DROP PROCEDURE IF EXISTS p391/
DROP PROCEDURE IF EXISTS p2412/
DROP FUNCTION IF EXISTS f_author_exists/
DROP FUNCTION IF EXISTS f_one/
DROP FUNCTION IF EXISTS f_number/
DROP FUNCTION IF EXISTS f317/

DROP PROCEDURE IF EXISTS fp1908/
DROP FUNCTION IF EXISTS fp1908/

DROP TRIGGER IF EXISTS t_triggers_trigger/

DROP TABLE IF EXISTS t_dates/
DROP TABLE IF EXISTS t_triggers/
DROP TABLE IF EXISTS t_book_to_book_store/
DROP TABLE IF EXISTS t_book_store/
DROP TABLE IF EXISTS t_book/
DROP TABLE IF EXISTS t_book_details/
DROP TABLE IF EXISTS t_author/
DROP TABLE IF EXISTS t_language/
DROP TABLE IF EXISTS x_test_case_2025/
DROP TABLE IF EXISTS x_test_case_71/
DROP TABLE IF EXISTS x_test_case_64_69/
DROP TABLE IF EXISTS x_test_case_85/
DROP TABLE IF EXISTS x_unused/
DROP TABLE IF EXISTS t_exotic_types/
DROP TABLE IF EXISTS t_639_numbers_table/
DROP TABLE IF EXISTS t_658_ref/
DROP TABLE IF EXISTS t_658_11/
DROP TABLE IF EXISTS t_658_21/
DROP TABLE IF EXISTS t_658_31/
DROP TABLE IF EXISTS t_658_12/
DROP TABLE IF EXISTS t_658_22/
DROP TABLE IF EXISTS t_658_32/
DROP TABLE IF EXISTS t_725_lob_test/
DROP TABLE IF EXISTS t_785/
DROP TABLE IF EXISTS t_unsigned/
DROP TABLE IF EXISTS t_959/
DROP TABLE IF EXISTS t_booleans/
DROP TABLE IF EXISTS t_identity_pk/
DROP TABLE IF EXISTS t_2926/

CREATE TABLE t_2926 (
  t1 TINYTEXT,
  t2 TEXT,
  t3 MEDIUMTEXT,
  t4 LONGTEXT
)
/

CREATE TABLE t_identity_pk (
  id INT NOT NULL AUTO_INCREMENT,
  val int,

  CONSTRAINT pk_t_identity_pk PRIMARY KEY (id)
)
/

CREATE TABLE t_dates (
  id int,
  d date,
  t time,
  ts datetime,
  d_int int,
  ts_bigint bigint,
  y2 year(2),
  y4 year(4),

  CONSTRAINT pk_t_dates PRIMARY KEY (id)
)
/

CREATE TABLE t_booleans (
  id int,
  one_zero int,
  true_false_lc varchar(5),
  true_false_uc varchar(5),
  yes_no_lc enum('yes', 'no'),
  yes_no_uc enum('YES', 'NO'),
  y_n_lc char(1),
  y_n_uc char(1),
  vc_boolean varchar(1),
  c_boolean char(1),
  n_boolean int,

  CONSTRAINT pk_t_booleans PRIMARY KEY (id)
)
/

CREATE TABLE t_959 (
  java_keywords enum('abstract', 'assert', 'boolean', 'break', 'byte', 'case', 'catch',
	                 'char', 'class', 'const', 'continue', 'default', 'double', 'do',
	                 'else', 'enum', 'extends', 'false', 'final', 'finally', 'float',
	                 'for', 'goto', 'if', 'implements', 'import', 'instanceof',
	                 'interface', 'int', 'long', 'native', 'new', 'package', 'private',
	                 'protected', 'public', 'return', 'short', 'static', 'strictfp',
	                 'super', 'switch', 'synchronized', 'this', 'throw', 'throws',
	                 'transient', 'true', 'try', 'void', 'volatile', 'while'),
  special_characters enum('enum(', '(', ')', ',', '''', ')enum')
) ENGINE = InnoDB
/

CREATE TABLE t_unsigned (
  u_byte tinyint unsigned,
  u_short smallint unsigned,
  u_int int unsigned,
  u_long bigint unsigned
) ENGINE = InnoDB
/

CREATE TABLE t_triggers (
  id_generated int not null AUTO_INCREMENT,
  id int,
  counter int,

  CONSTRAINT pk_t_triggers PRIMARY KEY (id_generated)
) ENGINE = InnoDB
/

CREATE TRIGGER t_triggers_trigger
BEFORE INSERT
ON t_triggers
FOR EACH ROW
BEGIN
	DECLARE new_id INT;

	SELECT IFNULL(MAX(id_generated), 0) + 1 INTO new_id FROM t_triggers;

	SET NEW.id = new_id;
	SET NEW.counter = new_id * 2;
END;
/

CREATE TABLE t_language (
  CD CHAR(2) NOT NULL COMMENT 'The language ISO code',
  DESCRIPTION VARCHAR(50) COMMENT 'The language description',
  description_english VARCHAR(50),
  ID INT NOT NULL COMMENT 'The language ID',

  CONSTRAINT pk_t_language PRIMARY KEY (ID)
) ENGINE = InnoDB
  COMMENT 'An entity holding language master data'
/


CREATE TABLE t_725_lob_test (
  ID int NOT NULL,
  LOB LONGBLOB NULL,

  CONSTRAINT pk_t_725_lob_test PRIMARY KEY (id)
)
/

CREATE TABLE t_785 (
  ID int,
  NAME varchar(50),
  VALUE varchar(50)
)
/

CREATE TABLE t_author (
  ID INT NOT NULL COMMENT 'The author ID',
  FIRST_NAME VARCHAR(50) COMMENT 'The author''s first name',
  LAST_NAME VARCHAR(50) NOT NULL COMMENT 'The author''s last name',
  DATE_OF_BIRTH DATE COMMENT 'The author''s date of birth',
  YEAR_OF_BIRTH INT COMMENT 'The author''s year of birth',
  ADDRESS VARCHAR(200) COMMENT 'The author''s address',

  CONSTRAINT pk_t_author PRIMARY KEY (ID)
) ENGINE = InnoDB
  COMMENT = 'An entity holding authors of books';
/

CREATE TABLE t_book_details (
  ID INT NOT NULL COMMENT 'The details ID',
  E enum('A', 'B', 'C') COMMENT '#1237 Don''t generate an enum for this',

  CONSTRAINT pk_t_book_details PRIMARY KEY (ID)
) ENGINE = InnoDB
  COMMENT = 'An unused details table'
/

CREATE TABLE t_book (
  ID INT NOT NULL COMMENT 'The book ID',
  AUTHOR_ID INT NOT NULL COMMENT 'The author ID in entity ''author''',
  co_author_id int,
  DETAILS_ID INT COMMENT 'Some more details about the book',
  TITLE TEXT NOT NULL COMMENT 'The book''s title',
  PUBLISHED_IN INT NOT NULL COMMENT 'The year the book was published in',
  LANGUAGE_ID INT NOT NULL COMMENT 'The language of the book' DEFAULT 1,
  CONTENT_TEXT LONGTEXT COMMENT 'Some textual content of the book',
  CONTENT_PDF LONGBLOB COMMENT 'Some binary content of the book',
  STATUS enum('SOLD OUT','ORDERED','ON STOCK') COMMENT 'The book''s stock status',
  INDEX (AUTHOR_ID),
  INDEX (LANGUAGE_ID),

  CONSTRAINT pk_t_book PRIMARY KEY (ID),
  CONSTRAINT fk_t_book_author_id FOREIGN KEY (AUTHOR_ID) REFERENCES T_AUTHOR(ID),
  CONSTRAINT fk_t_book_co_author_id FOREIGN KEY (CO_AUTHOR_ID) REFERENCES T_AUTHOR(ID),
  CONSTRAINT fk_t_book_details_id FOREIGN KEY (DETAILS_ID) REFERENCES T_BOOK_DETAILS(ID),
  CONSTRAINT fk_t_book_language_id FOREIGN KEY (LANGUAGE_ID) REFERENCES T_LANGUAGE(ID)
) ENGINE = InnoDB
  COMMENT = 'An entity holding books';
/

CREATE TABLE t_book_store (
  name VARCHAR(400) NOT NULL COMMENT 'The books store name',

  CONSTRAINT uk_t_book_store_name PRIMARY KEY(name)
) ENGINE = InnoDB
  COMMENT = 'A book store'
/

CREATE TABLE t_book_to_book_store (
  book_store_name VARCHAR(400) NOT NULL COMMENT 'The book store name',
  book_id INTEGER NOT NULL COMMENT 'The book ID',
  stock INTEGER COMMENT 'The number of books on stock',

  CONSTRAINT pk_b2bs PRIMARY KEY(book_store_name, book_id),
  CONSTRAINT fk_b2bs_bs_name FOREIGN KEY (book_store_name)
                             REFERENCES t_book_store (name)
                             ON DELETE CASCADE,
  CONSTRAINT fk_b2bs_b_id    FOREIGN KEY (book_id)
                             REFERENCES t_book (id)
                             ON DELETE CASCADE
) ENGINE = InnoDB
  COMMENT = 'An m:n relation between books and book stores'
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
  `FIELD 737` DECIMAL(25, 2),

  CONSTRAINT pk_x_unused PRIMARY KEY(ID, NAME),
  CONSTRAINT uk_x_unused_id UNIQUE(ID),
  CONSTRAINT fk_x_unused_self FOREIGN KEY(ID_REF, NAME_REF) REFERENCES X_UNUSED(ID, NAME)
) ENGINE = InnoDB
  COMMENT = 'An unused table in the same schema.';
/

CREATE TABLE t_exotic_types (
  ID INT NOT NULL,
  UU BINARY(16),
  
  CONSTRAINT pk_t_exotic_types PRIMARY KEY(ID)
)
/

CREATE TABLE t_639_numbers_table (
  ID INT NOT NULL,
  BYTE TINYINT,
  SHORT SMALLINT,
  `INTEGER` INT,
  `LONG` BIGINT,
  BYTE_DECIMAL DECIMAL(2, 0),
  SHORT_DECIMAL DECIMAL(4, 0),
  INTEGER_DECIMAL DECIMAL(9, 0),
  LONG_DECIMAL DECIMAL(18, 0),
  BIG_INTEGER DECIMAL(22, 0),
  BIG_DECIMAL DECIMAL(22, 5),
  `FLOAT` FLOAT,
  `DOUBLE` DOUBLE,
  CONSTRAINT pk_t_639_numbers_table PRIMARY KEY(ID)
) ENGINE = InnoDB
/

CREATE TABLE x_test_case_64_69 (
  ID INT NOT NULL,
  UNUSED_ID INT,

  CONSTRAINT pk_x_test_case_64_69 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_64_69a FOREIGN KEY(UNUSED_ID) REFERENCES X_UNUSED(ID),
  CONSTRAINT fk_x_test_case_64_69b FOREIGN KEY(UNUSED_ID) REFERENCES X_UNUSED(ID)
) ENGINE = InnoDB
  COMMENT = 'An unused table in the same schema.';
/

CREATE TABLE x_test_case_71 (
  ID INT NOT NULL,
  TEST_CASE_64_69_ID INT,

  CONSTRAINT pk_x_test_case_71 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_71 FOREIGN KEY(TEST_CASE_64_69_ID) REFERENCES X_TEST_CASE_64_69(ID)
) ENGINE = InnoDB
  COMMENT = 'An unused table in the same schema.';
/

CREATE TABLE x_test_case_85 (
  id int NOT NULL,
  x_unused_id int,
  x_unused_name VARCHAR(10),

  CONSTRAINT pk_x_test_case_85 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_85 FOREIGN KEY(x_unused_id, x_unused_name) REFERENCES X_UNUSED(id, name)
) ENGINE = InnoDB
  COMMENT = 'An unused table in the same schema.';
/

CREATE TABLE x_test_case_2025 (
  ref_id int NOT NULL,
  ref_name VARCHAR(10) NOT NULL,
  
  CONSTRAINT fk_x_test_case_2025_1 FOREIGN KEY(ref_id) REFERENCES x_test_case_85(ID),
  CONSTRAINT fk_x_test_case_2025_2 FOREIGN KEY(ref_id) REFERENCES x_test_case_71(ID),
  CONSTRAINT fk_x_test_case_2025_3 FOREIGN KEY(ref_id, ref_name) REFERENCES X_UNUSED(id, name)
) ENGINE = InnoDB
  COMMENT = 'An unused table in the same schema.';
/

CREATE OR REPLACE VIEW V_LIBRARY (AUTHOR, TITLE) AS
SELECT CONCAT(A.FIRST_NAME, ' ', A.LAST_NAME), B.TITLE
FROM T_AUTHOR A JOIN T_BOOK B ON B.AUTHOR_ID = A.ID;
/

CREATE VIEW v_author AS
SELECT * FROM t_author
/

CREATE VIEW v_book AS
SELECT * FROM t_book
/


CREATE PROCEDURE p_unused (in1 VARCHAR(50), OUT out1 BOOL, INOUT out2 BOOL)
BEGIN
END
/

CREATE PROCEDURE p_create_author_by_name (IN first_name VARCHAR(50), IN last_name VARCHAR(50))
  COMMENT 'Create a new author'
BEGIN
	SET @id = 0;

	SELECT max(id) + 1 INTO @id FROM t_author;

	INSERT INTO T_AUTHOR (ID, FIRST_NAME, LAST_NAME)
	VALUES (@id, first_name, last_name);
END
/

CREATE PROCEDURE p_create_author()
  COMMENT 'Create a new author'
BEGIN
	call {jdbc.Schema}.p_create_author_by_name('William', 'Shakespeare');
END
/

CREATE PROCEDURE p_author_exists (author_name VARCHAR(50), OUT result INT)
  COMMENT 'Check existence of an author'
BEGIN
  SELECT COUNT(*) > 0 INTO result
    FROM t_author
   WHERE first_name LIKE author_name
      OR last_name LIKE author_name;
END
/

CREATE PROCEDURE p391 (
	i1 INTEGER, INOUT io1 INTEGER, OUT o1 INTEGER,
	OUT o2 INTEGER, INOUT io2 INTEGER, i2 INTEGER)
  COMMENT 'Integration tests for #391'
BEGIN
  SET o1 = io1;
  SET io1 = i1;

  SET o2 = io2;
  SET io2 = i2;
END
/

CREATE PROCEDURE p2412(
        In p_in_1 integer, 
        p_in_2 integer,
        Out p_out_1 decimal(12,2), 
        out p_out_2 decimal(12,2), 
        InOut p_in_out decimal(12,2))
BEGIN
  SET p_out_1 = 0;
  SET p_out_2 = 0;
  SET p_in_out = 0;
END
/

CREATE FUNCTION f_author_exists (author_name VARCHAR(50))
  RETURNS INT
  COMMENT 'Check existence of an author'
BEGIN
  RETURN (SELECT COUNT(*) > 0
    FROM t_author
   WHERE first_name LIKE author_name
      OR last_name LIKE author_name);
END
/

CREATE FUNCTION f_one ()
  RETURNS INT
  COMMENT '1 constant value'
BEGIN
  RETURN 1;
END
/

CREATE FUNCTION f_number (n int)
  RETURNS INT
  COMMENT 'echo n'
BEGIN
  RETURN n;
END
/

CREATE FUNCTION f317 (p1 int, p2 int, p3 int, p4 int)
  RETURNS INT
  COMMENT 'integration test for #317'
BEGIN
  RETURN 1000 * p1 + 100 * p2 + p4;
END
/

CREATE PROCEDURE fp1908(in p1 integer, out p2 integer)
BEGIN
  SET p2 = p1 + 1;
END
/

CREATE FUNCTION fp1908(p1 integer)
  RETURNS INT
BEGIN
  RETURN p1;
END
/