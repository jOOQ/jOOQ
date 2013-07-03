DROP TABLE t_dates/
DROP TABLE t_triggers/
DROP TABLE t_book_to_book_store/
DROP TABLE t_book_store/
DROP TABLE t_book/
DROP TABLE t_book_details/
DROP TABLE t_author/
DROP TABLE t_language/
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
DROP TABLE t_unsigned/
DROP TABLE t_booleans/
DROP TABLE t_identity/
DROP TABLE t_identity_pk/

CREATE TABLE t_identity_pk (
  id int IDENTITY NOT NULL,
  val int
)
/

CREATE TABLE t_identity (
  id int IDENTITY NOT NULL,
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
  yes_no_lc varchar(3) null,
  yes_no_uc varchar(3) null,
  y_n_lc char(1) null,
  y_n_uc char(1) null,
  vc_boolean varchar(1) null,
  c_boolean char(1) null,
  n_boolean int null,

  CONSTRAINT pk_t_booleans PRIMARY KEY (id)
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
  id_generated int IDENTITY not null,
  id int null,
  counter int null
)
/

CREATE TABLE t_language (
  cd CHAR(2) NOT NULL,
  descr VARCHAR(50) NULL,
  description_english VARCHAR(50) NULL,
  id int NOT NULL,

  CONSTRAINT pk_t_language PRIMARY KEY (id)
)
/

CREATE TABLE t_725_lob_test (
  id int NOT NULL,
  lob VARBINARY(500) NULL,

  CONSTRAINT pk_t_725_lob_test PRIMARY KEY (id)
)
/

CREATE TABLE t_785 (
  ID int NULL,
  NAME varchar(50) NULL,
  VALUE varchar(50) NULL
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

  CONSTRAINT pk_t_book_details PRIMARY KEY (id)
)
/

CREATE TABLE t_book (
  id INT NOT NULL,
  author_id INT NOT NULL,
  co_author_id INT NULL,
  details_id INT NULL,
  title VARCHAR(400) NOT NULL,
  published_in INT NOT NULL,
  language_id INT NOT NULL,
  content_text CLOB NULL,
  content_pdf BINARY(400) NULL,

  CONSTRAINT pk_t_book PRIMARY KEY (id),
  CONSTRAINT fk_t_book_author_id FOREIGN KEY (author_id) REFERENCES t_author(id),
  CONSTRAINT fk_t_book_co_author_id FOREIGN KEY (co_author_id) REFERENCES t_author(id),
  CONSTRAINT fk_t_book_details_id FOREIGN KEY (details_id) REFERENCES t_book_details(id),
  CONSTRAINT fk_t_book_language_id FOREIGN KEY (language_id) REFERENCES t_language(id)
)
/

CREATE TABLE t_book_store (
  name VARCHAR(400) NOT NULL,

  CONSTRAINT uk_t_book_store_name PRIMARY KEY(name)
)
/

CREATE TABLE t_book_to_book_store (
  book_store_name VARCHAR(400) NOT NULL,
  book_id int NOT NULL,
  stock int,

  CONSTRAINT fk_b2bs_bs_name FOREIGN KEY (book_store_name)
                             REFERENCES t_book_store (name),
  CONSTRAINT fk_b2bs_b_id    FOREIGN KEY (book_id)
                             REFERENCES t_book (id)
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
  BYTE TINYINT NULL,
  SHORT SMALLINT NULL,
  [INTEGER] INT NULL,
  [LONG] BIGINT NULL,
  BYTE_DECIMAL DECIMAL(2, 0) NULL,
  SHORT_DECIMAL DECIMAL(4, 0) NULL,
  INTEGER_DECIMAL DECIMAL(9, 0) NULL,
  LONG_DECIMAL DECIMAL(18, 0) NULL,
  BIG_INTEGER DECIMAL(22, 0) NULL,
  BIG_DECIMAL DECIMAL(22, 5) NULL,
  [FLOAT] REAL NULL,
  [DOUBLE] DOUBLE PRECISION NULL,

  CONSTRAINT pk_t_639_numbers_table PRIMARY KEY(ID)
)
/
