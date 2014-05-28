DROP VIEW v_author/
DROP VIEW v_book/
DROP VIEW v_library/

DROP TRIGGER t_triggers_trigger/
DROP SEQUENCE s_triggers_sequence/

DROP TABLE t_dates/
DROP TABLE t_triggers/
DROP TABLE t_arrays/
DROP TABLE t_book_to_book_store/
DROP TABLE t_book_store/
DROP TABLE t_book/
DROP TABLE t_book_details/
DROP TABLE t_author/
DROP TABLE t_language/
DROP TABLE x_test_case_2025/
DROP TABLE x_test_case_71/
DROP TABLE x_test_case_85/
DROP TABLE x_test_case_64_69/
DROP TABLE x_unused/
DROP TABLE t_exotic_types/
DROP TABLE t_639_numbers_table/
DROP TABLE t_658_ref/
DROP TABLE t_658_11/
DROP TABLE t_658_21/
DROP TABLE t_658_31/
DROP TABLE t_658_12/
DROP TABLE t_658_22/
DROP TABLE t_658_32/
DROP TABLE t_725_lob_test/
DROP TABLE t_785/
DROP TABLE T_937/
DROP TABLE "T_941"/
DROP TABLE "t_941"/
DROP TABLE T_943/
DROP TABLE t_unsigned/
DROP TABLE t_booleans/



DROP TABLE t_identity/
DROP TABLE t_identity_pk/


CREATE TABLE t_dates (
  id int,
  d date,
  t time,
  ts timestamp,
  d_int int,
  ts_bigint bigint,

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

CREATE SEQUENCE s_triggers_sequence/

CREATE TABLE t_triggers (
  id_generated int not null,
  id int,
  counter int,

  CONSTRAINT pk_t_triggers PRIMARY KEY (id_generated)
)
/

CREATE TRIGGER t_triggers_trigger
BEFORE INSERT
ON t_triggers
AS
BEGIN
	new.id_generated = next value for s_triggers_sequence;

	new.id = new.id_generated;
	new.counter = new.id_generated * 2;
END
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
  LOB BLOB,

  CONSTRAINT pk_t_725_lob_test PRIMARY KEY (id)
)
/

CREATE TABLE t_785 (
  ID int,
  NAME varchar(50),
  "VALUE" varchar(50)
)
/

CREATE TABLE T_937 (
  T_937 int,

  CONSTRAINT T_937 PRIMARY KEY (T_937)
)
/

CREATE TABLE t_author (
  ID INT,
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
  ID INT,
  AUTHOR_ID INT NOT NULL,
  CO_AUTHOR_ID INT,
  DETAILS_ID INT,
  TITLE VARCHAR(400) NOT NULL,
  PUBLISHED_IN INT NOT NULL,
  LANGUAGE_ID INT NOT NULL,
  CONTENT_TEXT BLOB SUB_TYPE TEXT,
  CONTENT_PDF BLOB,

  REC_VERSION INT,

  CONSTRAINT pk_t_book PRIMARY KEY (ID),
  CONSTRAINT fk_t_book_author_id FOREIGN KEY (AUTHOR_ID) REFERENCES T_AUTHOR(ID) ON DELETE CASCADE,
  CONSTRAINT fk_t_book_co_author_id FOREIGN KEY (CO_AUTHOR_ID) REFERENCES T_AUTHOR(ID) ON DELETE CASCADE,
  CONSTRAINT fk_t_book_details_id FOREIGN KEY (DETAILS_ID) REFERENCES T_BOOK_DETAILS(ID) ON DELETE CASCADE,
  CONSTRAINT fk_t_book_language_id FOREIGN KEY (LANGUAGE_ID) REFERENCES T_LANGUAGE(ID) ON DELETE CASCADE
);
/

ALTER TABLE t_book ALTER COLUMN language_id SET DEFAULT 1;
/

CREATE TABLE t_book_store (
  name VARCHAR(400) NOT NULL,

  CONSTRAINT uk_t_book_store_name PRIMARY KEY(name)
);
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
);
/


CREATE TABLE x_unused (
  ID INT NOT NULL,
  NAME VARCHAR(10) NOT NULL,
  BIG_INTEGER DECIMAL(18),
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
  "FIELD 737" DECIMAL(18, 2),
  CONNECTION INT,
  PREPARED_STATEMENT INT,

  CONSTRAINT pk_x_unused PRIMARY KEY(ID, NAME),
  CONSTRAINT uk_x_unused_id UNIQUE(ID),
  CONSTRAINT fk_x_unused_self FOREIGN KEY(ID_REF, NAME_REF) REFERENCES X_UNUSED(ID, NAME)
);
/

CREATE TABLE t_exotic_types (
  ID INT NOT NULL,
  UU CHAR(36),
  
  CONSTRAINT pk_t_exotic_types PRIMARY KEY(ID)
);
/

CREATE TABLE t_639_numbers_table (
  ID INT NOT NULL,
  SHORT SMALLINT,
  "INTEGER" INT,
  "LONG" BIGINT,
  BYTE_DECIMAL DECIMAL(2, 0),
  SHORT_DECIMAL DECIMAL(4, 0),
  INTEGER_DECIMAL DECIMAL(9, 0),
  LONG_DECIMAL DECIMAL(18, 0),
  BIG_DECIMAL DECIMAL(18, 5),
  "FLOAT" REAL,
  "DOUBLE" DOUBLE PRECISION,

  CONSTRAINT pk_t_639_numbers_table PRIMARY KEY(ID)
);
/

CREATE TABLE x_test_case_64_69 (
  ID INT NOT NULL,
  UNUSED_ID INT,

  CONSTRAINT pk_x_test_case_64_69 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_64_69 FOREIGN KEY(UNUSED_ID) REFERENCES X_UNUSED(ID)
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

CREATE VIEW V_LIBRARY (AUTHOR, TITLE) AS
SELECT T_AUTHOR.FIRST_NAME || ' ' || T_AUTHOR.LAST_NAME, T_BOOK.TITLE
FROM T_AUTHOR JOIN T_BOOK ON T_BOOK.AUTHOR_ID = T_AUTHOR.ID;
/

CREATE VIEW v_author AS
SELECT * FROM t_author
/

CREATE VIEW v_book AS
SELECT * FROM t_book
/
