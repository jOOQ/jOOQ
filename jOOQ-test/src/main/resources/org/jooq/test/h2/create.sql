DROP VIEW IF EXISTS v_3176/
DROP VIEW IF EXISTS v_2603/
DROP VIEW IF EXISTS "Ää"/
DROP VIEW IF EXISTS v_author/
DROP VIEW IF EXISTS v_book/
DROP VIEW IF EXISTS v_library/
DROP VIEW IF EXISTS "v.with.dots"/

DROP ALIAS IF EXISTS f_arrays1/
DROP ALIAS IF EXISTS f_arrays2/
DROP ALIAS IF EXISTS f_arrays3/
DROP ALIAS IF EXISTS f_author_exists/
DROP ALIAS IF EXISTS f_one/
DROP ALIAS IF EXISTS f_number/
DROP ALIAS IF EXISTS f317/
DROP ALIAS IF EXISTS f1256/
DROP ALIAS IF EXISTS p_create_author/
DROP ALIAS IF EXISTS p_create_author_by_name/
DROP ALIAS IF EXISTS f_get_one_cursor/

DROP TRIGGER IF EXISTS t_triggers_trigger/
DROP SEQUENCE IF EXISTS s_triggers_sequence/

DROP TABLE IF EXISTS t_dates/
DROP TABLE IF EXISTS t_triggers/
DROP TABLE IF EXISTS t_arrays/
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
DROP TABLE IF EXISTS t_877/
DROP TABLE IF EXISTS t_2486/
DROP TABLE IF EXISTS t_2698/
DROP TABLE IF EXISTS t_2718/
DROP TABLE IF EXISTS t_3485/
DROP TABLE IF EXISTS t_3488_abc_xyz_eee/
DROP TABLE IF EXISTS t_3488_abcxyz_eee/
DROP TABLE IF EXISTS t_3488_abc_xyzeee/
DROP TABLE IF EXISTS t_3488_abcxyzeee/
DROP TABLE IF EXISTS t_3571/
DROP TABLE IF EXISTS t_3666/
DROP TABLE IF EXISTS t_unsigned/
DROP TABLE IF EXISTS t_booleans/
DROP TABLE IF EXISTS t_identity/
DROP TABLE IF EXISTS t_identity_pk/
DROP TABLE IF EXISTS t_2327_uk_only/

CREATE TABLE t_3485 (
  id INT,
  pw VARCHAR(100),
  
  CONSTRAINT pk_t_3485 PRIMARY KEY (id)
)
/

CREATE TABLE t_3666 (
  e1 VARCHAR(10),
  e2 NUMBER,
  e3 NUMBER(10),
  e4 NUMBER(10, 0),
  e5 NUMBER(10, 2),
  e6 NUMBER(10, 3)
)
/

CREATE TABLE t_3571 (
  e1 INTEGER,
  e2 INTEGER NOT NULL,
  e3 INTEGER          DEFAULT 1,
  e4 INTEGER NOT NULL DEFAULT 1
)
/

CREATE TABLE t_2327_uk_only (
  id INTEGER,
  
  CONSTRAINT uk_t_2327_uk_only UNIQUE (id)
)
/

CREATE TABLE t_identity_pk (
  id INTEGER NOT NULL AUTO_INCREMENT,
  val int,

  CONSTRAINT pk_t_identity_pk PRIMARY KEY (id)
)
/

CREATE TABLE t_identity (
  id INTEGER AUTO_INCREMENT,
  val int
)
/

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
  vc_boolean varchar(5),
  c_boolean char(5),
  n_boolean int,

  CONSTRAINT pk_t_booleans PRIMARY KEY (id)
)
/

CREATE TABLE t_unsigned (
  u_byte smallint,
  u_short int,
  u_int bigint,
  u_long number
)
/

CREATE TABLE t_triggers (
  id_generated int AUTO_INCREMENT,
  id int,
  counter int,

  CONSTRAINT pk_t_triggers PRIMARY KEY (id_generated)
)
/

CREATE SEQUENCE s_triggers_sequence START WITH 1/

CREATE TRIGGER t_triggers_trigger
BEFORE INSERT
ON t_triggers
FOR EACH ROW
CALL "org.jooq.test.utils.h2.TTriggersTrigger"
/

CREATE TABLE t_language (
  cd CHAR(2) NOT NULL,
  description VARCHAR(50),
  description_english VARCHAR(50),
  id INTEGER NOT NULL,

  CONSTRAINT pk_t_language PRIMARY KEY (ID)
)
/

COMMENT ON TABLE t_language IS 'An entity holding language master data'/
COMMENT ON COLUMN t_language.id IS 'The language ID'/
COMMENT ON COLUMN t_language.cd IS 'The language ISO code'/
COMMENT ON COLUMN t_language.description IS 'The language description'/


CREATE TABLE t_725_lob_test (
  ID int NOT NULL,
  LOB BLOB NULL,

  CONSTRAINT pk_t_725_lob_test PRIMARY KEY (id)
)
/

CREATE TABLE t_785 (
  ID int,
  NAME varchar(50),
  VALUE varchar(50)
)
/

CREATE TABLE t_877 (
  ID INT AUTO_INCREMENT
)
/

CREATE TABLE t_2486 (
  VAL1 DECIMAL(20),
  VAL2 DECIMAL(20),
  VAL3 DECIMAL(20, 5),
  VAL4 DECIMAL(20, 5),
  VAL5 VARCHAR(20),
  VAL6 VARCHAR(20),
  VAL7 DECIMAL(20, 5),
  VAL8 DECIMAL
)
/

CREATE TABLE t_2698 (
  ID INT,
  XX INT DEFAULT -1 NOT NULL,
  YY INT DEFAULT -2 NOT NULL,
  
  CONSTRAINT pk_t_2698 PRIMARY KEY (ID)
)
/

CREATE TABLE t_2718 (
  EXCLUDE_ME INT,
  XX INT,
  
  CONSTRAINT pk_t_2718 PRIMARY KEY (EXCLUDE_ME)
)
/

CREATE TABLE t_3488_abc_xyz_eee (
  ID INT,
  
  CONSTRAINT pk_t_3488_1 PRIMARY KEY (ID)
)/
CREATE TABLE t_3488_abcxyz_eee (
  ID INT,
  
  CONSTRAINT pk_t_3488_2 PRIMARY KEY (ID)
)/
CREATE TABLE t_3488_abc_xyzeee (
  ID INT,
  
  CONSTRAINT pk_t_3488_3 PRIMARY KEY (ID)
)/
CREATE TABLE t_3488_abcxyzeee (
  ID INT,
  
  CONSTRAINT pk_t_3488_4 PRIMARY KEY (ID)
)/

ALTER TABLE t_3488_abc_xyz_eee ADD CONSTRAINT fk_t_3488_1 FOREIGN KEY (ID) REFERENCES t_3488_abcxyzeee (ID)/
ALTER TABLE t_3488_abcxyz_eee ADD CONSTRAINT fk_t_3488_2 FOREIGN KEY (ID) REFERENCES t_3488_abc_xyz_eee (ID)/
ALTER TABLE t_3488_abc_xyzeee ADD CONSTRAINT fk_t_3488_3 FOREIGN KEY (ID) REFERENCES t_3488_abcxyz_eee (ID)/
ALTER TABLE t_3488_abcxyzeee ADD CONSTRAINT fk_t_3488_4 FOREIGN KEY (ID) REFERENCES t_3488_abc_xyzeee (ID)/

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
COMMENT ON TABLE t_author IS 'An entity holding authors of books'/
COMMENT ON COLUMN t_author.id IS 'The author ID'/
COMMENT ON COLUMN t_author.first_name IS 'The author''s first name'/
COMMENT ON COLUMN t_author.last_name IS 'The author''s last name'/
COMMENT ON COLUMN t_author.date_of_birth IS 'The author''s date of birth'/
COMMENT ON COLUMN t_author.year_of_birth IS 'The author''s year of birth'/
COMMENT ON COLUMN t_author.address IS 'The author''s address'/

CREATE TABLE t_book_details (
    ID INT,

    CONSTRAINT pk_t_book_details PRIMARY KEY (ID)
)
/
COMMENT ON TABLE t_book_details IS 'An unused details table'/

CREATE TABLE t_book (
  ID INT NOT NULL,
  AUTHOR_ID INT NOT NULL,
  co_author_id int,
  DETAILS_ID INT,
  TITLE VARCHAR(400) NOT NULL,
  PUBLISHED_IN INT NOT NULL,
  LANGUAGE_ID INT NOT NULL DEFAULT 1,
  CONTENT_TEXT CLOB,
  CONTENT_PDF BLOB,

  REC_VERSION INT,
  REC_TIMESTAMP TIMESTAMP,

  CONSTRAINT pk_t_book PRIMARY KEY (ID),
  CONSTRAINT fk_t_book_author_id FOREIGN KEY (AUTHOR_ID) REFERENCES T_AUTHOR(ID),
  CONSTRAINT fk_t_book_co_author_id FOREIGN KEY (CO_AUTHOR_ID) REFERENCES T_AUTHOR(ID),
  CONSTRAINT fk_t_book_details_id FOREIGN KEY (DETAILS_ID) REFERENCES T_BOOK_DETAILS(ID),
  CONSTRAINT fk_t_book_language_id FOREIGN KEY (LANGUAGE_ID) REFERENCES T_LANGUAGE(ID)
);
/
COMMENT ON TABLE t_book IS 'An entity holding books'/
COMMENT ON COLUMN t_book.id IS 'The book ID'/
COMMENT ON COLUMN t_book.author_id IS 'The author ID in entity ''author'''/
COMMENT ON COLUMN t_book.title IS 'The book''s title'/
COMMENT ON COLUMN t_book.published_in IS  'The year the book was published in'/
COMMENT ON COLUMN t_book.language_id IS  'The language of the book'/
COMMENT ON COLUMN t_book.content_text IS 'Some textual content of the book'/
COMMENT ON COLUMN t_book.content_pdf IS 'Some binary content of the book'/

CREATE TABLE t_book_store (
  name VARCHAR(400) NOT NULL,

  CONSTRAINT uk_t_book_store_name PRIMARY KEY(name)
);
/
COMMENT ON TABLE t_book_store IS 'A book store'/
COMMENT ON COLUMN t_book_store.name IS 'The books store name'/


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
COMMENT ON TABLE t_book_to_book_store IS 'An m:n relation between books and book stores'/
COMMENT ON COLUMN t_book_to_book_store.book_store_name IS 'The book store name'/
COMMENT ON COLUMN t_book_to_book_store.book_id IS 'The book ID'/
COMMENT ON COLUMN t_book_to_book_store.stock IS 'The number of books on stock'/


CREATE TABLE t_arrays (
  id integer not null,
  string_array ARRAY,
  number_array ARRAY,
  date_array ARRAY,

  CONSTRAINT pk_t_arrays PRIMARY KEY (ID)
);
/

CREATE TABLE x_unused (
  ID INT NOT NULL,
  NAME VARCHAR(10) NOT NULL,
  BIG_INTEGER DECIMAL(25),
  ID_REF INT,
  NAME_REF VARCHAR(10),
  CLASS INT,
  FIELDS INT,
  TABLE INT,
  CONFIGURATION INT,
  U_D_T INT,
  META_DATA INT,
  VALUES INT,
  TYPE0 INT,
  PRIMARY_KEY INT,
  PRIMARYKEY INT,
  "FIELD 737" DECIMAL(25, 2),

  CONSTRAINT pk_x_unused PRIMARY KEY(ID, NAME),
  CONSTRAINT uk_x_unused_id UNIQUE(ID),
  CONSTRAINT fk_x_unused_self FOREIGN KEY(ID_REF, NAME_REF) REFERENCES X_UNUSED(ID, NAME)
);
/

CREATE TABLE t_exotic_types (
  ID INT NOT NULL,
  UU UUID,
  
  CONSTRAINT pk_t_exotic_types PRIMARY KEY(ID)
);
/

CREATE TABLE t_639_numbers_table (
  ID INT NOT NULL,
  BYTE TINYINT,
  SHORT SMALLINT,
  INTEGER INT,
  LONG BIGINT,
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

CREATE VIEW V_LIBRARY (AUTHOR, TITLE) AS
SELECT CONCAT(T_AUTHOR.FIRST_NAME, ' ', T_AUTHOR.LAST_NAME), T_BOOK.TITLE
FROM T_AUTHOR JOIN T_BOOK ON T_BOOK.AUTHOR_ID = T_AUTHOR.ID;
/

CREATE VIEW v_author AS
SELECT * FROM t_author
/

CREATE VIEW v_book AS
SELECT * FROM t_book
/

CREATE VIEW "Ää" AS
SELECT 1 AS "Öö"
/

CREATE VIEW "v.with.dots" ("id.with.dots") AS
SELECT 1 AS "id.dotted"
/

CREATE VIEW v_2603 AS
SELECT 1 AS col1, 2 AS col2, 3 AS col3, 4 AS col4
/

CREATE VIEW v_3176 AS
SELECT 1 AS col001, 1 AS col002, 1 AS col003, 1 AS col004, 1 AS col005, 1 AS col006, 1 AS col007, 1 AS col008, 1 AS col009, 1 AS col010,
       1 AS col011, 1 AS col012, 1 AS col013, 1 AS col014, 1 AS col015, 1 AS col016, 1 AS col017, 1 AS col018, 1 AS col019, 1 AS col020,
       1 AS col021, 1 AS col022, 1 AS col023, 1 AS col024, 1 AS col025, 1 AS col026, 1 AS col027, 1 AS col028, 1 AS col029, 1 AS col030,
       1 AS col031, 1 AS col032, 1 AS col033, 1 AS col034, 1 AS col035, 1 AS col036, 1 AS col037, 1 AS col038, 1 AS col039, 1 AS col040,
       1 AS col041, 1 AS col042, 1 AS col043, 1 AS col044, 1 AS col045, 1 AS col046, 1 AS col047, 1 AS col048, 1 AS col049, 1 AS col050,
       1 AS col051, 1 AS col052, 1 AS col053, 1 AS col054, 1 AS col055, 1 AS col056, 1 AS col057, 1 AS col058, 1 AS col059, 1 AS col060,
       1 AS col061, 1 AS col062, 1 AS col063, 1 AS col064, 1 AS col065, 1 AS col066, 1 AS col067, 1 AS col068, 1 AS col069, 1 AS col070,
       1 AS col071, 1 AS col072, 1 AS col073, 1 AS col074, 1 AS col075, 1 AS col076, 1 AS col077, 1 AS col078, 1 AS col079, 1 AS col080,
       1 AS col081, 1 AS col082, 1 AS col083, 1 AS col084, 1 AS col085, 1 AS col086, 1 AS col087, 1 AS col088, 1 AS col089, 1 AS col090,
       1 AS col091, 1 AS col092, 1 AS col093, 1 AS col094, 1 AS col095, 1 AS col096, 1 AS col097, 1 AS col098, 1 AS col099, 1 AS col100,
       1 AS col101, 1 AS col102, 1 AS col103, 1 AS col104, 1 AS col105, 1 AS col106, 1 AS col107, 1 AS col108, 1 AS col109, 1 AS col110,
       1 AS col111, 1 AS col112, 1 AS col113, 1 AS col114, 1 AS col115, 1 AS col116, 1 AS col117, 1 AS col118, 1 AS col119, 1 AS col120,
       1 AS col121, 1 AS col122, 1 AS col123, 1 AS col124, 1 AS col125, 1 AS col126, 1 AS col127, 1 AS col128, 1 AS col129, 1 AS col130,
       1 AS col131, 1 AS col132, 1 AS col133, 1 AS col134, 1 AS col135, 1 AS col136, 1 AS col137, 1 AS col138, 1 AS col139, 1 AS col140,
       1 AS col141, 1 AS col142, 1 AS col143, 1 AS col144, 1 AS col145, 1 AS col146, 1 AS col147, 1 AS col148, 1 AS col149, 1 AS col150,
       1 AS col151, 1 AS col152, 1 AS col153, 1 AS col154, 1 AS col155, 1 AS col156, 1 AS col157, 1 AS col158, 1 AS col159, 1 AS col160,
       1 AS col161, 1 AS col162, 1 AS col163, 1 AS col164, 1 AS col165, 1 AS col166, 1 AS col167, 1 AS col168, 1 AS col169, 1 AS col170,
       1 AS col171, 1 AS col172, 1 AS col173, 1 AS col174, 1 AS col175, 1 AS col176, 1 AS col177, 1 AS col178, 1 AS col179, 1 AS col180,
       1 AS col181, 1 AS col182, 1 AS col183, 1 AS col184, 1 AS col185, 1 AS col186, 1 AS col187, 1 AS col188, 1 AS col189, 1 AS col190,
       1 AS col191, 1 AS col192, 1 AS col193, 1 AS col194, 1 AS col195, 1 AS col196, 1 AS col197, 1 AS col198, 1 AS col199, 1 AS col200,
       1 AS col201, 1 AS col202, 1 AS col203, 1 AS col204, 1 AS col205, 1 AS col206, 1 AS col207, 1 AS col208, 1 AS col209, 1 AS col210,
       1 AS col211, 1 AS col212, 1 AS col213, 1 AS col214, 1 AS col215, 1 AS col216, 1 AS col217, 1 AS col218, 1 AS col219, 1 AS col220,
       1 AS col221, 1 AS col222, 1 AS col223, 1 AS col224, 1 AS col225, 1 AS col226, 1 AS col227, 1 AS col228, 1 AS col229, 1 AS col230,
       1 AS col231, 1 AS col232, 1 AS col233, 1 AS col234, 1 AS col235, 1 AS col236, 1 AS col237, 1 AS col238, 1 AS col239, 1 AS col240,
       1 AS col241, 1 AS col242, 1 AS col243, 1 AS col244, 1 AS col245, 1 AS col246, 1 AS col247, 1 AS col248, 1 AS col249, 1 AS col250,
       1 AS col251, 1 AS col252, 1 AS col253, 1 AS col254, 1 AS col255, 1 AS col256, 1 AS col257, 1 AS col258, 1 AS col259, 1 AS col260,
       1 AS col261, 1 AS col262, 1 AS col263, 1 AS col264, 1 AS col265, 1 AS col266, 1 AS col267, 1 AS col268, 1 AS col269, 1 AS col270,
       1 AS col271, 1 AS col272, 1 AS col273, 1 AS col274, 1 AS col275, 1 AS col276, 1 AS col277, 1 AS col278, 1 AS col279, 1 AS col280,
       1 AS col281, 1 AS col282, 1 AS col283, 1 AS col284, 1 AS col285, 1 AS col286, 1 AS col287, 1 AS col288, 1 AS col289, 1 AS col290,
       1 AS col291, 1 AS col292, 1 AS col293, 1 AS col294, 1 AS col295, 1 AS col296, 1 AS col297, 1 AS col298, 1 AS col299, 1 AS col300
/

CREATE ALIAS f_one FOR "org.jooq.test.utils.h2.F.fOne";/
CREATE ALIAS f_number FOR "org.jooq.test.utils.h2.F.fNumber";/
CREATE ALIAS f317 FOR "org.jooq.test.utils.h2.F.f317";/
CREATE ALIAS f1256 FOR "org.jooq.test.utils.h2.F.f1256";/
CREATE ALIAS f_arrays1 FOR "org.jooq.test.utils.h2.F.f_arrays1";/
CREATE ALIAS f_arrays2 FOR "org.jooq.test.utils.h2.F.f_arrays2";/
CREATE ALIAS f_arrays3 FOR "org.jooq.test.utils.h2.F.f_arrays3";/
CREATE ALIAS f_author_exists FOR "org.jooq.test.utils.h2.F.fAuthorExists";/
CREATE ALIAS f_get_one_cursor FOR "org.jooq.test.utils.h2.F.fGetOneCursor";/
CREATE ALIAS p_create_author_by_name FOR "org.jooq.test.utils.h2.F.pCreateAuthorByName";/
CREATE ALIAS p_create_author FOR "org.jooq.test.utils.h2.F.pCreateAuthor";/


DROP TABLE IF EXISTS accounts/
DROP TABLE IF EXISTS transactions/

CREATE TABLE accounts (
  id INT NOT NULL PRIMARY KEY,
  account_owner VARCHAR(20) NOT NULL,
  account_name VARCHAR(20) NOT NULL,
  amount DECIMAL(18, 2) NOT NULL
);

CREATE TABLE transactions (
  id INT NOT NULL PRIMARY KEY,
  account_id INT NOT NULL,
  amount DECIMAL(18, 2) NOT NULL
);