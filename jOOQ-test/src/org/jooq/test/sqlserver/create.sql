DROP VIEW v_library/
DROP VIEW v_author/
DROP VIEW v_book/

DROP TRIGGER t_triggers_trigger/

DROP TABLE t_triggers/
DROP TABLE t_arrays/
DROP TABLE t_book_to_book_store/
DROP TABLE t_book_store/
DROP TABLE t_book/
DROP TABLE t_book_details/
DROP TABLE t_author/
DROP TABLE t_language/
DROP TABLE x_test_case_71/
DROP TABLE x_test_case_64_69/
DROP TABLE x_test_case_85/
DROP TABLE x_unused/
DROP TABLE x_many_fields/
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

DROP PROCEDURE p_arrays1/
DROP PROCEDURE p_arrays2/
DROP PROCEDURE p_arrays3/
DROP PROCEDURE p_many_parameters/
DROP FUNCTION f_arrays1/
DROP FUNCTION f_arrays2/
DROP FUNCTION f_arrays3/
DROP PROCEDURE p_enhance_address1/
DROP PROCEDURE p_enhance_address2/
DROP PROCEDURE p_enhance_address3/
DROP PROCEDURE p_unused/
DROP PROCEDURE p_create_author/ 
DROP PROCEDURE p_create_author_by_name/ 
DROP PROCEDURE p_author_exists/
DROP PROCEDURE p391/
DROP FUNCTION f_many_parameters/
DROP FUNCTION f_author_exists/
DROP FUNCTION f_one/
DROP FUNCTION f_number/
DROP FUNCTION f317/
DROP FUNCTION f378/

DROP TYPE u_address_type/
DROP TYPE u_street_type/
DROP TYPE u_string_array/
DROP TYPE u_number_array/
DROP TYPE u_number_long_array/
DROP TYPE u_date_array/



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
  TITLE VARCHAR(400) NOT NULL,
  PUBLISHED_IN int NOT NULL,
  LANGUAGE_ID int NOT NULL,
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
  ID INTEGER IDENTITY(1,1) NOT NULL,
  NAME VARCHAR(400) NOT NULL,
  
  CONSTRAINT uk_t_book_store_name UNIQUE(name)
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
  CONSTRAINT fk_x_test_case_64_69 FOREIGN KEY(UNUSED_ID) REFERENCES X_UNUSED(ID)
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

CREATE FUNCTION f317 (@p1 int, @p2 int, @p3 int, @p4 int) RETURNS int AS
BEGIN
  return 1000 * @p1 + 100 * @p2 + @p4;
END;
/
