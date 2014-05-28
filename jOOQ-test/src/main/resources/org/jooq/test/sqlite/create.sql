PRAGMA JOURNAL_MODE=OFF/
PRAGMA SYNCHRONOUS=OFF/
DROP VIEW IF EXISTS v_library/
DROP VIEW IF EXISTS v_author/
DROP VIEW IF EXISTS v_book/

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
DROP TABLE IF EXISTS t_986_1/
DROP TABLE IF EXISTS t_986_2/
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
DROP TABLE IF EXISTS t_booleans/
DROP TABLE IF EXISTS t_dates/

CREATE TABLE t_dates (
  id int,
  d date,
  t time,
  ts datetime,
  d_int int,
  ts_bigint int8,

  CONSTRAINT pk_t_dates PRIMARY KEY (id)
)
/

CREATE TABLE t_booleans (
  id int,
  one_zero int,
  true_false_lc varchar(5),
  true_false_uc varchar(5),
  yes_no_lc varchar(3),
  yes_no_uc varchar(3),
  y_n_lc char(1),
  y_n_uc char(1),
  vc_boolean varchar(1),
  c_boolean char(1),
  n_boolean int,

  CONSTRAINT pk_t_booleans PRIMARY KEY (id)
)
/

CREATE TABLE t_unsigned (
  u_byte smallint,
  u_short int,
  u_int bigint,
  u_long varchar(20)
)
/

CREATE TABLE t_triggers (
  id_generated integer primary key autoincrement,
  id int,
  counter int
)
/

CREATE TRIGGER IF NOT EXISTS t_triggers_trigger
AFTER INSERT
ON t_triggers
FOR EACH ROW
BEGIN
	update t_triggers
	set id = id_generated,
	    counter = id_generated * 2;
END;
/

CREATE TABLE t_language (
  cd CHAR(2) NOT NULL,
  description VARCHAR(50),
  description_english VARCHAR(50),
  id INTEGER NOT NULL,

  CONSTRAINT pk_t_language PRIMARY KEY (ID)
)
/


CREATE TABLE t_725_lob_test (
  ID int NOT NULL,
  LOB LONGVARBINARY NULL,

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
  ID INT NOT NULL,
  FIRST_NAME VARCHAR(50),
  LAST_NAME VARCHAR(50) NOT NULL,
  DATE_OF_BIRTH DATE,
  YEAR_OF_BIRTH INT,
  ADDRESS VARCHAR(50),

  CONSTRAINT pk_t_author PRIMARY KEY (ID)
);
/

CREATE TABLE t_book_details (
  ID INT,

  CONSTRAINT pk_t_book_details PRIMARY KEY (ID)
);
/

CREATE TABLE t_book (
  ID INT NOT NULL,
  AUTHOR_ID INT NOT NULL,
  co_author_id INTEGER,
  DETAILS_ID INT,
  TITLE VARCHAR(400) NOT NULL,
  PUBLISHED_IN INT NOT NULL,
  LANGUAGE_ID INT NOT NULL DEFAULT 1,
  CONTENT_TEXT LONGVARCHAR,
  CONTENT_PDF LONGVARBINARY,

  CONSTRAINT pk_t_book PRIMARY KEY (ID),
  CONSTRAINT fk_t_book_author_id FOREIGN KEY (AUTHOR_ID) REFERENCES T_AUTHOR(ID),
  CONSTRAINT fk_t_book_co_author_id FOREIGN KEY (CO_AUTHOR_ID) REFERENCES T_AUTHOR(ID),
  CONSTRAINT fk_t_book_details_id FOREIGN KEY (DETAILS_ID) REFERENCES T_BOOK_DETAILS(ID),
  CONSTRAINT fk_t_book_language_id FOREIGN KEY (LANGUAGE_ID) REFERENCES T_LANGUAGE(ID)
);
/

CREATE TABLE t_book_store (
  name VARCHAR(400) NOT NULL,

  CONSTRAINT uk_t_book_store_name PRIMARY KEY(name)
);
/

CREATE TABLE t_book_to_book_store (
  book_store_name VARCHAR(400) NOT NULL,
  book_id INT NOT NULL,
  stock INT,

  CONSTRAINT pk_b2bs PRIMARY KEY(book_store_name, book_id),
  CONSTRAINT fk_b2bs_bs_name FOREIGN KEY (book_store_name)
                             REFERENCES t_book_store (name)
                             ON DELETE CASCADE,
  CONSTRAINT fk_b2bs_b_id    FOREIGN KEY (book_id)
                             REFERENCES t_book (id)
                             ON DELETE CASCADE
)
/


CREATE TABLE x_unused (
  ID INT NOT NULL,
  NAME VARCHAR(10) NOT NULL,
  BIG_INTEGER DECIMAL(25),
  ID_REF INT,
  CLASS INT,
  FIELDS INT,
  CONFIGURATION INT,
  U_D_T INT,
  META_DATA INT,
  TYPE0 INT,
  PRIMARY_KEY INT,
  PRIMARYKEY INT,
  NAME_REF VARCHAR(10),
  "FIELD 737" DECIMAL(25, 2),

  CONSTRAINT pk_x_unused PRIMARY KEY(ID, NAME),
  CONSTRAINT uk_x_unused_id UNIQUE(ID),
  CONSTRAINT fk_x_unused_self FOREIGN KEY(ID_REF, NAME_REF) REFERENCES X_UNUSED(ID, NAME)
);
/

CREATE TABLE t_986_1 (
  REF INT,

  CONSTRAINT pk_986 PRIMARY KEY(REF),
  CONSTRAINT uk_986 UNIQUE(REF),
  CONSTRAINT fk_986 FOREIGN KEY(REF) REFERENCES X_UNUSED(ID)
)
/

CREATE TABLE t_986_2 (
  REF INT,

  CONSTRAINT pk_986 PRIMARY KEY(REF),
  CONSTRAINT uk_986 UNIQUE(REF),
  CONSTRAINT fk_986 FOREIGN KEY(REF) REFERENCES X_UNUSED(ID)
)
/

CREATE TABLE t_exotic_types (
  ID INT NOT NULL,
  UU CHAR(36),
  
  CONSTRAINT pk_t_exotic_types PRIMARY KEY(ID)
);
/

CREATE TABLE t_639_numbers_table (
  ID INT NOT NULL,
  BYTE TINYINT,
  SHORT SMALLINT,
  INTEGER INT,
  LONG INT8,
  BYTE_DECIMAL DECIMAL(2, 0),
  SHORT_DECIMAL DECIMAL(4, 0),
  INTEGER_DECIMAL DECIMAL(9, 0),
  LONG_DECIMAL DECIMAL(18, 0),
  BIG_INTEGER DECIMAL(22, 0),
  BIG_DECIMAL DECIMAL(22, 5),
  FLOAT REAL,
  DOUBLE DOUBLE,

  CONSTRAINT pk_t_639_numbers_table PRIMARY KEY(ID)
);
/

CREATE TABLE x_test_case_64_69 (
  ID INT NOT NULL,
  UNUSED_ID INT,

  CONSTRAINT pk_x_test_case_64_69 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_64_69a FOREIGN KEY(UNUSED_ID) REFERENCES X_UNUSED(ID),
  CONSTRAINT fk_x_test_case_64_69b FOREIGN KEY(UNUSED_ID) REFERENCES X_UNUSED(ID)
);
/

CREATE TABLE x_test_case_71 (
  ID INT NOT NULL,
  TEST_CASE_64_69_ID SMALLINT,

  CONSTRAINT pk_x_test_case_71 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_71 FOREIGN KEY(TEST_CASE_64_69_ID) REFERENCES X_TEST_CASE_64_69(ID)
);
/

CREATE TABLE x_test_case_85 (
  id int NOT NULL,
  x_unused_id int,
  x_unused_name VARCHAR(10),

  CONSTRAINT pk_x_test_case_85 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_85 FOREIGN KEY(x_unused_id, x_unused_name) REFERENCES X_UNUSED(id, name)
);
/

CREATE TABLE x_test_case_2025 (
  ref_id int NOT NULL,
  ref_name VARCHAR(10) NOT NULL,
  
  CONSTRAINT fk_x_test_case_2025_1 FOREIGN KEY(ref_id) REFERENCES x_test_case_85(ID),
  CONSTRAINT fk_x_test_case_2025_2 FOREIGN KEY(ref_id) REFERENCES x_test_case_71(ID),
  CONSTRAINT fk_x_test_case_2025_3 FOREIGN KEY(ref_id, ref_name) REFERENCES X_UNUSED(id, name)
);
/

CREATE VIEW V_LIBRARY AS
SELECT T_AUTHOR.FIRST_NAME AUTHOR, T_BOOK.TITLE TITLE
FROM T_AUTHOR JOIN T_BOOK ON T_BOOK.AUTHOR_ID = T_AUTHOR.ID;
/

CREATE VIEW v_author AS
SELECT * FROM t_author
/

CREATE VIEW v_book AS
SELECT * FROM t_book
/


