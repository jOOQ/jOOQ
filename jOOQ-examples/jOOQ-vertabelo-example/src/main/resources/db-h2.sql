DROP TABLE IF EXISTS PUBLIC.AUTHOR;
DROP TABLE IF EXISTS PUBLIC.BOOK;
DROP TABLE IF EXISTS PUBLIC.BOOK_STORE;
DROP TABLE IF EXISTS PUBLIC.BOOK_TO_BOOK_STORE;
DROP TABLE IF EXISTS PUBLIC.LANGUAGE;


-- Created by Vertabelo (http://vertabelo.com)
-- Script type: create
-- Scope: [tables, references, sequences, views, procedures]
-- Generated at Mon Sep 01 13:40:41 UTC 2014






-- tables
-- Table: AUTHOR
CREATE TABLE PUBLIC.AUTHOR (
    ID integer  NOT NULL,
    FIRST_NAME varchar(50)  NULL,
    LAST_NAME varchar(50)  NOT NULL,
    DATE_OF_BIRTH date  NULL,
    YEAR_OF_BIRTH integer  NOT NULL,
    PRIMARY KEY (ID)
);
-- Table: BOOK
CREATE TABLE PUBLIC.BOOK (
    ID integer  NOT NULL,
    AUTHOR_ID integer  NOT NULL,
    TITLE varchar(400)  NOT NULL,
    PUBLISHED_IN integer  NOT NULL,
    LANGUAGE_ID integer  NOT NULL,
    PRIMARY KEY (ID)
);
-- Table: BOOK_STORE
CREATE TABLE PUBLIC.BOOK_STORE (
    NAME varchar(400)  NOT NULL,
    CONSTRAINT uk_book_store_name UNIQUE (NAME)
);
-- Table: BOOK_TO_BOOK_STORE
CREATE TABLE PUBLIC.BOOK_TO_BOOK_STORE (
    NAME varchar(400)  NOT NULL,
    BOOK_ID integer  NOT NULL,
    STOCK integer  NOT NULL,
    PRIMARY KEY (NAME,BOOK_ID)
);
-- Table: "LANGUAGE"
CREATE TABLE PUBLIC."LANGUAGE" (
    ID integer  NOT NULL,
    CD char(2)  NOT NULL,
    DESCRIPTION varchar(50)  NULL,
    PRIMARY KEY (ID)
);




-- foreign keys
-- Reference:  fk_b2bs_book (table: PUBLIC.BOOK_TO_BOOK_STORE)


ALTER TABLE PUBLIC.BOOK_TO_BOOK_STORE ADD CONSTRAINT fk_b2bs_book
    FOREIGN KEY (BOOK_ID)
    REFERENCES PUBLIC.BOOK (ID)
;

-- Reference:  fk_b2bs_book_store (table: PUBLIC.BOOK_TO_BOOK_STORE)


ALTER TABLE PUBLIC.BOOK_TO_BOOK_STORE ADD CONSTRAINT fk_b2bs_book_store
    FOREIGN KEY (NAME)
    REFERENCES PUBLIC.BOOK_STORE (NAME)
;

-- Reference:  fk_book_author (table: PUBLIC.BOOK)


ALTER TABLE PUBLIC.BOOK ADD CONSTRAINT fk_book_author
    FOREIGN KEY (AUTHOR_ID)
    REFERENCES PUBLIC.AUTHOR (ID)
;

-- Reference:  fk_book_language (table: PUBLIC.BOOK)


ALTER TABLE PUBLIC.BOOK ADD CONSTRAINT fk_book_language
    FOREIGN KEY (LANGUAGE_ID)
    REFERENCES PUBLIC."LANGUAGE" (ID)
;





-- End of file.




INSERT INTO author VALUES (1, 'George', 'Orwell', '1903-06-25', 1903);
INSERT INTO author VALUES (2, 'Paulo', 'Coelho', '1947-08-24', 1947);

INSERT INTO language VALUES (1, 'EN', 'English');
INSERT INTO language VALUES (2, 'DE', 'German');
INSERT INTO language VALUES (3, 'FR', 'French');
INSERT INTO language VALUES (4, 'PT', 'Portuguese');

INSERT INTO book VALUES (1, 1, '1984', 1948, 1);
INSERT INTO book VALUES (2, 1, 'Animal Farm', 1945, 1);
INSERT INTO book VALUES (3, 2, 'O Alquimista', 1988, 4);
INSERT INTO book VALUES (4, 2, 'Brida', 1990, 2);

INSERT INTO book_store (name) VALUES
    ('Orell Füssli'),
    ('Ex Libris'),
    ('Buchhandlung im Volkshaus');

INSERT INTO book_to_book_store VALUES
    ('Orell Füssli', 1, 10),
    ('Orell Füssli', 2, 10),
    ('Orell Füssli', 3, 10),
    ('Ex Libris', 1, 1),
    ('Ex Libris', 3, 2),
    ('Buchhandlung im Volkshaus', 3, 1);