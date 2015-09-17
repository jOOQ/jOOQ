DROP MATERIALIZED VIEW m_library/
DROP VIEW v_library/
DROP VIEW v_author/
DROP VIEW v_book/
DROP VIEW v_incomplete/

DROP SEQUENCE s_961_byte/
DROP SEQUENCE s_961_short/
DROP SEQUENCE s_961_int/
DROP SEQUENCE s_961_long/
DROP SEQUENCE s_961_big_integer/
DROP SEQUENCE s_triggers_sequence/
DROP SEQUENCE s_3899_$/
DROP SEQUENCE s_3899$/
DROP TRIGGER t_triggers_trigger/

DROP TABLE multi_schema.t_book_sale/
DROP TABLE multi_schema.t_book/
DROP TABLE multi_schema.t_author/

DROP TABLE t_reserved/
DROP TABLE t_dates/
DROP TABLE t_triggers/
DROP TABLE t_arrays/
DROP TABLE t_book_to_book_store/
DROP TABLE t_book_store/
DROP TABLE t_book/
DROP TABLE t_book_details/
DROP TABLE t_author/
DROP TABLE t_language/
DROP TABLE t_directory/
DROP TABLE x_test_case_2025/
DROP TABLE x_test_case_71/
DROP TABLE x_test_case_64_69/
DROP TABLE x_test_case_85/
DROP TABLE x_unused/
DROP TABLE x_many_fields/
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
DROP TABLE "T_2845_CASE_sensitivity"/
DROP TABLE t_unsigned/
DROP TABLE t_booleans/
DROP TABLE t_temp/
DROP TABLE t_2155/
DROP TABLE t_3711/

DROP TABLE multi_schema_unused.x_unused/

DROP PROCEDURE p_nested/
DROP PROCEDURE p_arrays1/
DROP PROCEDURE p_arrays2/
DROP PROCEDURE p_arrays3/
DROP PROCEDURE p_arrays4/
DROP PROCEDURE p_tables1/
DROP PROCEDURE p_tables2/
DROP PROCEDURE p_tables3/
DROP PROCEDURE p_tables4/
DROP PROCEDURE p_many_parameters/
DROP PROCEDURE p_default/
DROP FUNCTION f_arrays1/
DROP FUNCTION f_arrays2/
DROP FUNCTION f_arrays3/
DROP FUNCTION f_arrays4/
DROP FUNCTION f_tables1/
DROP FUNCTION f_tables2/
DROP FUNCTION f_tables3/
DROP FUNCTION f_tables4/
DROP FUNCTION f_pipelined_array1/
DROP FUNCTION f_pipelined_array4/
DROP FUNCTION f_pipelined_table1/
DROP FUNCTION f_pipelined_table4/
DROP PROCEDURE p_enhance_address1/
DROP PROCEDURE p_enhance_address2/
DROP PROCEDURE p_enhance_address3/
DROP PROCEDURE p3005/
DROP PROCEDURE multi_schema.p4311/
DROP PROCEDURE p_unused/
DROP PROCEDURE p_create_author/
DROP PROCEDURE p_create_author_by_name/
DROP PROCEDURE p_author_exists/
DROP PROCEDURE p391/
DROP PROCEDURE p1490/
DROP PROCEDURE p2155/
DROP FUNCTION f2155/
DROP FUNCTION f_author_exists/
DROP FUNCTION f_one/
DROP FUNCTION f_number/
DROP FUNCTION f317/
DROP FUNCTION f378/
DROP FUNCTION SECOND_MAX/
DROP PROCEDURE p_get_two_cursors/
DROP PROCEDURE p_get_one_cursor/
DROP FUNCTION f_get_one_cursor/
DROP FUNCTION f691cursor_out/
DROP FUNCTION f691cursor_in/
DROP PACKAGE library/
DROP PACKAGE pls_objects/

DROP TYPE multi_schema.u_4347/
DROP TYPE multi_schema.u_4347_table/

DROP TYPE u_3082_3/
DROP TYPE u_3082_2/
DROP TYPE u_3082_1/

DROP TYPE u_nested_3/
DROP TYPE u_nested_2/
DROP TYPE u_nested_1/
DROP TYPE u_address_table/
DROP TYPE u_address_type/
DROP TYPE u_street_type/
DROP TYPE u_string_array/
DROP TYPE u_number_array/
DROP TYPE u_number_long_array/
DROP TYPE u_date_array/
DROP TYPE u_string_table/
DROP TYPE u_number_table/
DROP TYPE u_number_long_table/
DROP TYPE u_date_table/
DROP TYPE o_invalid_type/
DROP TYPE u_invalid_table/
DROP TYPE u_invalid_type/

DROP TYPE u_author_type/
DROP TYPE u_book_table/
DROP TYPE u_book_array/
DROP TYPE u_book_type/
DROP TYPE u_second_max/
DROP TYPE u_3005/
DROP TYPE multi_schema.u_4311/
DROP TYPE u_2155_object/
DROP TYPE u_2155_array/
DROP TYPE u_3709/


DROP PUBLIC DATABASE LINK public_link/
DROP DATABASE LINK local_link/

CREATE PUBLIC DATABASE LINK public_link CONNECT TO MULTI_SCHEMA IDENTIFIED BY MULTI_SCHEMA USING 'XE'/
CREATE DATABASE LINK local_link CONNECT TO MULTI_SCHEMA IDENTIFIED BY MULTI_SCHEMA USING 'XE'/


CREATE TYPE u_3709 AS OBJECT (
  v VARCHAR2(4000),

  --Reactivate this when we fix this
  --MEMBER FUNCTION value RETURN VARCHAR2,
  MEMBER FUNCTION value (value VARCHAR2) RETURN VARCHAR2
)
/

CREATE TYPE u_2155_array AS VARRAY(10) OF DATE
/

CREATE TYPE u_2155_object AS OBJECT (
  D DATE,
  A u_2155_array
)
/

CREATE TYPE u_3005 AS OBJECT (
  ID NUMBER(7)
)
/

CREATE TYPE multi_schema.u_4311 AS OBJECT (
  ID NUMBER(7)
)
/

CREATE TYPE U_SECOND_MAX AS OBJECT
(
  MAX NUMBER, -- highest value seen so far
  SECMAX NUMBER, -- second highest value seen so far
  STATIC FUNCTION ODCIAggregateInitialize(sctx IN OUT U_SECOND_MAX) RETURN NUMBER,
  MEMBER FUNCTION ODCIAggregateIterate(self IN OUT U_SECOND_MAX, value IN NUMBER) RETURN NUMBER,
  MEMBER FUNCTION ODCIAggregateTerminate(self IN U_SECOND_MAX, returnValue OUT NUMBER, flags IN NUMBER) RETURN NUMBER,
  MEMBER FUNCTION ODCIAggregateMerge(self IN OUT U_SECOND_MAX, ctx2 IN U_SECOND_MAX) RETURN NUMBER
)
/

CREATE OR REPLACE TYPE BODY U_SECOND_MAX IS
STATIC FUNCTION ODCIAggregateInitialize(sctx IN OUT U_SECOND_MAX)
RETURN NUMBER IS
BEGIN
  SCTX := U_SECOND_MAX(0, 0);
  RETURN ODCIConst.Success;
END;

MEMBER FUNCTION ODCIAggregateIterate(self IN OUT U_SECOND_MAX, value IN NUMBER) RETURN NUMBER IS
BEGIN
  IF VALUE > SELF.MAX THEN
    SELF.SECMAX := SELF.MAX;
    SELF.MAX := VALUE;
  ELSIF VALUE > SELF.SECMAX THEN
    SELF.SECMAX := VALUE;
  END IF;
  RETURN ODCIConst.Success;
END;

MEMBER FUNCTION ODCIAggregateTerminate(self IN U_SECOND_MAX, returnValue OUT NUMBER, flags IN NUMBER) RETURN NUMBER IS
BEGIN
  RETURNVALUE := SELF.SECMAX;
  RETURN ODCIConst.Success;
END;

MEMBER FUNCTION ODCIAggregateMerge(self IN OUT U_SECOND_MAX, ctx2 IN U_SECOND_MAX) RETURN NUMBER IS
BEGIN
  IF CTX2.MAX > SELF.MAX THEN
    IF CTX2.SECMAX > SELF.SECMAX THEN
      SELF.SECMAX := CTX2.SECMAX;
    ELSE
      SELF.SECMAX := SELF.MAX;
    END IF;
    SELF.MAX := CTX2.MAX;
  ELSIF CTX2.MAX > SELF.SECMAX THEN
    SELF.SECMAX := CTX2.MAX;
  END IF;
  RETURN ODCIConst.Success;
END;
END;
/

CREATE FUNCTION SECOND_MAX (input NUMBER) RETURN NUMBER
PARALLEL_ENABLE AGGREGATE USING U_SECOND_MAX;
/

CREATE TYPE u_book_type AS OBJECT (
  id number(7),
  title varchar2(400)
)
/

CREATE TYPE u_book_table AS TABLE OF u_book_type/
CREATE TYPE u_book_array AS VARRAY(4) OF u_book_type/

CREATE OR REPLACE TYPE u_author_type AS OBJECT (
  id number(7),
  first_name varchar2(50),
  last_name varchar2(50),

  member procedure load,
  member procedure get_books (
  	book1 OUT u_book_type,
  	book2 OUT u_book_type,
  	books OUT u_book_table),

  member function count_books return number,
  static procedure new_author (
    p_id number,
	p_first_name varchar2,
	p_last_name varchar2,
	p_author OUT u_author_type),

  static function get_author (p_id number) return u_author_type
)
/

CREATE OR REPLACE TYPE BODY u_author_type AS
  member procedure load is
  	x number(7);
  begin
  	x := id;

    if x is not null then
      select a.first_name, a.last_name
	  into first_name, last_name
      from t_author a
      where a.id = x;
    end if;
  end load;

  member procedure get_books (
    book1 OUT u_book_type,
    book2 OUT u_book_type,
  	books OUT u_book_table) is

    x number(7);
    b1 u_book_type := u_book_type(null, null);
    b2 u_book_type := u_book_type(null, null);
    bs u_book_table;
  begin
    x := id;

    -- execute a load to check whether the author is also reloaded
    self.load;

    if x is not null then
      begin
        select b.id, b.title
        into b1.id, b1.title
        from (
          select b.id, b.title, rownum r
          from t_book b
          where b.author_id = x
          order by b.id
        ) b
        where b.r = 1;
      exception 
        when others then null;
      end;

      begin
        select b.id, b.title
        into b2.id, b2.title
        from (
          select b.id, b.title, rownum r
          from t_book b
          where b.author_id = x
          order by b.id
        ) b
        where b.r = 2;
      exception 
        when others then null;
      end;
    end if;

	select u_book_type(b.id, b.title)
	bulk collect into bs
	from t_book b
    where b.author_id = x;

    book1 := b1;
    book2 := b2;
    books := bs;
  end get_books;

  member function count_books return number is
    x number(7);
    r number(7);
  begin
    x := id;

    select count(*)
    into r
    from t_book
    where author_id = x;

    return r;
  end count_books;

  static procedure new_author (
    p_id number,
	p_first_name varchar2,
	p_last_name varchar2,
	p_author OUT u_author_type) is
    result u_author_type;
  begin
  	insert into t_author (id, first_name, last_name)
	values (p_id, p_first_name, p_last_name);

	p_author := get_author(p_id);
  end new_author;

  static function get_author (p_id number) return u_author_type is
  	result u_author_type;
  begin
  	result := u_author_type(p_id, null, null);
	result.load;
	return result;
  end get_author;

end;
/

CREATE TYPE u_invalid_type AS invalid/
CREATE TYPE u_invalid_table AS TABLE OF u_invalid_type/
CREATE TYPE o_invalid_type AS OBJECT (
    invalid u_invalid_type
)/
CREATE TYPE u_string_array AS VARRAY(4) OF VARCHAR2(20)/
CREATE TYPE u_number_array AS VARRAY(4) OF NUMBER(7)/
CREATE TYPE u_number_long_array AS VARRAY(4) OF NUMBER(11)/
CREATE TYPE u_date_array AS VARRAY(4) OF DATE/

CREATE TYPE u_string_table AS TABLE OF VARCHAR2(20)/
CREATE TYPE u_number_table AS TABLE OF NUMBER(7)/
CREATE TYPE u_number_long_table AS TABLE OF NUMBER(11)/
CREATE TYPE u_date_table AS TABLE OF DATE/

CREATE TYPE u_nested_1 AS OBJECT (
  ID NUMBER(7),
  NESTED u_number_table
)/

CREATE TYPE u_nested_2 AS TABLE OF u_nested_1/
CREATE TYPE u_nested_3 AS OBJECT (
  ID NUMBER(7),
  NESTED u_nested_2
)/


CREATE TYPE u_3082_1 AS OBJECT (
  n NUMBER,
  numbers u_number_table
)
/

CREATE TYPE u_3082_2 AS TABLE OF u_3082_1
/

CREATE TYPE u_3082_3 AS OBJECT (
  o u_3082_1,
  t u_3082_2,
  numbers u_number_table
)
/

CREATE OR REPLACE PROCEDURE p_nested (
  p1 IN u_nested_3,
  p2 IN u_nested_3,
  p3 OUT u_nested_3,
  p4 OUT u_nested_3
) IS
BEGIN
  p3 := p1;
  p4 := p2;
END;/

CREATE TYPE u_street_type AS OBJECT (
  street VARCHAR2(100),
  no VARCHAR2(30),
  floors u_number_array,
  f_1323 blob,
  f_1326 clob
)
/

CREATE TYPE u_address_type AS OBJECT (
  street u_street_type,
  zip VARCHAR2(50),
  city VARCHAR2(50),
  country VARCHAR2(50),
  since DATE,
  code NUMBER(7),
  f_1323 blob,
  f_1326 clob
)
/

CREATE SEQUENCE s_961_byte MAXVALUE 100/
CREATE SEQUENCE s_961_short MAXVALUE 10000/
CREATE SEQUENCE s_961_int MAXVALUE 2000000000/
CREATE SEQUENCE s_961_long MAXVALUE 10000000000/
CREATE SEQUENCE s_961_big_integer/
CREATE SEQUENCE s_triggers_sequence/
CREATE SEQUENCE s_3899_$/
CREATE SEQUENCE s_3899$/

CREATE TABLE t_dates (
  id number(7),
  d date,
  t timestamp,
  ts timestamp,
  d_int number(7),
  ts_bigint number(18),
  i_y interval year to month,
  i_d interval day to second,

  CONSTRAINT pk_t_dates PRIMARY KEY (id)
)
/

CREATE TABLE t_2155 (
  id number(7),
  d1 DATE,
  d2 u_2155_object,
  d3 u_2155_array,

  CONSTRAINT pk_t_2155 PRIMARY KEY (id)
)
/

CREATE GLOBAL TEMPORARY TABLE t_temp (
  id number(7),

  CONSTRAINT pk_t_temp PRIMARY KEY (id)
)
/

CREATE TABLE t_booleans (
  id number(7),
  one_zero number(7),
  true_false_lc varchar2(5),
  true_false_uc varchar2(5),
  yes_no_lc varchar2(3),
  yes_no_uc varchar2(3),
  y_n_lc char(1),
  y_n_uc char(1),
  vc_boolean varchar2(1),
  c_boolean char(1),
  n_boolean number(7),

  CONSTRAINT pk_t_booleans PRIMARY KEY (id)
)
/

CREATE TABLE t_unsigned (
  u_byte number(3),
  u_short number(5),
  u_int number(10),
  u_long number(20)
)
/

CREATE TABLE t_triggers (
  id_generated number(7) not null,
  id number(7) not null,
  counter number(7) not null,

  CONSTRAINT pk_t_triggers PRIMARY KEY (id_generated)
)
/

CREATE OR REPLACE TRIGGER t_triggers_trigger
BEFORE INSERT
ON t_triggers
REFERENCING NEW AS new
FOR EACH ROW
BEGIN
	select s_triggers_sequence.nextval
	  into :new.id_generated
	  from dual;

	:new.id := :new.id_generated;
	:new.counter := :new.id_generated * 2;
END t_triggers_trigger;
/

CREATE TABLE t_directory (
  id           number(7) NOT NULL,
  parent_id    number(7),
  is_directory number(7),
  "name"       varchar2(50),

  CONSTRAINT pk_t_directory PRIMARY KEY (ID),
  CONSTRAINT pk_t_directory_self FOREIGN KEY (PARENT_ID) REFERENCES t_directory(ID) ON DELETE CASCADE
)
/

CREATE TABLE t_language (
  cd CHAR(2) NOT NULL,
  description VARCHAR2(50),
  description_english VARCHAR2(50),
  id NUMBER(7) NOT NULL,

  CONSTRAINT pk_t_language PRIMARY KEY (ID)
)
/
BEGIN
  EXECUTE IMMEDIATE 'COMMENT ON TABLE t_language IS ''An entity holding language master data

Oh oh. Newline character.

"Quotes". And \Escaping\That\Shouldn''''t\Be\Escaping

Oh, and beware of end-of-javadoc *' || CHR(47) || '''';
END;
/
COMMENT ON COLUMN t_language.id IS 'The language ID'/
COMMENT ON COLUMN t_language.cd IS 'The language ISO code'/
COMMENT ON COLUMN t_language.description IS 'The language description'/


CREATE TABLE t_725_lob_test (
  ID NUMBER(7) NOT NULL,
  LOB BLOB NULL,

  USER_JAVA_SQL_CLOB CLOB NULL,
  USER_JAVA_SQL_BLOB BLOB NULL,
  JOOQ_JAVA_SQL_CLOB CLOB NULL,
  JOOQ_JAVA_SQL_BLOB BLOB NULL,

  CONSTRAINT pk_t_725_lob_test PRIMARY KEY (id)
)
/

CREATE TABLE t_785 (
  ID NUMBER(7),
  NAME varchar2(50),
  VALUE varchar2(50)
)
/

CREATE TABLE "T_2845_CASE_sensitivity" (
  id int,

  insensitive int,
  "UPPER" int,
  "lower" int,
  "Mixed" int,

  CONSTRAINT pk_t_2845_case_sensitivity PRIMARY KEY (id)
)
/

CREATE TABLE t_author (
  id NUMBER(7) NOT NULL,
  first_name VARCHAR2(50),
  last_name VARCHAR2(50) NOT NULL,
  date_of_birth DATE,
  year_of_birth NUMBER(7),
  address u_address_type,

  CONSTRAINT pk_t_author PRIMARY KEY (ID)
)
/
COMMENT ON TABLE t_author IS 'An entity holding authors of books'/
COMMENT ON COLUMN t_author.id IS 'The author ID'/
COMMENT ON COLUMN t_author.first_name IS 'The author''s first name'/
COMMENT ON COLUMN t_author.last_name IS 'The author''s last name'/
COMMENT ON COLUMN t_author.date_of_birth IS 'The author''s date of birth'/
COMMENT ON COLUMN t_author.year_of_birth IS 'The author''s year of birth'/
COMMENT ON COLUMN t_author.address IS 'The author''s address'/

CREATE TABLE t_book_details (
  ID NUMBER(7),

  CONSTRAINT pk_t_book_details PRIMARY KEY (ID)
)
/
COMMENT ON TABLE t_book_details IS 'An unused details table'
/

CREATE TABLE t_book (
  id NUMBER(7) NOT NULL,
  author_id NUMBER(7) NOT NULL,
  co_author_id NUMBER(7),
  details_id NUMBER(7),
  title VARCHAR2(400) NOT NULL,
  published_in NUMBER(7) NOT NULL,
  language_id NUMBER(7) DEFAULT 1 NOT NULL,
  content_text CLOB,
  content_pdf BLOB,

  rec_version INT,
  rec_timestamp TIMESTAMP,

  CONSTRAINT pk_t_book PRIMARY KEY (ID),
  CONSTRAINT fk_t_book_author_id FOREIGN KEY (AUTHOR_ID) REFERENCES T_AUTHOR(ID) ON DELETE CASCADE,
  CONSTRAINT fk_t_book_co_author_id FOREIGN KEY (CO_AUTHOR_ID) REFERENCES T_AUTHOR(ID) ON DELETE CASCADE,
  CONSTRAINT fk_t_book_details_id FOREIGN KEY (DETAILS_ID) REFERENCES T_BOOK_DETAILS(ID) ON DELETE CASCADE,
  CONSTRAINT fk_t_book_language_id FOREIGN KEY (LANGUAGE_ID) REFERENCES T_LANGUAGE(ID) ON DELETE CASCADE
)
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
  name VARCHAR2(400) NOT NULL,

  CONSTRAINT uk_t_book_store_name PRIMARY KEY(name)
)
/
COMMENT ON TABLE t_book_store IS 'A book store'
/
COMMENT ON COLUMN t_book_store.name IS 'The books store name'
/


CREATE TABLE t_book_to_book_store (
  book_store_name VARCHAR2(400) NOT NULL,
  book_id NUMBER(7) NOT NULL,
  stock NUMBER(7),

  CONSTRAINT pk_b2bs PRIMARY KEY(book_store_name, book_id),
  CONSTRAINT fk_b2bs_bs_name FOREIGN KEY (book_store_name)
                             REFERENCES t_book_store (name)
                             ON DELETE CASCADE,
  CONSTRAINT fk_b2bs_b_id    FOREIGN KEY (book_id)
                             REFERENCES t_book (id)
                             ON DELETE CASCADE
)
/
COMMENT ON TABLE t_book_to_book_store IS 'An m:n relation between books and book stores'/
COMMENT ON COLUMN t_book_to_book_store.book_store_name IS 'The book store name'/
COMMENT ON COLUMN t_book_to_book_store.book_id IS 'The book ID'/
COMMENT ON COLUMN t_book_to_book_store.stock IS 'The number of books on stock'/

GRANT ALL ON T_BOOK_TO_BOOK_STORE TO MULTI_SCHEMA/
GRANT ALL ON T_BOOK_DETAILS TO MULTI_SCHEMA/
GRANT ALL ON T_LANGUAGE TO MULTI_SCHEMA/

CREATE TABLE MULTI_SCHEMA.T_BOOK_SALE (
  ID NUMBER(7) NOT NULL,
  BOOK_ID NUMBER(7) NOT NULL,
  BOOK_STORE_NAME VARCHAR2(400) NOT NULL,
  SOLD_AT DATE NOT NULL,
  SOLD_FOR NUMBER(10, 2) NOT NULL,

  CONSTRAINT pk_t_book_sale PRIMARY KEY (ID),
  CONSTRAINT fk_t_book_to_book_store FOREIGN KEY (BOOK_ID, BOOK_STORE_NAME) REFERENCES TEST.T_BOOK_TO_BOOK_STORE(BOOK_ID, BOOK_STORE_NAME) ON DELETE CASCADE
)
/

CREATE TABLE MULTI_SCHEMA.t_author (
  id NUMBER(7) NOT NULL,
  first_name VARCHAR2(50),
  last_name VARCHAR2(50) NOT NULL,
  date_of_birth DATE,
  year_of_birth NUMBER(7),
  address test.u_address_type,

  CONSTRAINT pk_t_author PRIMARY KEY (ID)
)
/

CREATE TABLE MULTI_SCHEMA.t_book (
  id NUMBER(7) NOT NULL,
  author_id NUMBER(7) NOT NULL,
  co_author_id NUMBER(7),
  details_id NUMBER(7),
  title VARCHAR2(400) NOT NULL,
  published_in NUMBER(7) NOT NULL,
  language_id NUMBER(7) DEFAULT 1 NOT NULL,
  content_text CLOB,
  content_pdf BLOB,

  CONSTRAINT pk_t_book PRIMARY KEY (ID),
  CONSTRAINT fk_t_book_author_id FOREIGN KEY (AUTHOR_ID) REFERENCES T_AUTHOR(ID) ON DELETE CASCADE,
  CONSTRAINT fk_t_book_co_author_id FOREIGN KEY (CO_AUTHOR_ID) REFERENCES T_AUTHOR(ID) ON DELETE CASCADE,
  CONSTRAINT fk_t_book_details_id FOREIGN KEY (DETAILS_ID) REFERENCES TEST.T_BOOK_DETAILS(ID) ON DELETE CASCADE,
  CONSTRAINT fk_t_book_language_id FOREIGN KEY (LANGUAGE_ID) REFERENCES TEST.T_LANGUAGE(ID) ON DELETE CASCADE
)
/

CREATE TABLE t_arrays (
  id NUMBER(7) not null,
  string_array u_string_array,
  number_array u_number_array,
  number_long_array u_number_long_array,
  date_array u_date_array,

  CONSTRAINT pk_t_arrays PRIMARY KEY (ID)
)
/

CREATE TABLE multi_schema_unused.x_unused (
  id NUMBER(7) NOT NULL,
  name VARCHAR2(10) NOT NULL,

  CONSTRAINT pk_x_unused PRIMARY KEY(ID, NAME)
)
/

GRANT ALL ON MULTI_SCHEMA_UNUSED.X_UNUSED TO TEST/

CREATE TABLE x_unused (
  id NUMBER(7) NOT NULL,
  name VARCHAR2(10) NOT NULL,
  big_integer NUMBER(38),
  id_ref NUMBER(7),
  CLASS NUMBER(7),
  FIELDS NUMBER(7),
  CONFIGURATION NUMBER(7),
  U_D_T NUMBER(7),
  META_DATA NUMBER(7),
  TYPE0 NUMBER(7),
  PRIMARY_KEY NUMBER(7),
  PRIMARYKEY NUMBER(7),
  name_ref VARCHAR2(10),
  "FIELD 737" NUMBER(25, 2),
  MS_UNUSED_ID_REF NUMBER(7),
  MS_UNUSED_NAME_REF VARCHAR2(10),

  CONSTRAINT pk_x_unused PRIMARY KEY(ID, NAME),
  CONSTRAINT uk_x_unused_id UNIQUE(ID),
  CONSTRAINT fk_x_unused_self
    FOREIGN KEY(ID_REF, NAME_REF)
    REFERENCES X_UNUSED(ID, NAME),
  CONSTRAINT fk_ms_unused_x_unused
    FOREIGN KEY(MS_UNUSED_ID_REF, MS_UNUSED_NAME_REF)
    REFERENCES MULTI_SCHEMA_UNUSED.X_UNUSED(ID, NAME)
)
/
COMMENT ON TABLE x_unused IS 'An unused table in the same schema.

"Its comments contain special characters"'
/
COMMENT ON COLUMN x_unused.id IS 'An unused column of an unused table in the same schema.

"Its comments contain special characters"'
/

CREATE TABLE t_exotic_types (
  ID NUMBER(7) NOT NULL,
  UU CHAR(36),

  JAVA_IO_SERIALIZABLE BLOB,

  PLAIN_SQL_CONVERTER_XML CLOB,
  PLAIN_SQL_BINDING_XML CLOB,

  ORACLE_XML_AS_IS XMLTYPE,
  ORACLE_XML_AS_DOCUMENT XMLTYPE,
  ORACLE_XML_AS_SQLXML XMLTYPE,

  CONSTRAINT pk_t_exotic_types PRIMARY KEY(ID)
)
/

CREATE TABLE t_639_numbers_table (
  ID NUMBER(7) NOT NULL,
  BYTE NUMBER(2),
  SHORT NUMBER(4),
  "INTEGER" NUMBER(7),
  "LONG" NUMBER(18),
  BYTE_DECIMAL DECIMAL(2, 0),
  SHORT_DECIMAL DECIMAL(4, 0),
  INTEGER_DECIMAL DECIMAL(9, 0),
  LONG_DECIMAL DECIMAL(18, 0),
  BIG_INTEGER DECIMAL(22, 0),
  BIG_DECIMAL DECIMAL(22, 5),

  "FLOAT" FLOAT,
  "REAL" REAL,

  CONSTRAINT pk_t_639_numbers_table PRIMARY KEY(ID)
)
/

CREATE TABLE x_test_case_64_69 (
  id NUMBER(7) NOT NULL,
  unused_id NUMBER(7),

  CONSTRAINT pk_x_test_case_64_69 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_64_69a FOREIGN KEY(UNUSED_ID) REFERENCES X_UNUSED(ID),
  CONSTRAINT fk_x_test_case_64_69b FOREIGN KEY(UNUSED_ID) REFERENCES X_UNUSED(ID)
)
/

CREATE TABLE x_test_case_71 (
  id NUMBER(7) NOT NULL,
  test_case_64_69_id NUMBER(4),

  CONSTRAINT pk_x_test_case_71 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_71 FOREIGN KEY(TEST_CASE_64_69_ID) REFERENCES X_TEST_CASE_64_69(ID)
)
/

CREATE TABLE x_test_case_85 (
  id NUMBER(7) NOT NULL,
  x_unused_id NUMBER(7),
  x_unused_name VARCHAR2(10),

  CONSTRAINT pk_x_test_case_85 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_85 FOREIGN KEY(x_unused_id, x_unused_name) REFERENCES X_UNUSED(id, name)
)
/

CREATE TABLE x_test_case_2025 (
  ref_id NUMBER(7) NOT NULL,
  ref_name VARCHAR2(10) NOT NULL,

  CONSTRAINT fk_x_test_case_2025_1 FOREIGN KEY(ref_id) REFERENCES x_test_case_85(ID),
  CONSTRAINT fk_x_test_case_2025_2 FOREIGN KEY(ref_id) REFERENCES x_test_case_71(ID),
  CONSTRAINT fk_x_test_case_2025_3 FOREIGN KEY(ref_id, ref_name) REFERENCES X_UNUSED(id, name)
)
/

CREATE OR REPLACE VIEW v_library (author, title) AS
SELECT a.first_name || ' ' || a.last_name, b.title
FROM t_author a JOIN t_book b ON b.author_id = a.id
/

CREATE MATERIALIZED VIEW m_library REFRESH WITH ROWID AS
SELECT * FROM v_library
/

CREATE VIEW v_author AS
SELECT * FROM t_author
/

CREATE VIEW v_book AS
SELECT * FROM t_book
/

CREATE FORCE VIEW v_incomplete AS
SELECT * FROM t_book
WHERE id = undefined
/

CREATE OR REPLACE PROCEDURE p_unused (in1 VARCHAR2, out1 OUT NUMBER, out2 IN OUT NUMBER)
IS
BEGIN
	NULL;
END p_unused;
/

CREATE OR REPLACE PROCEDURE p_enhance_address1 (address IN u_address_type, no OUT VARCHAR2)
IS
BEGIN
	no := address.street.no;
END p_enhance_address1;
/

CREATE OR REPLACE PROCEDURE p_enhance_address2 (address OUT u_address_type)
IS
BEGIN
	SELECT address
	INTO address
	FROM t_author
	WHERE first_name = 'George';
END p_enhance_address2;
/

CREATE OR REPLACE PROCEDURE p_enhance_address3 (address IN OUT u_address_type)
IS
BEGIN
	address.street.street := 'Zwinglistrasse';
	address.street.no := '17';
	address.street.floors := u_number_array(2);
END p_enhance_address3;
/

CREATE OR REPLACE PROCEDURE p3005 (p1 OUT u_3005)
IS
BEGIN
  p1 := NULL;
END p3005;
/

CREATE OR REPLACE PROCEDURE multi_schema.p4311 (p1 OUT multi_schema.u_4311, p2 IN multi_schema.u_4311)
IS
BEGIN
  p1 := p2;
END p4311;
/

CREATE OR REPLACE PROCEDURE p_create_author_by_name (first_name VARCHAR2, last_name VARCHAR2)
IS
BEGIN
	INSERT INTO T_AUTHOR (ID, FIRST_NAME, LAST_NAME)
	VALUES ((SELECT MAX(ID)+1 FROM T_AUTHOR), first_name, last_name);
END p_create_author_by_name;
/

CREATE OR REPLACE PROCEDURE p_create_author
IS
BEGIN
	p_create_author_by_name('William', 'Shakespeare');
END p_create_author;
/

CREATE OR REPLACE PROCEDURE p_arrays1 (in_array u_number_array, out_array OUT u_number_array)
IS
BEGIN
    out_array := in_array;
END p_arrays1;
/

CREATE OR REPLACE PROCEDURE p_arrays2 (in_array u_number_long_array, out_array OUT u_number_long_array)
IS
BEGIN
    out_array := in_array;
END p_arrays2;
/

CREATE OR REPLACE PROCEDURE p_arrays3 (in_array u_string_array, out_array OUT u_string_array)
IS
BEGIN
    out_array := in_array;
END p_arrays3;
/

CREATE OR REPLACE PROCEDURE p_arrays4 (in_array u_book_array, out_array OUT u_book_array)
IS
BEGIN
    out_array := in_array;
END p_arrays4;
/

CREATE OR REPLACE PROCEDURE p_tables1 (in_table u_number_table, out_table OUT u_number_table)
IS
BEGIN
    out_table := in_table;
END p_tables1;
/

CREATE OR REPLACE PROCEDURE p_tables2 (in_table u_number_long_table, out_table OUT u_number_long_table)
IS
BEGIN
    out_table := in_table;
END p_tables2;
/

CREATE OR REPLACE PROCEDURE p_tables3 (in_table u_string_table, out_table OUT u_string_table)
IS
BEGIN
    out_table := in_table;
END p_tables3;
/

CREATE OR REPLACE PROCEDURE p_tables4 (in_table u_book_table, out_table OUT u_book_table)
IS
BEGIN
    out_table := in_table;
END p_tables4;
/


CREATE OR REPLACE PROCEDURE p_default (
  p_in_number   IN  number   := 0,
  p_out_number  OUT number,
  p_in_varchar  IN  varchar2 := '0',
  p_out_varchar OUT varchar2,
  p_in_date     IN  date     := date '1981-07-10',
  p_out_date    OUT date
)
IS
BEGIN
    p_out_number := p_in_number;
	p_out_varchar := p_in_varchar;
	p_out_date := p_in_date;
END p_default;
/

CREATE OR REPLACE PROCEDURE p_many_parameters (
  f000 number, f001 number, f002 number, f003 number, f004 number,
  f005 number, f006 number, f007 number, f008 number, f009 number,
  f010 number, f011 number, f012 number, f013 number, f014 number,
  f015 number, f016 number, f017 number, f018 number, f019 number,
  f020 number, f021 number, f022 number, f023 number, f024 number,
  f025 number, f026 number, f027 number, f028 number, f029 number,
  f030 number, f031 number, f032 number, f033 number, f034 number,
  f035 number, f036 number, f037 number, f038 number, f039 number,
  f040 number, f041 number, f042 number, f043 number, f044 number,
  f045 number, f046 number, f047 number, f048 number, f049 number,
  f050 number, f051 number, f052 number, f053 number, f054 number,
  f055 number, f056 number, f057 number, f058 number, f059 number,
  f060 number, f061 number, f062 number, f063 number, f064 number,
  f065 number, f066 number, f067 number, f068 number, f069 number,
  f070 number, f071 number, f072 number, f073 number, f074 number,
  f075 number, f076 number, f077 number, f078 number, f079 number,
  f080 number, f081 number, f082 number, f083 number, f084 number,
  f085 number, f086 number, f087 number, f088 number, f089 number,
  f090 number, f091 number, f092 number, f093 number, f094 number,
  f095 number, f096 number, f097 number, f098 number, f099 number,

  f100 number, f101 number, f102 number, f103 number, f104 number,
  f105 number, f106 number, f107 number, f108 number, f109 number,
  f110 number, f111 number, f112 number, f113 number, f114 number,
  f115 number, f116 number, f117 number, f118 number, f119 number,
  f120 number, f121 number, f122 number, f123 number, f124 number,
  f125 number, f126 number, f127 number, f128 number, f129 number,
  f130 number, f131 number, f132 number, f133 number, f134 number,
  f135 number, f136 number, f137 number, f138 number, f139 number,
  f140 number, f141 number, f142 number, f143 number, f144 number,
  f145 number, f146 number, f147 number, f148 number, f149 number,
  f150 number, f151 number, f152 number, f153 number, f154 number,
  f155 number, f156 number, f157 number, f158 number, f159 number,
  f160 number, f161 number, f162 number, f163 number, f164 number,
  f165 number, f166 number, f167 number, f168 number, f169 number,
  f170 number, f171 number, f172 number, f173 number, f174 number,
  f175 number, f176 number, f177 number, f178 number, f179 number,
  f180 number, f181 number, f182 number, f183 number, f184 number,
  f185 number, f186 number, f187 number, f188 number, f189 number,
  f190 number, f191 number, f192 number, f193 number, f194 number,
  f195 number, f196 number, f197 number, f198 number, f199 number,

  f200 number, f201 number, f202 number, f203 number, f204 number,
  f205 number, f206 number, f207 number, f208 number, f209 number,
  f210 number, f211 number, f212 number, f213 number, f214 number,
  f215 number, f216 number, f217 number, f218 number, f219 number,
  f220 number, f221 number, f222 number, f223 number, f224 number,
  f225 number, f226 number, f227 number, f228 number, f229 number,
  f230 number, f231 number, f232 number, f233 number, f234 number,
  f235 number, f236 number, f237 number, f238 number, f239 number,
  f240 number, f241 number, f242 number, f243 number, f244 number,
  f245 number, f246 number, f247 number, f248 number, f249 number,
  f250 number, f251 number, f252 number, f253 number, f254 number,
  f255 number, f256 number, f257 number, f258 number, f259 number,
  f260 number, f261 number, f262 number, f263 number, f264 number,
  f265 number, f266 number, f267 number, f268 number, f269 number,
  f270 number, f271 number, f272 number, f273 number, f274 number,
  f275 number, f276 number, f277 number, f278 number, f279 number,
  f280 number, f281 number, f282 number, f283 number, f284 number,
  f285 number, f286 number, f287 number, f288 number, f289 number,
  f290 number, f291 number, f292 number, f293 number, f294 number,
  f295 number, f296 number, f297 number, f298 number, f299 number,

  f300 number, f301 number, f302 number, f303 number, f304 number,
  f305 number, f306 number, f307 number, f308 number, f309 number,
  f310 number, f311 number, f312 number, f313 number, f314 number,
  f315 number, f316 number, f317 number, f318 number, f319 number,
  f320 number, f321 number, f322 number, f323 number, f324 number,
  f325 number, f326 number, f327 number, f328 number, f329 number,
  f330 number, f331 number, f332 number, f333 number, f334 number,
  f335 number, f336 number, f337 number, f338 number, f339 number,
  f340 number, f341 number, f342 number, f343 number, f344 number,
  f345 number, f346 number, f347 number, f348 number, f349 number,
  f350 number, f351 number, f352 number, f353 number, f354 number,
  f355 number, f356 number, f357 number, f358 number, f359 number,
  f360 number, f361 number, f362 number, f363 number, f364 number,
  f365 number, f366 number, f367 number, f368 number, f369 number,
  f370 number, f371 number, f372 number, f373 number, f374 number,
  f375 number, f376 number, f377 number, f378 number, f379 number,
  f380 number, f381 number, f382 number, f383 number, f384 number,
  f385 number, f386 number, f387 number, f388 number, f389 number,
  f390 number, f391 number, f392 number, f393 number, f394 number,
  f395 number, f396 number, f397 number, f398 number, f399 number
)
IS
BEGIN
	NULL;
END p_many_parameters;
/

CREATE OR REPLACE FUNCTION f_many_parameters (
  f000 number, f001 number, f002 number, f003 number, f004 number,
  f005 number, f006 number, f007 number, f008 number, f009 number,
  f010 number, f011 number, f012 number, f013 number, f014 number,
  f015 number, f016 number, f017 number, f018 number, f019 number,
  f020 number, f021 number, f022 number, f023 number, f024 number,
  f025 number, f026 number, f027 number, f028 number, f029 number,
  f030 number, f031 number, f032 number, f033 number, f034 number,
  f035 number, f036 number, f037 number, f038 number, f039 number,
  f040 number, f041 number, f042 number, f043 number, f044 number,
  f045 number, f046 number, f047 number, f048 number, f049 number,
  f050 number, f051 number, f052 number, f053 number, f054 number,
  f055 number, f056 number, f057 number, f058 number, f059 number,
  f060 number, f061 number, f062 number, f063 number, f064 number,
  f065 number, f066 number, f067 number, f068 number, f069 number,
  f070 number, f071 number, f072 number, f073 number, f074 number,
  f075 number, f076 number, f077 number, f078 number, f079 number,
  f080 number, f081 number, f082 number, f083 number, f084 number,
  f085 number, f086 number, f087 number, f088 number, f089 number,
  f090 number, f091 number, f092 number, f093 number, f094 number,
  f095 number, f096 number, f097 number, f098 number, f099 number,

  f100 number, f101 number, f102 number, f103 number, f104 number,
  f105 number, f106 number, f107 number, f108 number, f109 number,
  f110 number, f111 number, f112 number, f113 number, f114 number,
  f115 number, f116 number, f117 number, f118 number, f119 number,
  f120 number, f121 number, f122 number, f123 number, f124 number,
  f125 number, f126 number, f127 number, f128 number, f129 number,
  f130 number, f131 number, f132 number, f133 number, f134 number,
  f135 number, f136 number, f137 number, f138 number, f139 number,
  f140 number, f141 number, f142 number, f143 number, f144 number,
  f145 number, f146 number, f147 number, f148 number, f149 number,
  f150 number, f151 number, f152 number, f153 number, f154 number,
  f155 number, f156 number, f157 number, f158 number, f159 number,
  f160 number, f161 number, f162 number, f163 number, f164 number,
  f165 number, f166 number, f167 number, f168 number, f169 number,
  f170 number, f171 number, f172 number, f173 number, f174 number,
  f175 number, f176 number, f177 number, f178 number, f179 number,
  f180 number, f181 number, f182 number, f183 number, f184 number,
  f185 number, f186 number, f187 number, f188 number, f189 number,
  f190 number, f191 number, f192 number, f193 number, f194 number,
  f195 number, f196 number, f197 number, f198 number, f199 number,

  f200 number, f201 number, f202 number, f203 number, f204 number,
  f205 number, f206 number, f207 number, f208 number, f209 number,
  f210 number, f211 number, f212 number, f213 number, f214 number,
  f215 number, f216 number, f217 number, f218 number, f219 number,
  f220 number, f221 number, f222 number, f223 number, f224 number,
  f225 number, f226 number, f227 number, f228 number, f229 number,
  f230 number, f231 number, f232 number, f233 number, f234 number,
  f235 number, f236 number, f237 number, f238 number, f239 number,
  f240 number, f241 number, f242 number, f243 number, f244 number,
  f245 number, f246 number, f247 number, f248 number, f249 number,
  f250 number, f251 number, f252 number, f253 number, f254 number,
  f255 number, f256 number, f257 number, f258 number, f259 number,
  f260 number, f261 number, f262 number, f263 number, f264 number,
  f265 number, f266 number, f267 number, f268 number, f269 number,
  f270 number, f271 number, f272 number, f273 number, f274 number,
  f275 number, f276 number, f277 number, f278 number, f279 number,
  f280 number, f281 number, f282 number, f283 number, f284 number,
  f285 number, f286 number, f287 number, f288 number, f289 number,
  f290 number, f291 number, f292 number, f293 number, f294 number,
  f295 number, f296 number, f297 number, f298 number, f299 number,

  f300 number, f301 number, f302 number, f303 number, f304 number,
  f305 number, f306 number, f307 number, f308 number, f309 number,
  f310 number, f311 number, f312 number, f313 number, f314 number,
  f315 number, f316 number, f317 number, f318 number, f319 number,
  f320 number, f321 number, f322 number, f323 number, f324 number,
  f325 number, f326 number, f327 number, f328 number, f329 number,
  f330 number, f331 number, f332 number, f333 number, f334 number,
  f335 number, f336 number, f337 number, f338 number, f339 number,
  f340 number, f341 number, f342 number, f343 number, f344 number,
  f345 number, f346 number, f347 number, f348 number, f349 number,
  f350 number, f351 number, f352 number, f353 number, f354 number,
  f355 number, f356 number, f357 number, f358 number, f359 number,
  f360 number, f361 number, f362 number, f363 number, f364 number,
  f365 number, f366 number, f367 number, f368 number, f369 number,
  f370 number, f371 number, f372 number, f373 number, f374 number,
  f375 number, f376 number, f377 number, f378 number, f379 number,
  f380 number, f381 number, f382 number, f383 number, f384 number,
  f385 number, f386 number, f387 number, f388 number, f389 number,
  f390 number, f391 number, f392 number, f393 number, f394 number,
  f395 number, f396 number, f397 number, f398 number, f399 number
)
RETURN number
IS
BEGIN
	return NULL;
END f_many_parameters;
/

CREATE OR REPLACE FUNCTION f_pipelined_array1
RETURN u_number_array
PIPELINED
AS
BEGIN
	FOR i in 1 .. 10 LOOP
	    PIPE ROW(i);
	END LOOP;
	RETURN;
END f_pipelined_array1;
/

CREATE OR REPLACE FUNCTION f_pipelined_table1
RETURN u_number_table
PIPELINED
AS
BEGIN
	FOR i in 1 .. 10 LOOP
	    PIPE ROW(i);
	END LOOP;
	RETURN;
END f_pipelined_table1;
/

CREATE OR REPLACE FUNCTION f_pipelined_array4
RETURN u_book_array
PIPELINED
AS
	title VARCHAR2(100);
BEGIN
	FOR i in 1 .. 4 LOOP
		SELECT b.title
		INTO title
		FROM t_book b
		WHERE b.id = i;

	    PIPE ROW(u_book_type(i, title));
	END LOOP;
	RETURN;
END f_pipelined_array4;
/

CREATE OR REPLACE FUNCTION f_pipelined_table4
RETURN u_book_table
PIPELINED
AS
	title VARCHAR2(100);
BEGIN
	FOR i in 1 .. 4 LOOP
		SELECT b.title
		INTO title
		FROM t_book b
		WHERE b.id = i;

	    PIPE ROW(u_book_type(i, title));
	END LOOP;
	RETURN;
END f_pipelined_table4;
/

CREATE OR REPLACE FUNCTION f_arrays1 (in_array u_number_array)
RETURN u_number_array
IS
BEGIN
    return in_array;
END f_arrays1;
/

CREATE OR REPLACE FUNCTION f_arrays2 (in_array u_number_long_array)
RETURN u_number_long_array
IS
BEGIN
    return in_array;
END f_arrays2;
/

CREATE OR REPLACE FUNCTION f_arrays3 (in_array u_string_array)
RETURN u_string_array
IS
BEGIN
    return in_array;
END f_arrays3;
/

CREATE OR REPLACE FUNCTION f_arrays4 (in_array u_book_array)
RETURN u_book_array
IS
BEGIN
    return in_array;
END f_arrays4;
/

CREATE OR REPLACE FUNCTION f_tables1 (in_table u_number_table)
RETURN u_number_table
IS
BEGIN
    return in_table;
END f_tables1;
/

CREATE OR REPLACE FUNCTION f_tables2 (in_table u_number_long_table)
RETURN u_number_long_table
IS
BEGIN
    return in_table;
END f_tables2;
/

CREATE OR REPLACE FUNCTION f_tables3 (in_table u_string_table)
RETURN u_string_table
IS
BEGIN
    return in_table;
END f_tables3;
/

CREATE OR REPLACE FUNCTION f_tables4 (in_table u_book_table)
RETURN u_book_table
IS
BEGIN
    return in_table;
END f_tables4;
/

CREATE OR REPLACE PROCEDURE p_author_exists (author_name VARCHAR2, result OUT NUMBER)
IS
  v_result NUMBER(1);
BEGIN
  SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
    INTO v_result
    FROM t_author
   WHERE first_name LIKE author_name
      OR last_name LIKE author_name;

  result := v_result;
END p_author_exists;
/

CREATE OR REPLACE PROCEDURE p391 (
	i1 NUMBER, io1 IN OUT NUMBER, o1 OUT NUMBER,
	o2 OUT NUMBER, io2 IN OUT NUMBER, i2 NUMBER) IS
BEGIN
  o1 := io1;
  io1 := i1;

  o2 := io2;
  io2 := i2;
END p391;
/

CREATE OR REPLACE PROCEDURE p2155 (p1 NUMBER, p2 DATE, p3 OUT NUMBER, p4 OUT DATE, p5 OUT DATE) IS
BEGIN
  p3 := p1;
  p4 := p2;
  p5 := p2;
END p2155;
/

CREATE OR REPLACE FUNCTION f2155 (p1 NUMBER, p2 DATE, p3 NUMBER, p4 DATE) RETURN DATE IS
BEGIN
  IF p1 IS NULL THEN
    RETURN p2;
  ELSE
    RETURN p4;
  END IF;
END;
/

CREATE OR REPLACE PROCEDURE p1490 ("value" NUMBER) IS
BEGIN
  NULL;
END p1490;
/

CREATE OR REPLACE PACKAGE library_types AS
    TYPE t_cursor_type IS REF CURSOR;
    TYPE t_author_type IS TABLE OF t_author%rowtype;
END library_types;
/

CREATE OR REPLACE PACKAGE library_767_package_test AS
    FUNCTION library_767_package_test RETURN NUMBER;
END library_767_package_test;
/

CREATE TYPE u_address_table AS TABLE OF u_address_type
/

CREATE OR REPLACE PROCEDURE p_get_two_cursors (
	books   OUT library_types.t_cursor_type,
	authors OUT library_types.t_cursor_type)
IS
BEGIN
	OPEN books   FOR SELECT * FROM t_book ORDER BY id ASC;
	OPEN authors FOR SELECT * FROM t_author ORDER BY id ASC;
END p_get_two_cursors;
/

CREATE OR REPLACE PROCEDURE p_get_one_cursor (
    total   OUT NUMBER,
	books   OUT library_types.t_cursor_type,
	book_ids IN u_number_array)
IS
BEGIN
	OPEN books FOR SELECT * FROM t_book WHERE id IN (SELECT * FROM TABLE(book_ids)) ORDER BY id ASC;
	SELECT count(*) INTO total FROM t_book WHERE id IN (SELECT * FROM TABLE(book_ids));
END p_get_one_cursor;
/

CREATE OR REPLACE FUNCTION f_get_one_cursor (
    book_ids IN u_number_array)
RETURN library_types.t_cursor_type
IS
	books library_types.t_cursor_type;
BEGIN
	IF (book_ids IS NULL) THEN
	    OPEN books FOR SELECT * FROM t_book WHERE 1 = 0;
	ELSE
		OPEN books FOR SELECT * FROM t_book WHERE id IN (SELECT * FROM TABLE(book_ids)) ORDER BY id ASC;
	END IF;

	RETURN books;
END f_get_one_cursor;
/

CREATE OR REPLACE FUNCTION f691cursor_out
RETURN library_types.t_cursor_type
IS
    books library_types.t_cursor_type;
BEGIN
	OPEN books FOR SELECT * FROM t_book ORDER BY ID;
	RETURN books;
END f691cursor_out;
/

CREATE OR REPLACE FUNCTION f691cursor_in (c IN library_types.t_cursor_type)
RETURN NUMBER
IS
    book t_book%rowtype;
    result number := 0;
BEGIN
	LOOP
	    FETCH c INTO book;
	    EXIT WHEN c%notfound;

	    result := result + 1;
	END LOOP;

	CLOSE c;

	RETURN result;
END f691cursor_in;
/

CREATE OR REPLACE FUNCTION f_author_exists (author_name VARCHAR2)
RETURN NUMBER
IS
  v_result NUMBER(1);
BEGIN
  SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
    INTO v_result
    FROM t_author
   WHERE first_name LIKE author_name
      OR last_name LIKE author_name;

  return v_result;
END f_author_exists;
/

CREATE OR REPLACE FUNCTION f_one
RETURN NUMBER
IS
BEGIN
	RETURN 1;
END f_one;
/

CREATE OR REPLACE FUNCTION f_number(n NUMBER)
RETURN NUMBER
IS
BEGIN
	RETURN n;
END f_number;
/

CREATE OR REPLACE FUNCTION f317 (p1 NUMBER, p2 NUMBER, p3 NUMBER, p4 NUMBER) RETURN NUMBER deterministic IS
BEGIN
  return 1000 * p1 + 100 * p2 + p4;
END f317;
/

CREATE OR REPLACE FUNCTION f378 (i NUMBER, io IN OUT NUMBER, o OUT NUMBER) RETURN NUMBER IS
  r NUMBER;
BEGIN
  r := i + io;
  o := io;
  io := i;
  return r;
END f378;
/

CREATE OR REPLACE FUNCTION f377 RETURN NUMBER IS
BEGIN
	syntax error;
END f377;
/

CREATE OR REPLACE PROCEDURE p377 RETURN NUMBER IS
BEGIN
	syntax error;
END p377;
/

CREATE OR REPLACE PACKAGE library AS
	PROCEDURE pkg_p_author_exists (author_name VARCHAR2, result OUT NUMBER);
	PROCEDURE pkg_p_author_exists (author_name VARCHAR2, result OUT NUMBER, unused IN OUT NUMBER);
	FUNCTION pkg_f_author_exists (author_name VARCHAR2) RETURN NUMBER;
	FUNCTION pkg_f_author_exists (author_name VARCHAR2, unused NUMBER) RETURN NUMBER;
	FUNCTION pkg_f_unused RETURN NUMBER;
	FUNCTION pkg_f378 (i NUMBER, io IN OUT NUMBER, o OUT NUMBER) RETURN NUMBER;

	PROCEDURE pkg_p_many_parameters (
	  f000 number, f001 number, f002 number, f003 number, f004 number,
	  f005 number, f006 number, f007 number, f008 number, f009 number,
	  f010 number, f011 number, f012 number, f013 number, f014 number,
	  f015 number, f016 number, f017 number, f018 number, f019 number,
	  f020 number, f021 number, f022 number, f023 number, f024 number,
	  f025 number, f026 number, f027 number, f028 number, f029 number,
	  f030 number, f031 number, f032 number, f033 number, f034 number,
	  f035 number, f036 number, f037 number, f038 number, f039 number,
	  f040 number, f041 number, f042 number, f043 number, f044 number,
	  f045 number, f046 number, f047 number, f048 number, f049 number,
	  f050 number, f051 number, f052 number, f053 number, f054 number,
	  f055 number, f056 number, f057 number, f058 number, f059 number,
	  f060 number, f061 number, f062 number, f063 number, f064 number,
	  f065 number, f066 number, f067 number, f068 number, f069 number,
	  f070 number, f071 number, f072 number, f073 number, f074 number,
	  f075 number, f076 number, f077 number, f078 number, f079 number,
	  f080 number, f081 number, f082 number, f083 number, f084 number,
	  f085 number, f086 number, f087 number, f088 number, f089 number,
	  f090 number, f091 number, f092 number, f093 number, f094 number,
	  f095 number, f096 number, f097 number, f098 number, f099 number,

	  f100 number, f101 number, f102 number, f103 number, f104 number,
	  f105 number, f106 number, f107 number, f108 number, f109 number,
	  f110 number, f111 number, f112 number, f113 number, f114 number,
	  f115 number, f116 number, f117 number, f118 number, f119 number,
	  f120 number, f121 number, f122 number, f123 number, f124 number,
	  f125 number, f126 number, f127 number, f128 number, f129 number,
	  f130 number, f131 number, f132 number, f133 number, f134 number,
	  f135 number, f136 number, f137 number, f138 number, f139 number,
	  f140 number, f141 number, f142 number, f143 number, f144 number,
	  f145 number, f146 number, f147 number, f148 number, f149 number,
	  f150 number, f151 number, f152 number, f153 number, f154 number,
	  f155 number, f156 number, f157 number, f158 number, f159 number,
	  f160 number, f161 number, f162 number, f163 number, f164 number,
	  f165 number, f166 number, f167 number, f168 number, f169 number,
	  f170 number, f171 number, f172 number, f173 number, f174 number,
	  f175 number, f176 number, f177 number, f178 number, f179 number,
	  f180 number, f181 number, f182 number, f183 number, f184 number,
	  f185 number, f186 number, f187 number, f188 number, f189 number,
	  f190 number, f191 number, f192 number, f193 number, f194 number,
	  f195 number, f196 number, f197 number, f198 number, f199 number,

	  f200 number, f201 number, f202 number, f203 number, f204 number,
	  f205 number, f206 number, f207 number, f208 number, f209 number,
	  f210 number, f211 number, f212 number, f213 number, f214 number,
	  f215 number, f216 number, f217 number, f218 number, f219 number,
	  f220 number, f221 number, f222 number, f223 number, f224 number,
	  f225 number, f226 number, f227 number, f228 number, f229 number,
	  f230 number, f231 number, f232 number, f233 number, f234 number,
	  f235 number, f236 number, f237 number, f238 number, f239 number,
	  f240 number, f241 number, f242 number, f243 number, f244 number,
	  f245 number, f246 number, f247 number, f248 number, f249 number,
	  f250 number, f251 number, f252 number, f253 number, f254 number,
	  f255 number, f256 number, f257 number, f258 number, f259 number,
	  f260 number, f261 number, f262 number, f263 number, f264 number,
	  f265 number, f266 number, f267 number, f268 number, f269 number,
	  f270 number, f271 number, f272 number, f273 number, f274 number,
	  f275 number, f276 number, f277 number, f278 number, f279 number,
	  f280 number, f281 number, f282 number, f283 number, f284 number,
	  f285 number, f286 number, f287 number, f288 number, f289 number,
	  f290 number, f291 number, f292 number, f293 number, f294 number,
	  f295 number, f296 number, f297 number, f298 number, f299 number,

	  f300 number, f301 number, f302 number, f303 number, f304 number,
	  f305 number, f306 number, f307 number, f308 number, f309 number,
	  f310 number, f311 number, f312 number, f313 number, f314 number,
	  f315 number, f316 number, f317 number, f318 number, f319 number,
	  f320 number, f321 number, f322 number, f323 number, f324 number,
	  f325 number, f326 number, f327 number, f328 number, f329 number,
	  f330 number, f331 number, f332 number, f333 number, f334 number,
	  f335 number, f336 number, f337 number, f338 number, f339 number,
	  f340 number, f341 number, f342 number, f343 number, f344 number,
	  f345 number, f346 number, f347 number, f348 number, f349 number,
	  f350 number, f351 number, f352 number, f353 number, f354 number,
	  f355 number, f356 number, f357 number, f358 number, f359 number,
	  f360 number, f361 number, f362 number, f363 number, f364 number,
	  f365 number, f366 number, f367 number, f368 number, f369 number,
	  f370 number, f371 number, f372 number, f373 number, f374 number,
	  f375 number, f376 number, f377 number, f378 number, f379 number,
	  f380 number, f381 number, f382 number, f383 number, f384 number,
	  f385 number, f386 number, f387 number, f388 number, f389 number,
	  f390 number, f391 number, f392 number, f393 number, f394 number,
	  f395 number, f396 number, f397 number, f398 number, f399 number
	);

	FUNCTION pkg_f_many_parameters (
	  f000 number, f001 number, f002 number, f003 number, f004 number,
	  f005 number, f006 number, f007 number, f008 number, f009 number,
	  f010 number, f011 number, f012 number, f013 number, f014 number,
	  f015 number, f016 number, f017 number, f018 number, f019 number,
	  f020 number, f021 number, f022 number, f023 number, f024 number,
	  f025 number, f026 number, f027 number, f028 number, f029 number,
	  f030 number, f031 number, f032 number, f033 number, f034 number,
	  f035 number, f036 number, f037 number, f038 number, f039 number,
	  f040 number, f041 number, f042 number, f043 number, f044 number,
	  f045 number, f046 number, f047 number, f048 number, f049 number,
	  f050 number, f051 number, f052 number, f053 number, f054 number,
	  f055 number, f056 number, f057 number, f058 number, f059 number,
	  f060 number, f061 number, f062 number, f063 number, f064 number,
	  f065 number, f066 number, f067 number, f068 number, f069 number,
	  f070 number, f071 number, f072 number, f073 number, f074 number,
	  f075 number, f076 number, f077 number, f078 number, f079 number,
	  f080 number, f081 number, f082 number, f083 number, f084 number,
	  f085 number, f086 number, f087 number, f088 number, f089 number,
	  f090 number, f091 number, f092 number, f093 number, f094 number,
	  f095 number, f096 number, f097 number, f098 number, f099 number,

	  f100 number, f101 number, f102 number, f103 number, f104 number,
	  f105 number, f106 number, f107 number, f108 number, f109 number,
	  f110 number, f111 number, f112 number, f113 number, f114 number,
	  f115 number, f116 number, f117 number, f118 number, f119 number,
	  f120 number, f121 number, f122 number, f123 number, f124 number,
	  f125 number, f126 number, f127 number, f128 number, f129 number,
	  f130 number, f131 number, f132 number, f133 number, f134 number,
	  f135 number, f136 number, f137 number, f138 number, f139 number,
	  f140 number, f141 number, f142 number, f143 number, f144 number,
	  f145 number, f146 number, f147 number, f148 number, f149 number,
	  f150 number, f151 number, f152 number, f153 number, f154 number,
	  f155 number, f156 number, f157 number, f158 number, f159 number,
	  f160 number, f161 number, f162 number, f163 number, f164 number,
	  f165 number, f166 number, f167 number, f168 number, f169 number,
	  f170 number, f171 number, f172 number, f173 number, f174 number,
	  f175 number, f176 number, f177 number, f178 number, f179 number,
	  f180 number, f181 number, f182 number, f183 number, f184 number,
	  f185 number, f186 number, f187 number, f188 number, f189 number,
	  f190 number, f191 number, f192 number, f193 number, f194 number,
	  f195 number, f196 number, f197 number, f198 number, f199 number,

	  f200 number, f201 number, f202 number, f203 number, f204 number,
	  f205 number, f206 number, f207 number, f208 number, f209 number,
	  f210 number, f211 number, f212 number, f213 number, f214 number,
	  f215 number, f216 number, f217 number, f218 number, f219 number,
	  f220 number, f221 number, f222 number, f223 number, f224 number,
	  f225 number, f226 number, f227 number, f228 number, f229 number,
	  f230 number, f231 number, f232 number, f233 number, f234 number,
	  f235 number, f236 number, f237 number, f238 number, f239 number,
	  f240 number, f241 number, f242 number, f243 number, f244 number,
	  f245 number, f246 number, f247 number, f248 number, f249 number,
	  f250 number, f251 number, f252 number, f253 number, f254 number,
	  f255 number, f256 number, f257 number, f258 number, f259 number,
	  f260 number, f261 number, f262 number, f263 number, f264 number,
	  f265 number, f266 number, f267 number, f268 number, f269 number,
	  f270 number, f271 number, f272 number, f273 number, f274 number,
	  f275 number, f276 number, f277 number, f278 number, f279 number,
	  f280 number, f281 number, f282 number, f283 number, f284 number,
	  f285 number, f286 number, f287 number, f288 number, f289 number,
	  f290 number, f291 number, f292 number, f293 number, f294 number,
	  f295 number, f296 number, f297 number, f298 number, f299 number,

	  f300 number, f301 number, f302 number, f303 number, f304 number,
	  f305 number, f306 number, f307 number, f308 number, f309 number,
	  f310 number, f311 number, f312 number, f313 number, f314 number,
	  f315 number, f316 number, f317 number, f318 number, f319 number,
	  f320 number, f321 number, f322 number, f323 number, f324 number,
	  f325 number, f326 number, f327 number, f328 number, f329 number,
	  f330 number, f331 number, f332 number, f333 number, f334 number,
	  f335 number, f336 number, f337 number, f338 number, f339 number,
	  f340 number, f341 number, f342 number, f343 number, f344 number,
	  f345 number, f346 number, f347 number, f348 number, f349 number,
	  f350 number, f351 number, f352 number, f353 number, f354 number,
	  f355 number, f356 number, f357 number, f358 number, f359 number,
	  f360 number, f361 number, f362 number, f363 number, f364 number,
	  f365 number, f366 number, f367 number, f368 number, f369 number,
	  f370 number, f371 number, f372 number, f373 number, f374 number,
	  f375 number, f376 number, f377 number, f378 number, f379 number,
	  f380 number, f381 number, f382 number, f383 number, f384 number,
	  f385 number, f386 number, f387 number, f388 number, f389 number,
	  f390 number, f391 number, f392 number, f393 number, f394 number,
	  f395 number, f396 number, f397 number, f398 number, f399 number
	) return number;

END library;
/

CREATE OR REPLACE PACKAGE BODY library AS
	PROCEDURE pkg_p_author_exists (author_name VARCHAR2, result OUT NUMBER) IS
	BEGIN
		test.p_author_exists(author_name, result);
	END pkg_p_author_exists;

	PROCEDURE pkg_p_author_exists (author_name VARCHAR2, result OUT NUMBER, unused IN OUT NUMBER) IS
	BEGIN
		test.p_author_exists(author_name, result);
	END pkg_p_author_exists;

	FUNCTION pkg_f_author_exists (author_name VARCHAR2) RETURN NUMBER IS
	BEGIN
		return test.f_author_exists(author_name);
	END pkg_f_author_exists;

	FUNCTION pkg_f_author_exists (author_name VARCHAR2, unused NUMBER) RETURN NUMBER IS
	BEGIN
		return test.f_author_exists(author_name);
	END pkg_f_author_exists;

	FUNCTION pkg_f_unused RETURN NUMBER IS
	BEGIN
		return 0;
	END pkg_f_unused;

	FUNCTION pkg_f378 (i NUMBER, io IN OUT NUMBER, o OUT NUMBER) RETURN NUMBER IS
	BEGIN
	  return test.f378 (i, io, o);
	END pkg_f378;

	PROCEDURE pkg_p_many_parameters (
	  f000 number, f001 number, f002 number, f003 number, f004 number,
	  f005 number, f006 number, f007 number, f008 number, f009 number,
	  f010 number, f011 number, f012 number, f013 number, f014 number,
	  f015 number, f016 number, f017 number, f018 number, f019 number,
	  f020 number, f021 number, f022 number, f023 number, f024 number,
	  f025 number, f026 number, f027 number, f028 number, f029 number,
	  f030 number, f031 number, f032 number, f033 number, f034 number,
	  f035 number, f036 number, f037 number, f038 number, f039 number,
	  f040 number, f041 number, f042 number, f043 number, f044 number,
	  f045 number, f046 number, f047 number, f048 number, f049 number,
	  f050 number, f051 number, f052 number, f053 number, f054 number,
	  f055 number, f056 number, f057 number, f058 number, f059 number,
	  f060 number, f061 number, f062 number, f063 number, f064 number,
	  f065 number, f066 number, f067 number, f068 number, f069 number,
	  f070 number, f071 number, f072 number, f073 number, f074 number,
	  f075 number, f076 number, f077 number, f078 number, f079 number,
	  f080 number, f081 number, f082 number, f083 number, f084 number,
	  f085 number, f086 number, f087 number, f088 number, f089 number,
	  f090 number, f091 number, f092 number, f093 number, f094 number,
	  f095 number, f096 number, f097 number, f098 number, f099 number,

	  f100 number, f101 number, f102 number, f103 number, f104 number,
	  f105 number, f106 number, f107 number, f108 number, f109 number,
	  f110 number, f111 number, f112 number, f113 number, f114 number,
	  f115 number, f116 number, f117 number, f118 number, f119 number,
	  f120 number, f121 number, f122 number, f123 number, f124 number,
	  f125 number, f126 number, f127 number, f128 number, f129 number,
	  f130 number, f131 number, f132 number, f133 number, f134 number,
	  f135 number, f136 number, f137 number, f138 number, f139 number,
	  f140 number, f141 number, f142 number, f143 number, f144 number,
	  f145 number, f146 number, f147 number, f148 number, f149 number,
	  f150 number, f151 number, f152 number, f153 number, f154 number,
	  f155 number, f156 number, f157 number, f158 number, f159 number,
	  f160 number, f161 number, f162 number, f163 number, f164 number,
	  f165 number, f166 number, f167 number, f168 number, f169 number,
	  f170 number, f171 number, f172 number, f173 number, f174 number,
	  f175 number, f176 number, f177 number, f178 number, f179 number,
	  f180 number, f181 number, f182 number, f183 number, f184 number,
	  f185 number, f186 number, f187 number, f188 number, f189 number,
	  f190 number, f191 number, f192 number, f193 number, f194 number,
	  f195 number, f196 number, f197 number, f198 number, f199 number,

	  f200 number, f201 number, f202 number, f203 number, f204 number,
	  f205 number, f206 number, f207 number, f208 number, f209 number,
	  f210 number, f211 number, f212 number, f213 number, f214 number,
	  f215 number, f216 number, f217 number, f218 number, f219 number,
	  f220 number, f221 number, f222 number, f223 number, f224 number,
	  f225 number, f226 number, f227 number, f228 number, f229 number,
	  f230 number, f231 number, f232 number, f233 number, f234 number,
	  f235 number, f236 number, f237 number, f238 number, f239 number,
	  f240 number, f241 number, f242 number, f243 number, f244 number,
	  f245 number, f246 number, f247 number, f248 number, f249 number,
	  f250 number, f251 number, f252 number, f253 number, f254 number,
	  f255 number, f256 number, f257 number, f258 number, f259 number,
	  f260 number, f261 number, f262 number, f263 number, f264 number,
	  f265 number, f266 number, f267 number, f268 number, f269 number,
	  f270 number, f271 number, f272 number, f273 number, f274 number,
	  f275 number, f276 number, f277 number, f278 number, f279 number,
	  f280 number, f281 number, f282 number, f283 number, f284 number,
	  f285 number, f286 number, f287 number, f288 number, f289 number,
	  f290 number, f291 number, f292 number, f293 number, f294 number,
	  f295 number, f296 number, f297 number, f298 number, f299 number,

	  f300 number, f301 number, f302 number, f303 number, f304 number,
	  f305 number, f306 number, f307 number, f308 number, f309 number,
	  f310 number, f311 number, f312 number, f313 number, f314 number,
	  f315 number, f316 number, f317 number, f318 number, f319 number,
	  f320 number, f321 number, f322 number, f323 number, f324 number,
	  f325 number, f326 number, f327 number, f328 number, f329 number,
	  f330 number, f331 number, f332 number, f333 number, f334 number,
	  f335 number, f336 number, f337 number, f338 number, f339 number,
	  f340 number, f341 number, f342 number, f343 number, f344 number,
	  f345 number, f346 number, f347 number, f348 number, f349 number,
	  f350 number, f351 number, f352 number, f353 number, f354 number,
	  f355 number, f356 number, f357 number, f358 number, f359 number,
	  f360 number, f361 number, f362 number, f363 number, f364 number,
	  f365 number, f366 number, f367 number, f368 number, f369 number,
	  f370 number, f371 number, f372 number, f373 number, f374 number,
	  f375 number, f376 number, f377 number, f378 number, f379 number,
	  f380 number, f381 number, f382 number, f383 number, f384 number,
	  f385 number, f386 number, f387 number, f388 number, f389 number,
	  f390 number, f391 number, f392 number, f393 number, f394 number,
	  f395 number, f396 number, f397 number, f398 number, f399 number
	)
	IS
	BEGIN
		NULL;
	END pkg_p_many_parameters;

	FUNCTION pkg_f_many_parameters (
	  f000 number, f001 number, f002 number, f003 number, f004 number,
	  f005 number, f006 number, f007 number, f008 number, f009 number,
	  f010 number, f011 number, f012 number, f013 number, f014 number,
	  f015 number, f016 number, f017 number, f018 number, f019 number,
	  f020 number, f021 number, f022 number, f023 number, f024 number,
	  f025 number, f026 number, f027 number, f028 number, f029 number,
	  f030 number, f031 number, f032 number, f033 number, f034 number,
	  f035 number, f036 number, f037 number, f038 number, f039 number,
	  f040 number, f041 number, f042 number, f043 number, f044 number,
	  f045 number, f046 number, f047 number, f048 number, f049 number,
	  f050 number, f051 number, f052 number, f053 number, f054 number,
	  f055 number, f056 number, f057 number, f058 number, f059 number,
	  f060 number, f061 number, f062 number, f063 number, f064 number,
	  f065 number, f066 number, f067 number, f068 number, f069 number,
	  f070 number, f071 number, f072 number, f073 number, f074 number,
	  f075 number, f076 number, f077 number, f078 number, f079 number,
	  f080 number, f081 number, f082 number, f083 number, f084 number,
	  f085 number, f086 number, f087 number, f088 number, f089 number,
	  f090 number, f091 number, f092 number, f093 number, f094 number,
	  f095 number, f096 number, f097 number, f098 number, f099 number,

	  f100 number, f101 number, f102 number, f103 number, f104 number,
	  f105 number, f106 number, f107 number, f108 number, f109 number,
	  f110 number, f111 number, f112 number, f113 number, f114 number,
	  f115 number, f116 number, f117 number, f118 number, f119 number,
	  f120 number, f121 number, f122 number, f123 number, f124 number,
	  f125 number, f126 number, f127 number, f128 number, f129 number,
	  f130 number, f131 number, f132 number, f133 number, f134 number,
	  f135 number, f136 number, f137 number, f138 number, f139 number,
	  f140 number, f141 number, f142 number, f143 number, f144 number,
	  f145 number, f146 number, f147 number, f148 number, f149 number,
	  f150 number, f151 number, f152 number, f153 number, f154 number,
	  f155 number, f156 number, f157 number, f158 number, f159 number,
	  f160 number, f161 number, f162 number, f163 number, f164 number,
	  f165 number, f166 number, f167 number, f168 number, f169 number,
	  f170 number, f171 number, f172 number, f173 number, f174 number,
	  f175 number, f176 number, f177 number, f178 number, f179 number,
	  f180 number, f181 number, f182 number, f183 number, f184 number,
	  f185 number, f186 number, f187 number, f188 number, f189 number,
	  f190 number, f191 number, f192 number, f193 number, f194 number,
	  f195 number, f196 number, f197 number, f198 number, f199 number,

	  f200 number, f201 number, f202 number, f203 number, f204 number,
	  f205 number, f206 number, f207 number, f208 number, f209 number,
	  f210 number, f211 number, f212 number, f213 number, f214 number,
	  f215 number, f216 number, f217 number, f218 number, f219 number,
	  f220 number, f221 number, f222 number, f223 number, f224 number,
	  f225 number, f226 number, f227 number, f228 number, f229 number,
	  f230 number, f231 number, f232 number, f233 number, f234 number,
	  f235 number, f236 number, f237 number, f238 number, f239 number,
	  f240 number, f241 number, f242 number, f243 number, f244 number,
	  f245 number, f246 number, f247 number, f248 number, f249 number,
	  f250 number, f251 number, f252 number, f253 number, f254 number,
	  f255 number, f256 number, f257 number, f258 number, f259 number,
	  f260 number, f261 number, f262 number, f263 number, f264 number,
	  f265 number, f266 number, f267 number, f268 number, f269 number,
	  f270 number, f271 number, f272 number, f273 number, f274 number,
	  f275 number, f276 number, f277 number, f278 number, f279 number,
	  f280 number, f281 number, f282 number, f283 number, f284 number,
	  f285 number, f286 number, f287 number, f288 number, f289 number,
	  f290 number, f291 number, f292 number, f293 number, f294 number,
	  f295 number, f296 number, f297 number, f298 number, f299 number,

	  f300 number, f301 number, f302 number, f303 number, f304 number,
	  f305 number, f306 number, f307 number, f308 number, f309 number,
	  f310 number, f311 number, f312 number, f313 number, f314 number,
	  f315 number, f316 number, f317 number, f318 number, f319 number,
	  f320 number, f321 number, f322 number, f323 number, f324 number,
	  f325 number, f326 number, f327 number, f328 number, f329 number,
	  f330 number, f331 number, f332 number, f333 number, f334 number,
	  f335 number, f336 number, f337 number, f338 number, f339 number,
	  f340 number, f341 number, f342 number, f343 number, f344 number,
	  f345 number, f346 number, f347 number, f348 number, f349 number,
	  f350 number, f351 number, f352 number, f353 number, f354 number,
	  f355 number, f356 number, f357 number, f358 number, f359 number,
	  f360 number, f361 number, f362 number, f363 number, f364 number,
	  f365 number, f366 number, f367 number, f368 number, f369 number,
	  f370 number, f371 number, f372 number, f373 number, f374 number,
	  f375 number, f376 number, f377 number, f378 number, f379 number,
	  f380 number, f381 number, f382 number, f383 number, f384 number,
	  f385 number, f386 number, f387 number, f388 number, f389 number,
	  f390 number, f391 number, f392 number, f393 number, f394 number,
	  f395 number, f396 number, f397 number, f398 number, f399 number
	)
	return number
	IS
	BEGIN
		return null;
	END pkg_f_many_parameters;
END library;
/



DROP TABLE T_976/
DROP TYPE T_976_OBJECT_TYPE/
DROP TYPE T_976_VARRAY_TYPE/
DROP PROCEDURE P_976/
DROP FUNCTION F_976/
DROP PACKAGE PKG_976/
DROP PACKAGE PKG_1358/

CREATE OR REPLACE PACKAGE PKG_1358 AS
  PROCEDURE P (I VARCHAR2);
  PROCEDURE P (J VARCHAR2);
  PROCEDURE P (K VARCHAR2);
  PROCEDURE P2 (I VARCHAR2);
  PROCEDURE P2 (J VARCHAR2);
  PROCEDURE P3 (K VARCHAR2);
  PROCEDURE P21 (I VARCHAR2);
END PKG_1358;
/

CREATE PACKAGE PKG_976 AS
  PROCEDURE P_976 (I IN DATE, O OUT DATE);
  FUNCTION F_976 (I IN DATE) RETURN DATE;
END PKG_976;
/

CREATE PACKAGE BODY PKG_976 AS
  PROCEDURE P_976 (I IN DATE, O OUT DATE) IS
  BEGIN
	  O := I;
  END P_976;
  FUNCTION F_976 (I IN DATE) RETURN DATE IS
  BEGIN
	  RETURN I;
  END F_976;
END PKG_976;
/

CREATE PROCEDURE P_976 (I IN DATE, O OUT DATE) IS
BEGIN
  O := I;
END P_976;
/
CREATE FUNCTION F_976 (I IN DATE) RETURN DATE IS
BEGIN
  RETURN I;
END F_976;
/

CREATE TYPE T_976_VARRAY_TYPE AS VARRAY(10) OF DATE/
CREATE TYPE T_976_OBJECT_TYPE AS OBJECT(D DATE)/
CREATE TABLE T_976 (
  ID NUMBER(7),
  D DATE,
  T T_976_VARRAY_TYPE,
  O T_976_OBJECT_TYPE,

  CONSTRAINT pk_t_976 PRIMARY KEY (ID)
)/

create or replace synonym t_author_synonym for t_author/
create or replace synonym v_author_synonym for v_author/
create or replace synonym p_unused_synonym for p_unused/
create or replace synonym t_author_synonym_synonym for t_author_synonym/
create or replace public synonym t_author_public_synonym for t_author/
create or replace public synonym t_author for t_author/

CREATE INDEX i_book_title_context ON t_book(title) INDEXTYPE IS CTXSYS.CONTEXT/




DROP PACKAGE test.test_synonym_package/
DROP PACKAGE multi_schema.ms_synonym_package/
DROP PUBLIC SYNONYM number_table_public/
DROP PUBLIC SYNONYM number_object_public/
DROP SYNONYM number_table_test/
DROP SYNONYM number_object_test/
DROP PUBLIC SYNONYM transitive_synonym_2/
DROP SYNONYM transitive_synonym_1/
DROP TYPE multi_schema.number_table/
DROP TYPE multi_schema.number_object/

CREATE TYPE multi_schema.number_table AS VARRAY(100) OF NUMBER(7)/
CREATE TYPE multi_schema.number_object AS OBJECT(a NUMBER(7), b NUMBER(7), c NUMBER(7))/

CREATE SYNONYM test.number_table_test FOR multi_schema.number_table/
CREATE SYNONYM test.number_object_test FOR multi_schema.number_object/
CREATE PUBLIC SYNONYM number_table_public FOR multi_schema.number_table/
CREATE PUBLIC SYNONYM number_object_public FOR multi_schema.number_object/

CREATE SYNONYM transitive_synonym_1 FOR number_object_public/
CREATE PUBLIC SYNONYM transitive_synonym_2 FOR test.transitive_synonym_1/

CREATE PACKAGE multi_schema.ms_synonym_package AS
  FUNCTION actual_table RETURN multi_schema.number_table;
  FUNCTION public_table RETURN number_table_public;
  FUNCTION test_table RETURN test.number_table_test;

  FUNCTION actual_object RETURN multi_schema.number_object;
  FUNCTION public_object RETURN number_object_public;
  FUNCTION test_object RETURN test.number_object_test;

  FUNCTION test_transitive RETURN transitive_synonym_2;
END ms_synonym_package;
/
CREATE PACKAGE BODY multi_schema.ms_synonym_package AS
  FUNCTION actual_table RETURN multi_schema.number_table IS
  BEGIN
    RETURN number_table(1, 2, 3);
  END;

  FUNCTION public_table RETURN number_table_public IS
  BEGIN
    RETURN number_table(4, 5, 6);
  END;

  FUNCTION test_table RETURN test.number_table_test IS
  BEGIN
    RETURN number_table(7, 8, 9);
  END;


  FUNCTION actual_object RETURN multi_schema.number_object IS
  BEGIN
    RETURN number_object(1, 2, 3);
  END;

  FUNCTION public_object RETURN number_object_public IS
  BEGIN
    RETURN number_object(4, 5, 6);
  END;

  FUNCTION test_object RETURN test.number_object_test IS
  BEGIN
    RETURN number_object(7, 8, 9);
  END;

  FUNCTION test_transitive RETURN transitive_synonym_2 IS
  BEGIN
    RETURN number_object(7, 8, 9);
  END;
END ms_synonym_package;
/

CREATE PACKAGE test.test_synonym_package AS
  FUNCTION actual_table RETURN multi_schema.number_table;
  FUNCTION public_table RETURN number_table_public;
  FUNCTION test_table RETURN test.number_table_test;

  FUNCTION actual_object RETURN multi_schema.number_object;
  FUNCTION public_object RETURN number_object_public;
  FUNCTION test_object RETURN test.number_object_test;

  FUNCTION test_transitive RETURN transitive_synonym_2;
END test_synonym_package;
/
CREATE PACKAGE BODY test.test_synonym_package AS
  FUNCTION actual_table RETURN multi_schema.number_table IS
  BEGIN
    RETURN multi_schema.number_table(1, 2, 3);
  END;

  FUNCTION public_table RETURN number_table_public IS
  BEGIN
    RETURN multi_schema.number_table(4, 5, 6);
  END;

  FUNCTION test_table RETURN test.number_table_test IS
  BEGIN
    RETURN multi_schema.number_table(7, 8, 9);
  END;


  FUNCTION actual_object RETURN multi_schema.number_object IS
  BEGIN
    RETURN multi_schema.number_object(1, 2, 3);
  END;

  FUNCTION public_object RETURN number_object_public IS
  BEGIN
    RETURN multi_schema.number_object(4, 5, 6);
  END;

  FUNCTION test_object RETURN test.number_object_test IS
  BEGIN
    RETURN multi_schema.number_object(7, 8, 9);
  END;

  FUNCTION test_transitive RETURN transitive_synonym_2 IS
  BEGIN
    RETURN multi_schema.number_object(7, 8, 9);
  END;
END test_synonym_package;
/

CREATE TABLE test.t_3711 (
  v1 number_table_public,
  v2 number_object_public,
  v3 test.number_table_test,
  v4 test.number_object_test,
  v7 multi_schema.number_table,
  v8 multi_schema.number_object
)
/




DROP FUNCTION usr_2522.f_2522
/
DROP FUNCTION usr_2522_a.f_2522
/
DROP TABLE usr_2522.t_2522
/
DROP TABLE usr_2522_a.t_2522
/
DROP TYPE usr_2522.u_2522
/
DROP TYPE usr_2522_a.u_2522
/
CREATE TYPE usr_2522.u_2522 AS OBJECT (v NUMBER(7))
/
CREATE TYPE usr_2522_a.u_2522 AS OBJECT (v NUMBER(7))
/
CREATE TABLE usr_2522.t_2522 (id NUMBER(7), v usr_2522.u_2522)
/
CREATE TABLE usr_2522_a.t_2522 (id NUMBER(7), v usr_2522_a.u_2522)
/
CREATE FUNCTION usr_2522.f_2522 RETURN usr_2522.u_2522 IS
BEGIN
  RETURN usr_2522.u_2522(1);
END;
/
CREATE FUNCTION usr_2522_a.f_2522 RETURN usr_2522_a.u_2522 IS
BEGIN
  RETURN usr_2522_a.u_2522(2);
END;
/




CREATE PACKAGE pls_objects AS

  pls_i_c CONSTANT NUMBER(7) := 1;
  pls_v_c CONSTANT VARCHAR2(50) := 'A';

  pls_i NUMBER(7) := 1;
  pls_v VARCHAR2(50) := 1;

  TYPE pls_r_t IS RECORD (a NUMBER);
  TYPE pls_r_tt IS TABLE OF pls_r_t;

  PROCEDURE pls_p (i IN NUMBER, v IN VARCHAR2, io OUT NUMBER, vo OUT VARCHAR2);
  FUNCTION pls_f (i NUMBER, v VARCHAR2) RETURN NUMBER;
  PROCEDURE p_bool (i IN BOOLEAN, io IN OUT BOOLEAN, o OUT BOOLEAN);
  FUNCTION f_bool (i BOOLEAN) RETURN BOOLEAN;
END pls_objects;
/
CREATE PACKAGE BODY pls_objects AS
  PROCEDURE pls_p (i IN NUMBER, v IN VARCHAR2, io OUT NUMBER, vo OUT VARCHAR2) IS
  BEGIN
    io := i;
    vo := v;
  END pls_p;

  FUNCTION pls_f (i NUMBER, v VARCHAR2) RETURN NUMBER IS
  BEGIN
    RETURN i;
  END pls_f;

  PROCEDURE p_bool (i IN BOOLEAN, io IN OUT BOOLEAN, o OUT BOOLEAN) IS
  BEGIN
    o := io;
    io := i;
  END p_bool;

  FUNCTION f_bool (i BOOLEAN) RETURN BOOLEAN IS
  BEGIN
    RETURN i;
  END f_bool;
END pls_objects;
/


CREATE TABLE t_reserved(
  "abstract" INT,
  "case" INT,
  "catch" INT,
  "class" INT,
  "def" INT,
  "do" INT,
  "else" INT,
  "extends" INT,
  "false" INT,
  "final" INT,
  "finally" INT,
  "for" INT,
  "forSome" INT,
  "if" INT,
  "implicit" INT,
  "import" INT,
  "lazy" INT,
  "match" INT,
  "new" INT,
  "null" INT,
  "object" INT,
  "override" INT,
  "package" INT,
  "private" INT,
  "protected" INT,
  "return" INT,
  "sealed" INT,
  "super" INT,
  "this" INT,
  "throw" INT,
  "trait" INT,
  "try" INT,
  "true" INT,
  "type" INT,
  "val" INT,
  "var" INT,
  "while" INT,
  "with" INT,
  "yield" INT
)
/

CREATE TYPE multi_schema.u_4347 AS OBJECT (
  ID NUMBER(7),
  t test.u_address_table,
  o test.u_address_type
)
/

CREATE TYPE multi_schema.u_4347_table AS TABLE OF test.u_address_type
/