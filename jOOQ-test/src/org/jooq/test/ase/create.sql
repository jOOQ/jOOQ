DROP VIEW v_library/
DROP VIEW v_author/
DROP VIEW v_book/

DROP TRIGGER t_triggers_trigger/

DROP TABLE t_triggers/
DROP TABLE t_book_to_book_store/
DROP TABLE t_book_store/
DROP TABLE t_book/
DROP TABLE t_book_details/
DROP TABLE t_author/
DROP TABLE t_language/
DROP TABLE x_test_case_85/
DROP TABLE x_test_case_71/
DROP TABLE x_test_case_64_69/
DROP TABLE x_unused/
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

DROP PROCEDURE p_unused/
DROP PROCEDURE p_author_exists/
DROP PROCEDURE p_create_author/ 
DROP PROCEDURE p_create_author_by_name/ 
DROP PROCEDURE p391/
DROP FUNCTION f_author_exists/
DROP FUNCTION f_one/
DROP FUNCTION f_number/
DROP FUNCTION f317/

CREATE TABLE t_triggers (
  id_generated int IDENTITY not null,
  id int null,
  counter int null,
  
  CONSTRAINT pk_t_triggers PRIMARY KEY (id_generated)
)
/

CREATE TRIGGER t_triggers_trigger
ON t_triggers
FOR INSERT
AS UPDATE t_triggers SET id = id_generated, counter = id_generated * 2
/

CREATE TABLE t_language (
  cd CHAR(2) NOT NULL,
  description VARCHAR(50) NULL,
  description_english VARCHAR(50) NULL,
  id INTEGER NOT NULL,
  
  CONSTRAINT pk_t_language PRIMARY KEY (id)
)
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
  content_text text NULL,
  content_pdf BINARY(400) NULL,
  
  CONSTRAINT pk_t_book PRIMARY KEY (id),
  CONSTRAINT fk_t_book_author_id FOREIGN KEY (author_id) REFERENCES t_author(id),
  CONSTRAINT fk_t_book_co_author_id FOREIGN KEY (co_author_id) REFERENCES t_author(id),
  CONSTRAINT fk_t_book_details_id FOREIGN KEY (details_id) REFERENCES t_book_details(id), 
  CONSTRAINT fk_t_book_language_id FOREIGN KEY (language_id) REFERENCES t_language(id)
)
/

CREATE TABLE t_book_store (
  id INTEGER IDENTITY NOT NULL,
  name VARCHAR(400) NOT NULL,
  
  CONSTRAINT uk_t_book_store_name UNIQUE(name) 
) 
/

CREATE TABLE t_book_to_book_store (
  book_store_name VARCHAR(400) NOT NULL,
  book_id INTEGER NOT NULL,
  stock INTEGER,
   
  CONSTRAINT pk_b2bs PRIMARY KEY(book_store_name, book_id),
  CONSTRAINT fk_b2bs_bs_name FOREIGN KEY (book_store_name)
                             REFERENCES t_book_store (name),
  CONSTRAINT fk_b2bs_b_id    FOREIGN KEY (book_id)
                             REFERENCES t_book (id)
) 
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
  [FIELD 737] DECIMAL(25, 2),
	
  CONSTRAINT pk_x_unused PRIMARY KEY(ID, NAME),
  CONSTRAINT uk_x_unused_id UNIQUE(ID),
  CONSTRAINT fk_x_unused_self FOREIGN KEY(ID_REF, NAME_REF) REFERENCES x_unused(ID, NAME)
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

CREATE TABLE x_test_case_64_69 (
  ID INT NOT NULL,
  UNUSED_ID INT,
   
  CONSTRAINT pk_x_test_case_64_69 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_64_69 FOREIGN KEY(UNUSED_ID) REFERENCES x_unused(ID)
) 
/

CREATE TABLE x_test_case_71 (
  ID INT NOT NULL,
  TEST_CASE_64_69_ID INT,
 
  CONSTRAINT pk_x_test_case_71 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_71 FOREIGN KEY(TEST_CASE_64_69_ID) REFERENCES x_test_case_64_69(ID)
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