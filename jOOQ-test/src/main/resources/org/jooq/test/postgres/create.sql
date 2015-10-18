DROP MATERIALIZED VIEW IF EXISTS m_library/
DROP MATERIALIZED VIEW IF EXISTS m_arrays/
DROP MATERIALIZED VIEW IF EXISTS m_639_numbers_table/
DROP VIEW IF EXISTS v_library/
DROP VIEW IF EXISTS v_author/
DROP VIEW IF EXISTS v_book/

DROP FUNCTION f_setof_void (arg BIGINT)/
DROP FUNCTION f_setof_bigint (arg BIGINT)/
DROP FUNCTION p_default (
  p_in_number IN INTEGER, p_out_number OUT INTEGER, 
  p_in_varchar IN  VARCHAR(50), p_out_varchar OUT VARCHAR(50),
  p_in_date IN DATE, p_out_date OUT DATE)/
DROP AGGREGATE second_max(INTEGER)/
DROP FUNCTION second_max_sfunc (state INTEGER[], data INTEGER)/
DROP FUNCTION second_max_ffunc (state INTEGER[])/
DROP FUNCTION f_tables1()/
DROP FUNCTION f_tables2()/
DROP FUNCTION f_tables3()/
DROP FUNCTION f_tables4(in_id INTEGER)/
DROP FUNCTION f_tables5 (v1 INTEGER, v2 INTEGER, v3 INTEGER)/
DROP FUNCTION f_array_tables(in_text IN text[], in_integer IN integer[])/
DROP FUNCTION f_arrays(in_array IN integer[])/
DROP FUNCTION f_arrays(in_array IN bigint[])/
DROP FUNCTION f_arrays(in_array IN text[])/
DROP FUNCTION p_arrays(in_array IN integer[], out_array OUT integer[])/
DROP FUNCTION p_arrays(in_array IN bigint[], out_array OUT bigint[])/
DROP FUNCTION p_arrays(in_array IN text[], out_array OUT text[])/
DROP FUNCTION p_enhance_address1(address IN u_address_type, no OUT VARCHAR)/
DROP FUNCTION p_enhance_address2(address OUT u_address_type)/
DROP FUNCTION p_enhance_address3(address IN OUT u_address_type)/
DROP FUNCTION p_unused(in1 VARCHAR, out1 OUT INTEGER, out2 IN OUT INTEGER)/
DROP FUNCTION p_create_author()/
DROP FUNCTION p_create_author_by_name(first_name VARCHAR, last_name VARCHAR)/
DROP FUNCTION p_author_exists(author_name VARCHAR, result OUT INTEGER)/
DROP FUNCTION p391(
	i1 INTEGER, io1 IN OUT INTEGER, o1 OUT INTEGER,
	o2 OUT INTEGER, io2 IN OUT INTEGER, i2 INTEGER)
/
DROP FUNCTION f_author_exists(author_name VARCHAR);/
DROP FUNCTION f_one();/
DROP FUNCTION f(f int, f_ int);/
DROP FUNCTION p(p int, p_ int);/
DROP FUNCTION f_number(n int);/
DROP FUNCTION f317(p1 int, p2 int, p3 int, p4 int);/
DROP FUNCTION p_get_two_cursors(books OUT refcursor, authors OUT refcursor)/
DROP FUNCTION p_get_one_cursor(total OUT int, books OUT refcursor, book_ids in int[])/
DROP FUNCTION f_get_one_cursor(book_ids IN int[])/
DROP FUNCTION f_search_book_ids(p_title character varying, p_limit bigint, p_offset bigint)/
DROP FUNCTION f_search_book_titles(p_title character varying, p_limit bigint, p_offset bigint)/
DROP FUNCTION f_search_books(p_title character varying, p_limit bigint, p_offset bigint)/
DROP FUNCTION f_search_book(p_title character varying)/
DROP FUNCTION f_get_arrays(p_id integer)/
DROP FUNCTION f4430(p1 int, p2 bigint)/
DROP FUNCTION f_dates(
  d IN OUT date,
  t IN OUT time,
  ts IN OUT timestamp,
  t_tz IN OUT time with time zone,
  ts_tz IN OUT timestamp with time zone)/

DROP TRIGGER IF EXISTS t_triggers_trigger ON t_triggers/
DROP FUNCTION p_triggers()/

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
DROP TABLE IF EXISTS t_exotic_types/
DROP TABLE IF EXISTS t_986_1/
DROP TABLE IF EXISTS t_986_2/
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
DROP TABLE IF EXISTS t_785/
DROP TABLE IF EXISTS t_959/
DROP TABLE IF EXISTS t_unsigned/
DROP TABLE IF EXISTS t_booleans/
DROP TABLE IF EXISTS t_identity/
DROP TABLE IF EXISTS t_identity_pk/
DROP TABLE IF EXISTS t_pg_extensions/
DROP TABLE IF EXISTS t_inheritance_all/
DROP TABLE IF EXISTS t_inheritance_1_2_1/
DROP TABLE IF EXISTS t_inheritance_1_2/
DROP TABLE IF EXISTS t_inheritance_1_1/
DROP TABLE IF EXISTS t_inheritance_1/
DROP TABLE IF EXISTS t_2781/
DROP TABLE IF EXISTS t_3111/
DROP TABLE IF EXISTS t_681/

DROP TYPE IF EXISTS u_address_type/
DROP TYPE IF EXISTS u_street_type/
DROP TYPE IF EXISTS u_book_status/
DROP TYPE IF EXISTS u_country/
DROP TYPE IF EXISTS u_959/
DROP TYPE IF EXISTS u_2781/
DROP TYPE IF EXISTS u_uuids/

DROP DOMAIN IF EXISTS d_email/
DROP DOMAIN IF EXISTS d_year/
DROP DOMAIN IF EXISTS d_recursive_int_3/
DROP DOMAIN IF EXISTS d_recursive_int_2/
DROP DOMAIN IF EXISTS d_recursive_int_1/

CREATE DOMAIN d_recursive_int_1 AS int CHECK (VALUE > 0)/
CREATE DOMAIN d_recursive_int_2 AS d_recursive_int_1 CHECK (VALUE > 10)/
CREATE DOMAIN d_recursive_int_3 AS d_recursive_int_2 CHECK (VALUE > 20)/
CREATE DOMAIN d_year AS int CHECK (VALUE BETWEEN 1900 AND 2050)/
CREATE DOMAIN d_email AS text CHECK (VALUE ~ '\w+@\w+\.\w+')/

CREATE TYPE u_uuids AS (
  u1 uuid,
  u2 uuid[]
)
/

CREATE TYPE u_959 AS ENUM('abstract', 'assert', 'boolean', 'break', 'byte', 'case', 'catch',
	                 'char', 'class', 'const', 'continue', 'default', 'double', 'do',
	                 'else', 'enum', 'extends', 'false', 'final', 'finally', 'float',
	                 'for', 'goto', 'if', 'implements', 'import', 'instanceof',
	                 'interface', 'int', 'long', 'native', 'new', 'package', 'private',
	                 'protected', 'public', 'return', 'short', 'static', 'strictfp',
	                 'super', 'switch', 'synchronized', 'this', 'throw', 'throws',
	                 'transient', 'true', 'try', 'void', 'volatile', 'while')/
CREATE TYPE u_2781 AS ENUM('org', 'jooq')/
CREATE TYPE u_book_status AS ENUM ('SOLD OUT', 'ON STOCK', 'ORDERED')/
CREATE TYPE u_country AS ENUM ('Brazil', 'England', 'Germany')/

CREATE TYPE u_street_type AS (
  street VARCHAR(100),
  no VARCHAR(30),
  floors integer[],
  f_1323 bytea
)
/

CREATE TYPE u_address_type AS (
  street u_street_type,
  zip VARCHAR(50),
  city VARCHAR(50),
  country u_country,
  since DATE,
  code INTEGER,
  f_1323 bytea
)
/

CREATE TABLE t_681 (
  id int,
  year d_year,
  email d_email,
  
  CONSTRAINT pk_t_681 PRIMARY KEY (id)
)
/

CREATE TABLE t_3111 (
  id int,
  inverse int,
  bool1 boolean,
  bool2 boolean,

  CONSTRAINT pk_t_3111 PRIMARY KEY (id)
)
/

CREATE TABLE t_2781 (
  org text,
  jooq text
)
/

CREATE TABLE t_inheritance_1 (
  text_1 text
)
/

CREATE TABLE t_inheritance_1_1 (
  text_1_1 text
) INHERITS (t_inheritance_1)
/

CREATE TABLE t_inheritance_1_2 (
  text_1_2 text
) INHERITS (t_inheritance_1)
/

CREATE TABLE t_inheritance_1_2_1 (
  text_1_2_1 text
) INHERITS (t_inheritance_1_2)
/

CREATE TABLE t_inheritance_all (
  text_1_all text
) INHERITS (t_inheritance_1_1, t_inheritance_1_2)
/

CREATE TABLE t_pg_extensions (
  id serial not null,
  pg_interval interval,
  pg_box box,
  pg_hstore hstore,
  pg_geometry geometry(Point, 4326, 2),
  pg_position geography(Point, 4326), 
  
  CONSTRAINT pk_t_pg_extensions PRIMARY KEY (id)
)/

CREATE TABLE t_identity_pk (
  id serial not null,
  val int,

  CONSTRAINT pk_t_identity_pk PRIMARY KEY (id)
)
/

CREATE TABLE t_identity (
  id serial not null,
  val int
)
/

CREATE TABLE t_dates (
  id int,
  d date,
  t time,
  ts timestamp,
  t_tz time with time zone,
  ts_tz timestamp with time zone,
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

CREATE TABLE t_959 (
  java_keywords u_959
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
  id_generated serial4 not null,
  id int,
  counter int,

  CONSTRAINT pk_t_triggers PRIMARY KEY (id_generated)
)
/

CREATE FUNCTION p_triggers ()
RETURNS trigger
AS $$
BEGIN
	new.id = new.id_generated;
	new.counter = new.id_generated * 2;

    return new;
END
$$ LANGUAGE plpgsql;
/

CREATE TRIGGER t_triggers_trigger
BEFORE INSERT
ON t_triggers
FOR EACH ROW
EXECUTE PROCEDURE p_triggers()
/

CREATE TABLE t_language (
  cd CHAR(2) NOT NULL,
  description VARCHAR(50),
  description_english VARCHAR(50),
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


CREATE TABLE t_725_lob_test (
  ID int NOT NULL,
  LOB BYTEA NULL,

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
  id INTEGER NOT NULL,
  first_name VARCHAR(50),
  last_name VARCHAR(50) NOT NULL,
  date_of_birth DATE,
  year_of_birth INTEGER,
  address u_address_type,

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
  ID INT,

  CONSTRAINT pk_t_book_details PRIMARY KEY (ID)
)
/
COMMENT ON TABLE t_book_details IS 'An unused details table'
/

CREATE TABLE t_book (
  id INTEGER NOT NULL,
  author_id INTEGER NOT NULL,
  co_author_id INTEGER,
  details_id INT,
  title VARCHAR(400) NOT NULL,
  published_in INTEGER NOT NULL,
  language_id INTEGER NOT NULL DEFAULT 1,
  content_text TEXT,
  content_pdf BYTEA,
  status u_book_status,

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
COMMENT ON COLUMN t_book.status IS 'The book''s stock status'
/


CREATE TABLE t_book_store (
  name VARCHAR(400) NOT NULL,

  CONSTRAINT uk_t_book_store_name PRIMARY KEY(name)
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
/
COMMENT ON TABLE t_book_to_book_store IS 'An m:n relation between books and book stores'
/
COMMENT ON COLUMN t_book_to_book_store.book_store_name IS 'The book store name'
/
COMMENT ON COLUMN t_book_to_book_store.book_id IS 'The book ID'
/
COMMENT ON COLUMN t_book_to_book_store.stock IS 'The number of books on stock'
/


CREATE TABLE t_arrays (
  id integer not null,
  string_array VARCHAR(20)[],
  number_array INTEGER[],
  date_array DATE[],
  udt_array u_street_type[],
  address_array u_address_type[],
  enum_array u_country[],
  array_array INTEGER[][],
  number_list VARCHAR(20)[],
  string_list VARCHAR(20)[],
  date_list VARCHAR(20)[],

  CONSTRAINT pk_t_arrays PRIMARY KEY (ID)
)
/

CREATE TABLE x_unused (
  id INTEGER NOT NULL,
  name VARCHAR(10) NOT NULL,
  BIG_INTEGER DECIMAL(25),
  id_ref INTEGER,
  CLASS INT,
  FIELDS INT,
  CONFIGURATION INT,
  U_D_T INT,
  META_DATA INT,
  VALUES INT,
  TYPE0 INT,
  PRIMARY_KEY INT,
  PRIMARYKEY INT,
  name_ref VARCHAR(10),
  "FIELD 737" DECIMAL(25, 2),

  CONSTRAINT pk_x_unused PRIMARY KEY(ID, NAME),
  CONSTRAINT uk_x_unused_id UNIQUE(ID),
  CONSTRAINT fk_x_unused_self FOREIGN KEY(ID_REF, NAME_REF) REFERENCES X_UNUSED(ID, NAME)
)
/
COMMENT ON TABLE x_unused IS 'An unused table in the same schema.'
/

CREATE TABLE t_986_1 (
  REF INT,

  CONSTRAINT fk_986 FOREIGN KEY(REF) REFERENCES X_UNUSED(ID)
)
/

CREATE TABLE t_986_2 (
  REF INT,

  CONSTRAINT fk_986 FOREIGN KEY(REF) REFERENCES X_UNUSED(ID)
)
/

CREATE TABLE t_exotic_types (
  ID                     INT  NOT NULL,
  UU                     UUID,
  UU_ARRAY               UUID[],
  
  UU_WRAPPER             UUID,
  UU_WRAPPER_ARRAY       UUID[],
  
  JS                     JSON,
  JS_GSON                JSON,
  JS_JAVAX               JSON,
  JS_JACKSON             JSON,
  JS_JACKSON_JSON_NODE   JSON,
  
  HSTORE                 HSTORE,
  HSTORE_MAP             HSTORE,
  
  RANGE_INT4             INT4RANGE,
  RANGE_INT8             INT8RANGE,
  
  PG_XML_AS_IS           XML,
  PG_XML_AS_DOCUMENT     XML,

  CONSTRAINT pk_t_exotic_types PRIMARY KEY(ID)
)
/

CREATE TABLE t_639_numbers_table (
  ID INT NOT NULL,
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
  DOUBLE DOUBLE PRECISION,

  CONSTRAINT pk_t_639_numbers_table PRIMARY KEY(ID)
);
/


CREATE TABLE x_test_case_64_69 (
  id INTEGER NOT NULL,
  unused_id INTEGER,

  CONSTRAINT pk_x_test_case_64_69 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_64_69a FOREIGN KEY(UNUSED_ID) REFERENCES X_UNUSED(ID),
  CONSTRAINT fk_x_test_case_64_69b FOREIGN KEY(UNUSED_ID) REFERENCES X_UNUSED(ID)
)
/

CREATE TABLE x_test_case_71 (
  id INTEGER NOT NULL,
  test_case_64_69_id SMALLINT,

  CONSTRAINT pk_x_test_case_71 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_71 FOREIGN KEY(TEST_CASE_64_69_ID) REFERENCES X_TEST_CASE_64_69(ID)
)
/

CREATE TABLE x_test_case_85 (
  id INTEGER NOT NULL,
  x_unused_id INTEGER,
  x_unused_name VARCHAR(10),

  CONSTRAINT pk_x_test_case_85 PRIMARY KEY(ID),
  CONSTRAINT fk_x_test_case_85 FOREIGN KEY(x_unused_id, x_unused_name) REFERENCES X_UNUSED(id, name)
)
/

CREATE TABLE x_test_case_2025 (
  ref_id INTEGER NOT NULL,
  ref_name VARCHAR(10) NOT NULL,

  CONSTRAINT fk_x_test_case_2025_1 FOREIGN KEY(ref_id) REFERENCES x_test_case_85(ID),
  CONSTRAINT fk_x_test_case_2025_2 FOREIGN KEY(ref_id) REFERENCES x_test_case_71(ID),
  CONSTRAINT fk_x_test_case_2025_3 FOREIGN KEY(ref_id, ref_name) REFERENCES X_UNUSED(id, name)
)
/

CREATE OR REPLACE VIEW v_library (author, title) AS
SELECT a.first_name || ' ' || a.last_name, b.title
FROM t_author a JOIN t_book b ON b.author_id = a.id
/

CREATE MATERIALIZED VIEW m_library AS
SELECT * FROM v_library
/

CREATE MATERIALIZED VIEW m_arrays AS
SELECT * FROM t_arrays
/

CREATE MATERIALIZED VIEW m_639_numbers_table AS
SELECT * FROM t_639_numbers_table
/

CREATE VIEW v_author AS
SELECT * FROM t_author
/

CREATE VIEW v_book AS
SELECT * FROM t_book
/

CREATE OR REPLACE FUNCTION f_search_book_ids(p_title character varying, p_limit bigint, p_offset bigint)
  RETURNS SETOF INT AS
$BODY$
SELECT id FROM t_book
WHERE (LOWER(title) LIKE LOWER('%' || $1 || '%'))
LIMIT $2 OFFSET $3;
$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
/

CREATE OR REPLACE FUNCTION f_search_book_titles(p_title character varying, p_limit bigint, p_offset bigint)
  RETURNS SETOF VARCHAR(50) AS
$BODY$
SELECT title FROM t_book
WHERE (LOWER(title) LIKE LOWER('%' || $1 || '%'))
LIMIT $2 OFFSET $3;
$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
/

CREATE OR REPLACE FUNCTION f_search_books(p_title character varying, p_limit bigint, p_offset bigint)
  RETURNS SETOF t_book AS
$BODY$
SELECT * FROM t_book
WHERE (LOWER(title) LIKE LOWER('%' || $1 || '%'))
LIMIT $2 OFFSET $3;
$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
/

CREATE OR REPLACE FUNCTION f_search_book(p_title character varying)
  RETURNS t_book AS
$BODY$
SELECT * FROM t_book
WHERE (LOWER(title) LIKE LOWER('%' || $1 || '%'))
LIMIT 1;
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
/

CREATE OR REPLACE FUNCTION f_get_arrays(p_id integer)
  RETURNS SETOF t_arrays AS
$BODY$
SELECT * FROM t_arrays
WHERE p_id IS NULL OR p_id = id
ORDER BY id;
$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
/

CREATE FUNCTION p_unused (in1 VARCHAR, out1 OUT INT, out2 IN OUT INT)
AS $$
BEGIN
	NULL;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION p_enhance_address1 (address IN u_address_type, no OUT VARCHAR)
AS $$
BEGIN
	no := $1.street.no;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION p_enhance_address2 (address OUT u_address_type)
AS $$
BEGIN
	address := (
		SELECT t_author.address
		FROM t_author
		WHERE first_name = 'George'
	);
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION p_enhance_address3 (address IN OUT u_address_type)
AS $$
BEGIN
	address.street := row('Zwinglistrasse', '17');
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION p_create_author_by_name (first_name VARCHAR, last_name VARCHAR)
RETURNS VOID
AS $$
BEGIN
	INSERT INTO T_AUTHOR (ID, FIRST_NAME, LAST_NAME)
	VALUES ((SELECT MAX(ID)+1 FROM T_AUTHOR), first_name, last_name);
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION p_create_author()
RETURNS VOID
AS $$
BEGIN
	PERFORM {jdbc.Schema}.p_create_author_by_name('William', 'Shakespeare');
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION p_author_exists (author_name VARCHAR, result OUT INTEGER)
AS $$
DECLARE
  v_result INT;
BEGIN
  SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
    INTO v_result
    FROM t_author
   WHERE first_name LIKE author_name
      OR last_name LIKE author_name;

  result := v_result;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION p_arrays(in_array IN integer[], out_array OUT integer[])
AS $$
BEGIN
	out_array := in_array;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION p_arrays(in_array IN bigint[], out_array OUT bigint[])
AS $$
BEGIN
	out_array := in_array;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION p_arrays(in_array IN text[], out_array OUT text[])
AS $$
BEGIN
	out_array := in_array;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION p391 (
	i1 INTEGER, io1 IN OUT INTEGER, o1 OUT INTEGER,
	o2 OUT INTEGER, io2 IN OUT INTEGER, i2 INTEGER)
AS $$
BEGIN
  o1 := io1;
  io1 := i1;

  o2 := io2;
  io2 := i2;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION f_arrays(in_array IN integer[])
RETURNS integer[]
AS $$
BEGIN
	return in_array;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION f_arrays(in_array IN bigint[])
RETURNS bigint[]
AS $$
BEGIN
	return in_array;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION f_arrays(in_array IN text[])
RETURNS text[]
AS $$
BEGIN
	return in_array;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION f_tables1 ()
RETURNS TABLE (
    column_value INTEGER
)
AS $$
BEGIN
    RETURN QUERY
    SELECT 1;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION f_tables2 ()
RETURNS TABLE (
    column_value BIGINT
)
AS $$
BEGIN
    RETURN QUERY
    SELECT CAST(1 AS BIGINT);
END
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION f_tables3 ()
RETURNS TABLE (
    column_value VARCHAR(5)
)
AS $$
BEGIN
    RETURN QUERY
    SELECT CAST('1' AS VARCHAR(5));
END
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION f_tables4 (in_id INTEGER)
RETURNS TABLE (
    id INTEGER,
    title VARCHAR(400)
)
AS $$
BEGIN
    RETURN QUERY
    SELECT b.id, b.title
    FROM t_book b
    WHERE in_id IS NULL OR b.id = in_id
    ORDER BY b.id;
END
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION f_tables5 (v1 INTEGER, v2 INTEGER, v3 INTEGER)
RETURNS TABLE (
    v INTEGER,
    s INTEGER
)
AS $$
BEGIN
    RETURN QUERY
    SELECT *
    FROM (VALUES(v1, v1),
                (v2, v1 + v2),
                (v3, v1 + v2 + v3)) t(a, b);
END
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION f_array_tables(in_text IN text[], in_integer IN integer[])
RETURNS TABLE (
    out_text text[],
    out_integer integer[]
)
AS $$
BEGIN
    out_text := NULL;
    out_integer := NULL;
    RETURN NEXT;
    
    out_text := ARRAY[]::text[];
    out_integer := ARRAY[]::integer[];
    RETURN NEXT;
    
    out_text := ARRAY['a'];
    out_integer := ARRAY[1];
    RETURN NEXT;
    
    out_text := ARRAY['a', 'b'];
    out_integer := ARRAY[1, 2];
    RETURN NEXT;
END
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION f_author_exists (author_name VARCHAR)
RETURNS INT
AS $$
DECLARE
	v_result INT;
BEGIN
  SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END
    INTO v_result
    FROM t_author
   WHERE first_name LIKE author_name
      OR last_name LIKE author_name;

  return v_result;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION f (f int, f_ int)
RETURNS INT
AS $$
BEGIN
    RETURN f;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION p (p int, p_ int)
RETURNS VOID
AS $$
BEGIN
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION f_one ()
RETURNS INT
AS $$
BEGIN
    RETURN 1;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION f_number (n int)
RETURNS INT
AS $$
BEGIN
	RETURN n;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION f317 (p1 int, p2 int, p3 int, p4 int)
RETURNS INT
AS $$
BEGIN
	RETURN 1000 * p1 + 100 * p2 + p4;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION p_get_two_cursors (
	books   OUT refcursor,
	authors OUT refcursor)
AS $$
BEGIN
	OPEN books   FOR SELECT * FROM t_book ORDER BY id ASC;
	OPEN authors FOR SELECT * FROM t_author ORDER BY id ASC;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION p_get_one_cursor (
    total   OUT int,
	books   OUT refcursor,
	book_ids IN int[])
AS $$
BEGIN
	OPEN books FOR SELECT * FROM t_book WHERE id IN (SELECT * FROM UNNEST(book_ids)) ORDER BY id ASC;
	SELECT count(*) INTO total FROM t_book WHERE id IN (SELECT * FROM UNNEST(book_ids));
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION f_get_one_cursor (book_ids int[])
RETURNS refcursor
AS $$
DECLARE
    ref refcursor;
BEGIN
	IF (book_ids IS NULL) THEN
	    OPEN ref FOR SELECT * FROM t_book WHERE 1 = 0;
	ELSE
		OPEN ref FOR SELECT * FROM t_book WHERE id IN (SELECT * FROM UNNEST(book_ids)) ORDER BY id ASC;
	END IF;

	RETURN ref;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION second_max_sfunc (state INTEGER[], data INTEGER) RETURNS INTEGER[]
AS
$$
BEGIN
    IF state IS NULL THEN
        RETURN ARRAY[data, NULL];
    ELSE
        RETURN CASE WHEN state[1] > data
                    THEN CASE WHEN state[2] > data
                              THEN state
                              ELSE ARRAY[state[1], data]
                         END
                    ELSE ARRAY[data, state[1]]
               END;
    END IF;
END;
$$ LANGUAGE plpgsql;
/

CREATE FUNCTION second_max_ffunc (state INTEGER[]) RETURNS INTEGER
AS
$$
BEGIN
    RETURN state[2];
END;
$$ LANGUAGE plpgsql;
/

CREATE AGGREGATE second_max (INTEGER) (
    SFUNC     = second_max_sfunc,
    STYPE     = INTEGER[],
    FINALFUNC = second_max_ffunc
);
/

CREATE FUNCTION p_default (
  p_in_number   IN  INTEGER     = 0,
  p_out_number  OUT INTEGER,
  p_in_varchar  IN  VARCHAR(50) = '0',
  p_out_varchar OUT VARCHAR(50),
  p_in_date     IN  DATE        = date '1981-07-10',
  p_out_date    OUT DATE
)
AS
$$
BEGIN
    p_out_number := p_in_number;
    p_out_varchar := p_in_varchar;
    p_out_date := p_in_date;
END;
$$ LANGUAGE plpgsql;
/

CREATE OR REPLACE FUNCTION f_setof_void (arg BIGINT) RETURNS SETOF VOID AS $$
BEGIN
  RETURN;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION f_setof_bigint (arg BIGINT) RETURNS SETOF BIGINT AS $$
BEGIN
  RETURN QUERY SELECT COUNT(*) FROM information_schema.tables;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION f4430(p1 SMALLINT, p2 INT) RETURNS BIGINT AS $$
BEGIN
  RETURN p1 + p2;
END;
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION f_dates(
  d IN OUT date,
  t IN OUT time,
  ts IN OUT timestamp,
  t_tz IN OUT time with time zone,
  ts_tz IN OUT timestamp with time zone)
AS $$
BEGIN
  d := d;
  t := t;
  ts := ts;
  t_tz := t_tz;
  ts_tz := ts_tz;
END;
$$ LANGUAGE plpgsql;
  