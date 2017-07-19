USE multi_database/
DROP TABLE t_4792/
DROP TABLE t_4795/

CREATE TABLE t_4792 (
  id INTEGER,
  v VARCHAR(50),

  CONSTRAINT pk_t_4792 PRIMARY KEY (id)
)/

CREATE TABLE t_4795 (
  id INTEGER,
  v VARCHAR(50),

  CONSTRAINT pk_t_4795 PRIMARY KEY (id)
)/

USE test/
DROP VIEW v_library/
DROP VIEW v_author/
DROP VIEW v_book/

DROP PROCEDURE p_many_parameters/
DROP PROCEDURE p_unused/
DROP PROCEDURE p_create_author/
DROP PROCEDURE p_create_author_by_name/
DROP PROCEDURE p_author_exists/
DROP PROCEDURE p391/
DROP PROCEDURE p_default/
DROP PROCEDURE p1490/
DROP PROCEDURE p_raise/
DROP PROCEDURE p_raise_3696/
DROP PROCEDURE p_results/
DROP PROCEDURE p_results_and_out_parameters/
DROP PROCEDURE p_results_and_row_counts/
DROP PROCEDURE p_books_and_authors/
DROP PROCEDURE p_dates/
DROP PROCEDURE p4106/
DROP PROCEDURE p5171/
DROP PROCEDURE p5171_count/
DROP FUNCTION f5171/
DROP FUNCTION f5171_count/
DROP FUNCTION f_cross_multiply/
DROP FUNCTION f_tables1/
DROP FUNCTION f_tables2/
DROP FUNCTION f_tables3/
DROP FUNCTION f_tables4/
DROP FUNCTION f_tables5/
DROP FUNCTION f_many_parameters/
DROP FUNCTION f_author_exists/
DROP FUNCTION f_one/
DROP FUNCTION f_number/
DROP FUNCTION f_unsigned/
DROP FUNCTION f317/

DROP TRIGGER t_triggers_trigger/

DROP TABLE multi_schema.t_book_sale/
DROP TABLE multi_schema.t_book/
DROP TABLE multi_schema.t_author/

DROP TABLE t_dates/
DROP TABLE t_triggers/
DROP TABLE t_book_to_book_store/
DROP TABLE t_book_store/
DROP TABLE t_book/
DROP TABLE t_book_details/
DROP TABLE t_author/
DROP TABLE t_user/
DROP TABLE t_language/
DROP TABLE x_test_case_2025/
DROP TABLE x_test_case_71/
DROP TABLE x_test_case_64_69/
DROP TABLE x_test_case_85/
DROP TABLE t_exotic_types/
DROP TABLE x_unused/
DROP TABLE t_639_numbers_table/
DROP TABLE t_725_lob_test/
DROP TABLE t_785/
DROP TABLE t_unsigned/
DROP TABLE t_booleans/
DROP TABLE t_identity/
DROP TABLE t_identity_pk/
DROP TABLE t_error_on_update/
DROP TABLE t_3084/
DROP TABLE t_3084_a/
DROP TABLE t_3084_two_unique_keys/
DROP TABLE t_3090_a/
DROP TABLE t_3090_b/
DROP TABLE t_3085/
DROP TABLE t_4795/
DROP TABLE t_5538_a/
DROP TABLE t_5538_b/

DROP TYPE u_date_table/
DROP TYPE u_number_long_table/
DROP TYPE u_number_table/
DROP TYPE u_string_table/
DROP TYPE u_book_table/

CREATE TYPE u_book_table AS TABLE (
  id INTEGER,
  title VARCHAR(400)
)
/

CREATE TYPE u_string_table      AS TABLE (column_value VARCHAR(20))/
CREATE TYPE u_number_table      AS TABLE (column_value INTEGER)/
CREATE TYPE u_number_long_table AS TABLE (column_value BIGINT)/
CREATE TYPE u_date_table        AS TABLE (column_value DATE)/

CREATE TABLE t_5538_a (
  id INTEGER
--CREATE UNIQUE INDEX uk_t5538 ON t_5538_a (id)
)/

CREATE TABLE t_5538_b (
  id INTEGER
--CREATE UNIQUE INDEX uk_t5538 ON t_5538_b (id)
)/

CREATE TABLE t_4795 (
  id INTEGER,
  v VARCHAR(50),

  CONSTRAINT pk_t_4795 PRIMARY KEY (id)
)/


CREATE TABLE t_error_on_update (
  id INTEGER
)
/

CREATE TRIGGER t_error_on_update_trigger ON t_error_on_update FOR UPDATE
AS
BEGIN
    IF (SELECT id FROM INSERTED) = 2
    BEGIN
        Raiserror('t_error_on_update_trigger 1',  9, -1)
        Raiserror('t_error_on_update_trigger 2', 10, -1)
    END
    ELSE
    BEGIN
        Raiserror('t_error_on_update_trigger 1',  9, -1)
        Raiserror('t_error_on_update_trigger 2', 10, -1)
        Raiserror('t_error_on_update_trigger 3', 11, -1)
        Raiserror('t_error_on_update_trigger 4', 12, -1)
        Raiserror('t_error_on_update_trigger 5', 13, -1)
    END
END
/

CREATE TABLE t_identity_pk (
  id INTEGER IDENTITY(1,1) NOT NULL,
  val int,

  CONSTRAINT pk_t_identity_pk PRIMARY KEY (id)
)
/

CREATE TABLE t_identity (
  id INTEGER IDENTITY(1,1) NOT NULL,
  val int
)
/


CREATE TABLE t_dates (
  id int,
  d date,
  t time,
  ts datetime,
  ts_tz datetimeoffset,
  d_int int,
  ts_bigint bigint,
  offset1 datetimeoffset,
  offset2 datetimeoffset,
  offset3 datetimeoffset,

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
  u_long decimal(20)
)
/

CREATE TABLE t_triggers (
  id_generated int IDENTITY(1,1) not null,
  id int,
  counter int,

  CONSTRAINT pk_t_triggers PRIMARY KEY (id_generated)
)
/

CREATE TRIGGER t_triggers_trigger
ON t_triggers
AFTER INSERT AS
    update t_triggers
    set id = id_generated,
        counter = id_generated * 2;
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
  LOB varbinary(max) NULL,

  CONSTRAINT pk_t_725_lob_test PRIMARY KEY (id)
)
/

CREATE TABLE t_785 (
  ID int,
  NAME varchar(50),
  VALUE varchar(50)
)
/

CREATE TABLE t_3084 (
  ID   INTEGER,
  data INTEGER
)
/

CREATE TABLE t_3084_a (
  ID   INTEGER,
  data INTEGER,

  CONSTRAINT uk_t_3084_a UNIQUE (ID)
)
/

CREATE TABLE t_3084_two_unique_keys (
  ID1   INTEGER NOT NULL,
  ID2   INTEGER     NULL,
  ID3   INTEGER NOT NULL,
  ID4   INTEGER     NULL,
  data  INTEGER
)
/

CREATE UNIQUE INDEX uk_t_3084 ON t_3084 (ID)/
CREATE UNIQUE INDEX uk_t_3084_a2 ON t_3084_a (ID)/
CREATE UNIQUE INDEX uk_t_3084_two_unique_keys_1 ON t_3084_two_unique_keys (ID1, ID2, ID3, ID4)/
CREATE UNIQUE INDEX uk_t_3084_two_unique_keys_2 ON t_3084_two_unique_keys (ID1, ID3, ID2, ID4)/


CREATE TABLE t_3090_a (
  id1  INTEGER NOT NULL,
  id2  INTEGER     NULL,
  data INTEGER     NULL,

  CONSTRAINT uk_t_3090_a UNIQUE (id1, id2)
)
/

CREATE TABLE t_3090_b (
  id1  INTEGER NOT NULL,
  id2  INTEGER     NULL,
  data INTEGER     NULL
)
/

CREATE UNIQUE INDEX uk_t_3090_b ON t_3090_b (id1, id2)/

CREATE TABLE t_3085 (
  c1 INT,
  c2 DATETIME2
)
/

CREATE TABLE t_author (
  ID int NOT NULL,
  FIRST_NAME VARCHAR(50),
  LAST_NAME VARCHAR(50) NOT NULL,
  DATE_OF_BIRTH DATE,
  YEAR_OF_BIRTH int,
  ADDRESS VARCHAR(50),

  CONSTRAINT pk_t_author PRIMARY KEY (ID)
)
/

CREATE INDEX i_author_name ON t_author (last_name, first_name)/
CREATE INDEX i_author_name_inverse ON t_author (first_name, last_name)/

CREATE TABLE t_user (
  ID int NOT NULL,
  FIRST_NAME VARCHAR(50),
  LAST_NAME VARCHAR(50) NOT NULL,

  CONSTRAINT pk_t_user PRIMARY KEY (ID)
)
/

CREATE INDEX i_author_name ON t_user (last_name, first_name)/
CREATE INDEX i_author_name_inverse ON t_user (first_name, last_name)/

CREATE TABLE t_book_details (
  ID int,

  CONSTRAINT pk_t_book_details PRIMARY KEY (ID)
)
/

CREATE TABLE t_book (
  ID int NOT NULL,
  AUTHOR_ID int NOT NULL,
  CO_AUTHOR_ID int,
  DETAILS_ID int,
  TITLE VARCHAR(400) NOT NULL DEFAULT 'no title',
  PUBLISHED_IN int NOT NULL,
  LANGUAGE_ID int NOT NULL DEFAULT 1,
  CONTENT_TEXT text,
  CONTENT_PDF varbinary(max),

  CONSTRAINT pk_t_book PRIMARY KEY (ID),
  CONSTRAINT fk_t_book_author_id FOREIGN KEY (AUTHOR_ID) REFERENCES T_AUTHOR(ID),
  CONSTRAINT fk_t_book_co_author_id FOREIGN KEY (CO_AUTHOR_ID) REFERENCES T_AUTHOR(ID),
  CONSTRAINT fk_t_book_details_id FOREIGN KEY (DETAILS_ID) REFERENCES T_BOOK_DETAILS(ID),
  CONSTRAINT fk_t_book_language_id FOREIGN KEY (LANGUAGE_ID) REFERENCES T_LANGUAGE(ID)
)
/


CREATE TABLE t_book_store (
  NAME VARCHAR(400) NOT NULL,

  CONSTRAINT uk_t_book_store_name PRIMARY KEY(name)
)
/


CREATE TABLE t_book_to_book_store (
  BOOK_STORE_NAME VARCHAR(400) NOT NULL,
  BOOK_ID INTEGER NOT NULL,
  STOCK INTEGER,

  CONSTRAINT pk_b2bs PRIMARY KEY(book_store_name, book_id),
  CONSTRAINT fk_b2bs_bs_name FOREIGN KEY (book_store_name)
                             REFERENCES t_book_store (name)
                             ON DELETE CASCADE,
  CONSTRAINT fk_b2bs_b_id    FOREIGN KEY (book_id)
                             REFERENCES t_book (id)
                             ON DELETE CASCADE
)
/

CREATE TABLE MULTI_SCHEMA.t_author (
  ID int NOT NULL,
  FIRST_NAME VARCHAR(50),
  LAST_NAME VARCHAR(50) NOT NULL,
  DATE_OF_BIRTH DATE,
  YEAR_OF_BIRTH int,
  ADDRESS VARCHAR(50),

  CONSTRAINT pk_t_author PRIMARY KEY (ID)
)
/


CREATE TABLE x_unused (
  id int NOT NULL,
  name VARCHAR(10) NOT NULL,
  BIG_INTEGER DECIMAL(25),
  id_ref int,
  CLASS int,
  FIELDS int,
  CONFIGURATION int,
  U_D_T int,
  META_DATA int,
  TYPE0 int,
  PRIMARY_KEY int,
  PRIMARYKEY int,
  name_ref VARCHAR(10),
  "FIELD 737" DECIMAL(25, 2),

  CONSTRAINT pk_x_unused PRIMARY KEY(ID, NAME),
  CONSTRAINT uk_x_unused_id UNIQUE(ID),
  CONSTRAINT fk_x_unused_self FOREIGN KEY(ID_REF, NAME_REF) REFERENCES X_UNUSED(ID, NAME)
)
/

CREATE TABLE t_exotic_types (
  ID INT NOT NULL,
  UU UNIQUEIDENTIFIER,

  UNTYPED_XML_AS_DOM XML,
  UNTYPED_XML_AS_JAXB XML,

  CONSTRAINT pk_t_exotic_types PRIMARY KEY(ID)
)
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
  "DOUBLE" FLOAT,

  CONSTRAINT pk_t_639_numbers_table PRIMARY KEY(ID)
)
/


CREATE TABLE x_test_case_64_69 (
  id int NOT NULL,
  unused_id int,

  CONSTRAINT pk_x_test_case_64_69 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_64_69a FOREIGN KEY(UNUSED_ID) REFERENCES X_UNUSED(ID),
  CONSTRAINT fk_x_test_case_64_69b FOREIGN KEY(UNUSED_ID) REFERENCES X_UNUSED(ID)
)
/

CREATE TABLE x_test_case_71 (
  id int NOT NULL,
  test_case_64_69_id int,

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

CREATE TABLE x_test_case_2025 (
  ref_id int NOT NULL,
  ref_name VARCHAR(10) NOT NULL,

  CONSTRAINT fk_x_test_case_2025_1 FOREIGN KEY(ref_id) REFERENCES x_test_case_85(ID),
  CONSTRAINT fk_x_test_case_2025_2 FOREIGN KEY(ref_id) REFERENCES x_test_case_71(ID),
  CONSTRAINT fk_x_test_case_2025_3 FOREIGN KEY(ref_id, ref_name) REFERENCES X_UNUSED(id, name)
)
/

CREATE VIEW v_library (author, title) AS
SELECT a.first_name + ' ' + a.last_name, b.title
FROM t_author a JOIN t_book b ON b.author_id = a.id
/

CREATE VIEW v_author AS
SELECT * FROM t_author
/

CREATE VIEW v_book AS
SELECT * FROM t_book
/


CREATE PROCEDURE p_unused (@in1 VARCHAR, @out1 int OUT, @out2 int OUT)
AS
;
/

CREATE PROCEDURE p_create_author_by_name (@first_name VARCHAR(50), @last_name VARCHAR(50))
AS
BEGIN
  INSERT INTO T_AUTHOR (ID, FIRST_NAME, LAST_NAME)
  VALUES ((SELECT MAX(ID)+1 FROM T_AUTHOR), @first_name, @last_name);
END
/

CREATE PROCEDURE p_create_author
AS
BEGIN
  EXEC p_create_author_by_name 'William', 'Shakespeare';
END
/

CREATE PROCEDURE p5171 (
    @books u_book_table READONLY,
    @strings u_string_table READONLY,
    @numbers u_number_table READONLY,
    @numbers_long u_number_long_table READONLY,
    @dates u_date_table READONLY
)
AS
BEGIN
    SELECT *
    FROM @books
    FULL OUTER JOIN @strings ON 1 = 1
    FULL OUTER JOIN @numbers ON 1 = 1
    FULL OUTER JOIN @numbers_long ON 1 = 1
    FULL OUTER JOIN @dates ON 1 = 1
    RETURN
END
/

CREATE PROCEDURE p5171_count (
    @books u_book_table READONLY,
    @strings u_string_table READONLY,
    @numbers u_number_table READONLY,
    @numbers_long u_number_long_table READONLY,
    @dates u_date_table READONLY,
    @result int OUT
)
AS
BEGIN
  SELECT @result = count(*)
  FROM @books
  FULL OUTER JOIN @strings ON 1 = 1
  FULL OUTER JOIN @numbers ON 1 = 1
  FULL OUTER JOIN @numbers_long ON 1 = 1
  FULL OUTER JOIN @dates ON 1 = 1;
END
/

CREATE FUNCTION f5171 (
    @books u_book_table READONLY,
    @strings u_string_table READONLY,
    @numbers u_number_table READONLY,
    @numbers_long u_number_long_table READONLY,
    @dates u_date_table READONLY
)
RETURNS @out_table TABLE (
    book_id INTEGER,
    book_title VARCHAR(400),
    strings_column_value VARCHAR(20),
    numbers_column_value INTEGER,
    number_longs_column_value BIGINT,
    dates_column_value DATE
)
AS
BEGIN
    INSERT @out_table
    SELECT *
    FROM @books
    FULL OUTER JOIN @strings ON 1 = 1
    FULL OUTER JOIN @numbers ON 1 = 1
    FULL OUTER JOIN @numbers_long ON 1 = 1
    FULL OUTER JOIN @dates ON 1 = 1
    RETURN
END
/

CREATE FUNCTION f5171_count (
    @books u_book_table READONLY,
    @strings u_string_table READONLY,
    @numbers u_number_table READONLY,
    @numbers_long u_number_long_table READONLY,
    @dates u_date_table READONLY
)
RETURNS INTEGER
AS
BEGIN
  DECLARE @result INT;

  SELECT @result = count(*)
  FROM @books
  FULL OUTER JOIN @strings ON 1 = 1
  FULL OUTER JOIN @numbers ON 1 = 1
  FULL OUTER JOIN @numbers_long ON 1 = 1
  FULL OUTER JOIN @dates ON 1 = 1;

  RETURN @result;
END
/

CREATE FUNCTION f_cross_multiply (
  @numbers u_number_table READONLY
)
RETURNS @result TABLE (
  i1 INTEGER,
  i2 INTEGER,
  product INTEGER
)
AS
BEGIN
  INSERT INTO @result
  SELECT n1.column_value, n2.column_value, n1.column_value * n2.column_value
  FROM @numbers n1
  CROSS JOIN @numbers n2

  RETURN
END
/

CREATE FUNCTION f_tables1 ()
RETURNS @out_table TABLE (
    column_value INTEGER
)
AS
BEGIN
    INSERT @out_table
    VALUES (1)
    RETURN
END
/

CREATE FUNCTION f_tables2 ()
RETURNS @out_table TABLE (
    column_value BIGINT
)
AS
BEGIN
    INSERT @out_table
    VALUES (1)
    RETURN
END
/

CREATE FUNCTION f_tables3 ()
RETURNS TABLE
AS RETURN
SELECT '1' column_value
/

CREATE FUNCTION f_tables4 (@id INTEGER)
RETURNS @out_table TABLE (
    id INTEGER,
    title VARCHAR(400)
)
AS
BEGIN
    INSERT @out_table
    SELECT id, title
    FROM t_book
    WHERE @id IS NULL OR id = @id
    ORDER BY id
    RETURN
END
/

CREATE FUNCTION f_tables5 (@v1 INTEGER, @v2 INTEGER, @v3 INTEGER)
RETURNS @out_table TABLE (
    v INTEGER,
    s INTEGER
)
AS
BEGIN
    INSERT @out_table
    VALUES(@v1, @v1),
          (@v2, @v1 + @v2),
          (@v3, @v1 + @v2 + @v3)
    RETURN
END
/

CREATE PROCEDURE p_many_parameters (
  @f000 int, @f001 int, @f002 int, @f003 int, @f004 int,
  @f005 int, @f006 int, @f007 int, @f008 int, @f009 int,
  @f010 int, @f011 int, @f012 int, @f013 int, @f014 int,
  @f015 int, @f016 int, @f017 int, @f018 int, @f019 int,
  @f020 int, @f021 int, @f022 int, @f023 int, @f024 int,
  @f025 int, @f026 int, @f027 int, @f028 int, @f029 int,
  @f030 int, @f031 int, @f032 int, @f033 int, @f034 int,
  @f035 int, @f036 int, @f037 int, @f038 int, @f039 int,
  @f040 int, @f041 int, @f042 int, @f043 int, @f044 int,
  @f045 int, @f046 int, @f047 int, @f048 int, @f049 int,
  @f050 int, @f051 int, @f052 int, @f053 int, @f054 int,
  @f055 int, @f056 int, @f057 int, @f058 int, @f059 int,
  @f060 int, @f061 int, @f062 int, @f063 int, @f064 int,
  @f065 int, @f066 int, @f067 int, @f068 int, @f069 int,
  @f070 int, @f071 int, @f072 int, @f073 int, @f074 int,
  @f075 int, @f076 int, @f077 int, @f078 int, @f079 int,
  @f080 int, @f081 int, @f082 int, @f083 int, @f084 int,
  @f085 int, @f086 int, @f087 int, @f088 int, @f089 int,
  @f090 int, @f091 int, @f092 int, @f093 int, @f094 int,
  @f095 int, @f096 int, @f097 int, @f098 int, @f099 int,

  @f100 int, @f101 int, @f102 int, @f103 int, @f104 int,
  @f105 int, @f106 int, @f107 int, @f108 int, @f109 int,
  @f110 int, @f111 int, @f112 int, @f113 int, @f114 int,
  @f115 int, @f116 int, @f117 int, @f118 int, @f119 int,
  @f120 int, @f121 int, @f122 int, @f123 int, @f124 int,
  @f125 int, @f126 int, @f127 int, @f128 int, @f129 int,
  @f130 int, @f131 int, @f132 int, @f133 int, @f134 int,
  @f135 int, @f136 int, @f137 int, @f138 int, @f139 int,
  @f140 int, @f141 int, @f142 int, @f143 int, @f144 int,
  @f145 int, @f146 int, @f147 int, @f148 int, @f149 int,
  @f150 int, @f151 int, @f152 int, @f153 int, @f154 int,
  @f155 int, @f156 int, @f157 int, @f158 int, @f159 int,
  @f160 int, @f161 int, @f162 int, @f163 int, @f164 int,
  @f165 int, @f166 int, @f167 int, @f168 int, @f169 int,
  @f170 int, @f171 int, @f172 int, @f173 int, @f174 int,
  @f175 int, @f176 int, @f177 int, @f178 int, @f179 int,
  @f180 int, @f181 int, @f182 int, @f183 int, @f184 int,
  @f185 int, @f186 int, @f187 int, @f188 int, @f189 int,
  @f190 int, @f191 int, @f192 int, @f193 int, @f194 int,
  @f195 int, @f196 int, @f197 int, @f198 int, @f199 int,

  @f200 int, @f201 int, @f202 int, @f203 int, @f204 int,
  @f205 int, @f206 int, @f207 int, @f208 int, @f209 int,
  @f210 int, @f211 int, @f212 int, @f213 int, @f214 int,
  @f215 int, @f216 int, @f217 int, @f218 int, @f219 int,
  @f220 int, @f221 int, @f222 int, @f223 int, @f224 int,
  @f225 int, @f226 int, @f227 int, @f228 int, @f229 int,
  @f230 int, @f231 int, @f232 int, @f233 int, @f234 int,
  @f235 int, @f236 int, @f237 int, @f238 int, @f239 int,
  @f240 int, @f241 int, @f242 int, @f243 int, @f244 int,
  @f245 int, @f246 int, @f247 int, @f248 int, @f249 int,
  @f250 int, @f251 int, @f252 int, @f253 int, @f254 int,
  @f255 int, @f256 int, @f257 int, @f258 int, @f259 int,
  @f260 int, @f261 int, @f262 int, @f263 int, @f264 int,
  @f265 int, @f266 int, @f267 int, @f268 int, @f269 int,
  @f270 int, @f271 int, @f272 int, @f273 int, @f274 int,
  @f275 int, @f276 int, @f277 int, @f278 int, @f279 int,
  @f280 int, @f281 int, @f282 int, @f283 int, @f284 int,
  @f285 int, @f286 int, @f287 int, @f288 int, @f289 int,
  @f290 int, @f291 int, @f292 int, @f293 int, @f294 int,
  @f295 int, @f296 int, @f297 int, @f298 int, @f299 int,

  @f300 int, @f301 int, @f302 int, @f303 int, @f304 int,
  @f305 int, @f306 int, @f307 int, @f308 int, @f309 int,
  @f310 int, @f311 int, @f312 int, @f313 int, @f314 int,
  @f315 int, @f316 int, @f317 int, @f318 int, @f319 int,
  @f320 int, @f321 int, @f322 int, @f323 int, @f324 int,
  @f325 int, @f326 int, @f327 int, @f328 int, @f329 int,
  @f330 int, @f331 int, @f332 int, @f333 int, @f334 int,
  @f335 int, @f336 int, @f337 int, @f338 int, @f339 int,
  @f340 int, @f341 int, @f342 int, @f343 int, @f344 int,
  @f345 int, @f346 int, @f347 int, @f348 int, @f349 int,
  @f350 int, @f351 int, @f352 int, @f353 int, @f354 int,
  @f355 int, @f356 int, @f357 int, @f358 int, @f359 int,
  @f360 int, @f361 int, @f362 int, @f363 int, @f364 int,
  @f365 int, @f366 int, @f367 int, @f368 int, @f369 int,
  @f370 int, @f371 int, @f372 int, @f373 int, @f374 int,
  @f375 int, @f376 int, @f377 int, @f378 int, @f379 int,
  @f380 int, @f381 int, @f382 int, @f383 int, @f384 int,
  @f385 int, @f386 int, @f387 int, @f388 int, @f389 int,
  @f390 int, @f391 int, @f392 int, @f393 int, @f394 int,
  @f395 int, @f396 int, @f397 int, @f398 int, @f399 int
)
AS
;
/

CREATE FUNCTION f_many_parameters (
  @f000 int, @f001 int, @f002 int, @f003 int, @f004 int,
  @f005 int, @f006 int, @f007 int, @f008 int, @f009 int,
  @f010 int, @f011 int, @f012 int, @f013 int, @f014 int,
  @f015 int, @f016 int, @f017 int, @f018 int, @f019 int,
  @f020 int, @f021 int, @f022 int, @f023 int, @f024 int,
  @f025 int, @f026 int, @f027 int, @f028 int, @f029 int,
  @f030 int, @f031 int, @f032 int, @f033 int, @f034 int,
  @f035 int, @f036 int, @f037 int, @f038 int, @f039 int,
  @f040 int, @f041 int, @f042 int, @f043 int, @f044 int,
  @f045 int, @f046 int, @f047 int, @f048 int, @f049 int,
  @f050 int, @f051 int, @f052 int, @f053 int, @f054 int,
  @f055 int, @f056 int, @f057 int, @f058 int, @f059 int,
  @f060 int, @f061 int, @f062 int, @f063 int, @f064 int,
  @f065 int, @f066 int, @f067 int, @f068 int, @f069 int,
  @f070 int, @f071 int, @f072 int, @f073 int, @f074 int,
  @f075 int, @f076 int, @f077 int, @f078 int, @f079 int,
  @f080 int, @f081 int, @f082 int, @f083 int, @f084 int,
  @f085 int, @f086 int, @f087 int, @f088 int, @f089 int,
  @f090 int, @f091 int, @f092 int, @f093 int, @f094 int,
  @f095 int, @f096 int, @f097 int, @f098 int, @f099 int,

  @f100 int, @f101 int, @f102 int, @f103 int, @f104 int,
  @f105 int, @f106 int, @f107 int, @f108 int, @f109 int,
  @f110 int, @f111 int, @f112 int, @f113 int, @f114 int,
  @f115 int, @f116 int, @f117 int, @f118 int, @f119 int,
  @f120 int, @f121 int, @f122 int, @f123 int, @f124 int,
  @f125 int, @f126 int, @f127 int, @f128 int, @f129 int,
  @f130 int, @f131 int, @f132 int, @f133 int, @f134 int,
  @f135 int, @f136 int, @f137 int, @f138 int, @f139 int,
  @f140 int, @f141 int, @f142 int, @f143 int, @f144 int,
  @f145 int, @f146 int, @f147 int, @f148 int, @f149 int,
  @f150 int, @f151 int, @f152 int, @f153 int, @f154 int,
  @f155 int, @f156 int, @f157 int, @f158 int, @f159 int,
  @f160 int, @f161 int, @f162 int, @f163 int, @f164 int,
  @f165 int, @f166 int, @f167 int, @f168 int, @f169 int,
  @f170 int, @f171 int, @f172 int, @f173 int, @f174 int,
  @f175 int, @f176 int, @f177 int, @f178 int, @f179 int,
  @f180 int, @f181 int, @f182 int, @f183 int, @f184 int,
  @f185 int, @f186 int, @f187 int, @f188 int, @f189 int,
  @f190 int, @f191 int, @f192 int, @f193 int, @f194 int,
  @f195 int, @f196 int, @f197 int, @f198 int, @f199 int,

  @f200 int, @f201 int, @f202 int, @f203 int, @f204 int,
  @f205 int, @f206 int, @f207 int, @f208 int, @f209 int,
  @f210 int, @f211 int, @f212 int, @f213 int, @f214 int,
  @f215 int, @f216 int, @f217 int, @f218 int, @f219 int,
  @f220 int, @f221 int, @f222 int, @f223 int, @f224 int,
  @f225 int, @f226 int, @f227 int, @f228 int, @f229 int,
  @f230 int, @f231 int, @f232 int, @f233 int, @f234 int,
  @f235 int, @f236 int, @f237 int, @f238 int, @f239 int,
  @f240 int, @f241 int, @f242 int, @f243 int, @f244 int,
  @f245 int, @f246 int, @f247 int, @f248 int, @f249 int,
  @f250 int, @f251 int, @f252 int, @f253 int, @f254 int,
  @f255 int, @f256 int, @f257 int, @f258 int, @f259 int,
  @f260 int, @f261 int, @f262 int, @f263 int, @f264 int,
  @f265 int, @f266 int, @f267 int, @f268 int, @f269 int,
  @f270 int, @f271 int, @f272 int, @f273 int, @f274 int,
  @f275 int, @f276 int, @f277 int, @f278 int, @f279 int,
  @f280 int, @f281 int, @f282 int, @f283 int, @f284 int,
  @f285 int, @f286 int, @f287 int, @f288 int, @f289 int,
  @f290 int, @f291 int, @f292 int, @f293 int, @f294 int,
  @f295 int, @f296 int, @f297 int, @f298 int, @f299 int,

  @f300 int, @f301 int, @f302 int, @f303 int, @f304 int,
  @f305 int, @f306 int, @f307 int, @f308 int, @f309 int,
  @f310 int, @f311 int, @f312 int, @f313 int, @f314 int,
  @f315 int, @f316 int, @f317 int, @f318 int, @f319 int,
  @f320 int, @f321 int, @f322 int, @f323 int, @f324 int,
  @f325 int, @f326 int, @f327 int, @f328 int, @f329 int,
  @f330 int, @f331 int, @f332 int, @f333 int, @f334 int,
  @f335 int, @f336 int, @f337 int, @f338 int, @f339 int,
  @f340 int, @f341 int, @f342 int, @f343 int, @f344 int,
  @f345 int, @f346 int, @f347 int, @f348 int, @f349 int,
  @f350 int, @f351 int, @f352 int, @f353 int, @f354 int,
  @f355 int, @f356 int, @f357 int, @f358 int, @f359 int,
  @f360 int, @f361 int, @f362 int, @f363 int, @f364 int,
  @f365 int, @f366 int, @f367 int, @f368 int, @f369 int,
  @f370 int, @f371 int, @f372 int, @f373 int, @f374 int,
  @f375 int, @f376 int, @f377 int, @f378 int, @f379 int,
  @f380 int, @f381 int, @f382 int, @f383 int, @f384 int,
  @f385 int, @f386 int, @f387 int, @f388 int, @f389 int,
  @f390 int, @f391 int, @f392 int, @f393 int, @f394 int,
  @f395 int, @f396 int, @f397 int, @f398 int, @f399 int
)
RETURNS int
AS
BEGIN
  RETURN 0;
END;
/

CREATE PROCEDURE p_author_exists (@author_name VARCHAR(50), @result int OUT)
AS
BEGIN
  SELECT @result = CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
    FROM t_author
   WHERE first_name LIKE @author_name
      OR last_name LIKE @author_name
END;
/

CREATE PROCEDURE p391 (
  @i1 int, @io1 int out, @o1 int out,
  @o2 int out, @io2 int out, @i2 int) AS
BEGIN
  SET @o1 = @io1;
  SET @io1 = @i1;

  SET @o2 = @io2;
  SET @io2 = @i2;
END;
/

CREATE PROCEDURE p_default (
  @p_in_number       INTEGER = 0,
  @p_out_number      INTEGER OUT,
  @p_in_varchar      VARCHAR(10) = '0',
  @p_out_varchar     VARCHAR(10) OUT,
  @p_in_date         DATE = '1981-07-10',
  @p_out_date        DATE OUT
) AS
BEGIN
  SET @p_out_number = @p_in_number;
  SET @p_out_varchar = @p_in_varchar;
  SET @p_out_date = @p_in_date;
END;
/


CREATE PROCEDURE p1490 (@value int) AS
BEGIN
  RETURN;
END;
/

CREATE PROCEDURE p_raise(@mode int)
AS
BEGIN
    IF @mode = 3
    BEGIN
        UPDATE t_author SET last_name = 'abc' WHERE id = 1000;

        RAISERROR('message 1', 16, 2, 3);
        RAISERROR('message 2', 16, 2, 3);

        UPDATE t_author SET last_name = 'abc' WHERE id = 1000;

        RAISERROR('message 3', 16, 2, 3);
        RAISERROR('message 4', 16, 2, 3);
    END
    ELSE IF @mode = 2
    BEGIN
        UPDATE t_author SET last_name = 'abc' WHERE id = 1000;

        RAISERROR('message 1', 16, 2, 3);
        RAISERROR('message 2', 16, 2, 3);
    END
    ELSE IF @mode = 1
    BEGIN
        RAISERROR('message 1', 16, 2, 3);
        RAISERROR('message 2', 16, 2, 3);
    END
    ELSE
        THROW 50000, 'message', 2;
END;
/

CREATE PROCEDURE p_raise_3696(@number int)
AS
BEGIN
    DECLARE @message VARCHAR(100) = '';

    WHILE @number > 0
    BEGIN
        SET @message = 'message ' + CAST(@number AS VARCHAR(5));
        SET @number = @number - 1;
        RAISERROR(@message, 16, 2, 3);
    END;
END;
/


CREATE PROCEDURE p_results(
  @p_result_sets INT
)
AS
BEGIN
  IF @p_result_sets = 1 BEGIN
    SELECT 1 a;
  END
  ELSE IF @p_result_sets = 2 BEGIN
    SELECT 1 a;
    SELECT 1 b UNION SELECT 2 b;
  END
  ELSE IF @p_result_sets = 3 BEGIN
    SELECT 1 a;
    SELECT 1 b UNION SELECT 2 b;
    SELECT 1 c UNION SELECT 2 c UNION SELECT 3 c;
  END;
END;
/

CREATE PROCEDURE p_results_and_out_parameters(
  @p_result_sets INT,
  @p_count INT OUT
)
AS
BEGIN
  IF @p_result_sets = 1 BEGIN
    SELECT 1 a;
  END
  ELSE IF @p_result_sets = 2 BEGIN
    SELECT 1 a;
    SELECT 1 b UNION SELECT 2 b;
  END
  ELSE IF @p_result_sets = 3 BEGIN
    SELECT 1 a;
    SELECT 1 b UNION SELECT 2 b;
    SELECT 1 c UNION SELECT 2 c UNION SELECT 3 c;
  END;

  SET @p_count = @p_result_sets;
END;
/

CREATE PROCEDURE p_results_and_row_counts(
  @p_result_sets INT
)
AS
BEGIN
  CREATE TABLE #t (v INT);

  IF @p_result_sets = 1 BEGIN
    SELECT 1 a;

    INSERT INTO #t VALUES (1);
  END
  ELSE IF @p_result_sets = 2 BEGIN
    SELECT 1 a;
    SELECT 1 b UNION SELECT 2 b;

    INSERT INTO #t VALUES (1), (2);
  END
  ELSE IF @p_result_sets = 3 BEGIN
    SELECT 1 a;
    SELECT 1 b UNION SELECT 2 b;
    SELECT 1 c UNION SELECT 2 c UNION SELECT 3 c;

    INSERT INTO #t VALUES (1), (2), (3);
  END;
END;
/

CREATE PROCEDURE p_books_and_authors(
  @p_author_search VARCHAR(50)
)
AS
BEGIN
  DECLARE @author_ids TABLE (id INT);

  INSERT INTO @author_ids
  SELECT id
  FROM t_author
  WHERE @p_author_search IS NULL
  OR first_name LIKE @p_author_search
  OR last_name LIKE @p_author_search

  SELECT *
  FROM t_author
  WHERE id IN (SELECT id FROM @author_ids);

  SELECT *
  FROM t_book
  WHERE author_id IN (SELECT id FROM @author_ids);
END;
/

CREATE PROCEDURE p_dates(
  @d date OUTPUT,
  @t time OUTPUT,
  @ts datetime OUTPUT,
  @t_tz time OUTPUT,
  @ts_tz datetimeoffset OUTPUT)
AS
BEGIN
  SET @d = @d;
  SET @t = @t;
  SET @ts = @ts;
  SET @t_tz = @t_tz;
  SET @ts_tz = @ts_tz;
END;
/

CREATE PROCEDURE p4106 (
    @param1 INT,
    @param2 INT OUTPUT
) AS BEGIN
   SET @param2 = @param1

   IF @param1 = 5
       RETURN 42
END;
/

CREATE FUNCTION f_author_exists (@author_name VARCHAR(50))
RETURNS int
AS
BEGIN
  DECLARE @result int;

  SELECT @result = CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
    FROM t_author
   WHERE first_name LIKE @author_name
      OR last_name LIKE @author_name;

  RETURN @result;
END;
/

CREATE FUNCTION f_one()
RETURNS int
AS
BEGIN
  RETURN 1;
END;
/

CREATE FUNCTION f_number(@n int)
RETURNS int
AS
BEGIN
  RETURN @n;
END;
/

CREATE FUNCTION f_unsigned(@t tinyint, @s smallint, @i int, @b bigint)
RETURNS tinyint
AS
BEGIN
  RETURN @t;
END;
/

CREATE FUNCTION f317 (@p1 int, @p2 int, @p3 int, @p4 int) RETURNS int AS
BEGIN
  return 1000 * @p1 + 100 * @p2 + @p4;
END;
/
