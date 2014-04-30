DROP VIEW v_library/
DROP VIEW v_author/
DROP VIEW v_book/

DROP TRIGGER t_triggers_trigger/

DROP TABLE t_dates/
DROP TABLE t_triggers/
DROP TABLE t_book_to_book_store/
DROP TABLE t_book_store/
DROP TABLE t_book/
DROP TABLE t_book_details/
DROP TABLE t_author/
DROP TABLE t_language/
DROP TABLE t_directory/
DROP TABLE x_test_case_2025/
DROP TABLE x_test_case_85/
DROP TABLE x_test_case_71/
DROP TABLE x_test_case_64_69/
DROP TABLE t_986_1/
DROP TABLE t_986_2/
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
DROP TABLE t_959/
DROP TABLE t_unsigned/
DROP TABLE t_booleans/
DROP TABLE t_identity/
DROP TABLE t_identity_pk/

CREATE TABLE t_identity_pk (
  id INTEGER AUTO_INCREMENT NOT NULL,
  val int,

  CONSTRAINT pk_t_identity_pk PRIMARY KEY (id)
)
/

CREATE TABLE t_identity (
  id INTEGER AUTO_INCREMENT NOT NULL,
  val int
)
/

CREATE TABLE t_dates (
  id int,
  d date null,
  t time null,
  ts datetime null,
  d_int int null,
  ts_bigint bigint null,

  CONSTRAINT pk_t_dates PRIMARY KEY (id)
)
/

CREATE TABLE t_booleans (
  id int,
  one_zero int null,
  true_false_lc varchar(5) null,
  true_false_uc varchar(5) null,
  yes_no_lc enum('yes', 'no'),
  yes_no_uc enum('YES', 'NO'),
  y_n_lc char(1) null,
  y_n_uc char(1) null,
  vc_boolean varchar(1) null,
  c_boolean char(1) null,
  n_boolean int null,

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
)
/

CREATE TABLE t_unsigned (
  u_byte smallint,
  u_short int,
  u_int bigint,
  u_long decimal(20)
)
/

CREATE TABLE t_triggers (
  id_generated int AUTO_INCREMENT not null,
  id int null,
  counter int null,

  CONSTRAINT pk_t_triggers PRIMARY KEY (id_generated)
)
/

CREATE TRIGGER t_triggers_trigger
AFTER INSERT
ON t_triggers
EXECUTE AFTER
UPDATE t_triggers SET id = id_generated, counter = id_generated * 2
/

CREATE TABLE t_directory (
  id           int NOT NULL,
  parent_id    int,
  is_directory int,
  "name"       varchar(50),

  CONSTRAINT pk_t_directory PRIMARY KEY (ID),
  CONSTRAINT pk_t_directory_self FOREIGN KEY (PARENT_ID) REFERENCES t_directory(ID) ON DELETE CASCADE
)
/

CREATE TABLE t_language (
  cd CHAR(2) NOT NULL,
  description VARCHAR(50) NULL,
  description_english VARCHAR(50) NULL,
  id INTEGER NOT NULL,

  CONSTRAINT pk_t_language PRIMARY KEY (id)
)
/

CREATE TABLE t_725_lob_test (
  id int NOT NULL,
  lob BLOB NULL,

  CONSTRAINT pk_t_725_lob_test PRIMARY KEY (id)
)
/

CREATE TABLE t_785 (
  "ID" int NULL,
  "NAME" varchar(50) NULL,
  "VALUE" varchar(50) NULL
)
/

CREATE TABLE t_author (
  id INT NOT NULL,
  first_name VARCHAR(50) NULL,
  last_name VARCHAR(50) NOT NULL,
  date_of_birth DATE NULL,
  year_of_birth INT NULL,
  address VARCHAR(200) NULL,

  CONSTRAINT pk_t_author PRIMARY KEY (id)
)
/

CREATE TABLE t_book_details (
  id INT NOT NULL,
  E enum('A', 'B', 'C'),

  CONSTRAINT pk_t_book_details PRIMARY KEY (id)
)
/

CREATE TABLE t_book (
  ID INT NOT NULL,
  AUTHOR_ID INT NOT NULL,
  CO_AUTHOR_ID INT NULL,
  DETAILS_ID INT NULL,
  TITLE VARCHAR(400) NOT NULL,
  PUBLISHED_IN INT NOT NULL,
  LANGUAGE_ID INT NOT NULL DEFAULT 1,
  CONTENT_TEXT CLOB NULL,
  CONTENT_PDF BLOB NULL,
  STATUS enum('SOLD OUT','ORDERED','ON STOCK'),

  CONSTRAINT pk_t_book PRIMARY KEY (ID),
  CONSTRAINT fk_t_book_author_id FOREIGN KEY (AUTHOR_ID) REFERENCES t_author(id) ON DELETE CASCADE,
  CONSTRAINT fk_t_book_co_author_id FOREIGN KEY (CO_AUTHOR_ID) REFERENCES t_author(id) ON DELETE CASCADE,
  CONSTRAINT fk_t_book_details_id FOREIGN KEY (DETAILS_ID) REFERENCES t_book_details(id) ON DELETE CASCADE,
  CONSTRAINT fk_t_book_language_id FOREIGN KEY (LANGUAGE_ID) REFERENCES t_language(id) ON DELETE CASCADE
)
/

CREATE TABLE t_book_store (
  name VARCHAR(400) NOT NULL,

  CONSTRAINT uk_t_book_store_name PRIMARY KEY(name)
)
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
/

CREATE TABLE x_unused (
  ID INT NOT NULL,
  NAME VARCHAR(10) NOT NULL,
  BIG_INTEGER DECIMAL(25),
  ID_REF INT,
  NAME_REF VARCHAR(10),
  "CLASS" INT,
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
  CONSTRAINT fk_x_unused_self FOREIGN KEY(ID_REF, NAME_REF) REFERENCES x_unused(ID, NAME)
)
/

CREATE TABLE t_986_1 (
  "REF" INT,

  CONSTRAINT pk_986 PRIMARY KEY("REF")
)
/

CREATE TABLE t_986_2 (
  "REF" INT,

  CONSTRAINT pk_986 PRIMARY KEY("REF")
)
/

CREATE TABLE t_exotic_types (
  ID INT NOT NULL,
  UU CHAR(36),
  
  CONSTRAINT pk_t_exotic_types PRIMARY KEY(ID)
)
/

CREATE TABLE t_639_numbers_table (
  ID INT NOT NULL,
  "SHORT" SMALLINT NULL,
  "INTEGER" INT NULL,
  "LONG" BIGINT NULL,
  BYTE_DECIMAL DECIMAL(2, 0) NULL,
  SHORT_DECIMAL DECIMAL(4, 0) NULL,
  INTEGER_DECIMAL DECIMAL(9, 0) NULL,
  LONG_DECIMAL DECIMAL(18, 0) NULL,
  BIG_INTEGER DECIMAL(22, 0) NULL,
  BIG_DECIMAL DECIMAL(22, 5) NULL,
  "FLOAT" REAL NULL,
  "DOUBLE" DOUBLE PRECISION NULL,

  CONSTRAINT pk_t_639_numbers_table PRIMARY KEY(ID)
)
/

CREATE TABLE x_test_case_64_69 (
  ID INT NOT NULL,
  UNUSED_ID INT,

  CONSTRAINT pk_x_test_case_64_69 PRIMARY KEY(ID) --,
  --[cannot reference UK] CONSTRAINT fk_x_test_case_64_69 FOREIGN KEY(UNUSED_ID) REFERENCES x_unused(ID)
)
/

CREATE TABLE x_test_case_71 (
  ID INT NOT NULL,
  TEST_CASE_64_69_ID INT,

  CONSTRAINT pk_x_test_case_71 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_71a FOREIGN KEY(TEST_CASE_64_69_ID) REFERENCES x_test_case_64_69(ID),
  CONSTRAINT fk_x_test_case_71b FOREIGN KEY(TEST_CASE_64_69_ID) REFERENCES x_test_case_64_69(ID)
)
/

CREATE TABLE x_test_case_85 (
  id int NOT NULL,
  x_unused_id int,
  x_unused_name VARCHAR(10),

  CONSTRAINT pk_x_test_case_85 PRIMARY KEY(id),
  CONSTRAINT fk_x_test_case_85 FOREIGN KEY(x_unused_id, x_unused_name) REFERENCES x_unused(ID, NAME)
)
/

CREATE TABLE x_test_case_2025 (
  ref_id int NOT NULL,
  ref_name VARCHAR(10) NOT NULL,
  
  CONSTRAINT fk_x_test_case_2025_1 FOREIGN KEY(ref_id) REFERENCES x_test_case_85(ID),
  CONSTRAINT fk_x_test_case_2025_2 FOREIGN KEY(ref_id) REFERENCES x_test_case_71(ID),
  CONSTRAINT fk_x_test_case_2025_3 FOREIGN KEY(ref_id, ref_name) REFERENCES X_UNUSED(id, name)
)
/

CREATE VIEW v_library (author, title) AS
SELECT a.first_name || ' ' || a.last_name, b.title
FROM t_author a JOIN t_book b ON b.author_id = a.id
/

CREATE VIEW v_author AS
SELECT * FROM t_author
/

CREATE VIEW v_book AS
SELECT * FROM t_book
/