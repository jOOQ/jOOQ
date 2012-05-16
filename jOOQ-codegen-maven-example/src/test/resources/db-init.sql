CREATE TABLE t_author (
  ID INT,
  FIRST_NAME VARCHAR(50),
  LAST_NAME VARCHAR(50) NOT NULL,
  DATE_OF_BIRTH DATE,
  YEAR_OF_BIRTH INT,
  ADDRESS VARCHAR(50),

  CONSTRAINT pk_t_author PRIMARY KEY (ID)
);

CREATE SEQUENCE s_author_id START WITH 1;

INSERT INTO t_author (ID, FIRST_NAME, LAST_NAME) VALUES (next value for s_author_id, 'George', 'Orwell');
INSERT INTO t_author (ID, FIRST_NAME, LAST_NAME) VALUES (next value for s_author_id, 'Paulo', 'Coelho');
