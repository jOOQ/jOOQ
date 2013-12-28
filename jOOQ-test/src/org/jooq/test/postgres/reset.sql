DELETE FROM t_exotic_types/
DELETE FROM t_639_numbers_table/
DELETE FROM t_arrays/
DELETE FROM t_book_to_book_store/
DELETE FROM t_book_store/
DELETE FROM t_book/
DELETE FROM t_author/
DELETE FROM t_language/
DELETE FROM t_booleans/
DELETE FROM t_dates/
DELETE FROM t_identity/
DELETE FROM t_identity_pk/
DELETE FROM t_triggers/
DELETE FROM t_inheritance_all/
DELETE FROM t_inheritance_1_2_1/
DELETE FROM t_inheritance_1_2/
DELETE FROM t_inheritance_1_1/
DELETE FROM t_inheritance_1/

DROP SEQUENCE IF EXISTS s_author_id/
CREATE SEQUENCE s_author_id/

INSERT INTO t_language (id, cd, description, description_english) VALUES (1, 'en', 'English', 'English')/
INSERT INTO t_language (id, cd, description, description_english) VALUES (2, 'de', 'Deutsch', 'German')/
INSERT INTO t_language (id, cd, description, description_english) VALUES (3, 'fr', 'Français', 'French')/
INSERT INTO t_language (id, cd, description, description_english) VALUES (4, 'pt', null, null)/
/


INSERT INTO t_author VALUES (nextval('s_author_id'), 'George', 'Orwell', TO_DATE('1903-06-25', 'YYYY-MM-DD'), 1903, ROW(ROW('Parliament Hill', '77', '{1, 2, 3}', E'\\160\\160'::bytea), 'NW31A9', 'Hampstead', 'England', '1980-01-01', null, E'\\161\\161'::bytea))
/
INSERT INTO t_author VALUES (nextval('s_author_id'), 'Paulo', 'Coelho', TO_DATE('1947-08-24', 'YYYY-MM-DD'), 1947, ROW(ROW('Caixa Postal', '43.003', null, null), null, 'Rio de Janeiro', 'Brazil', '1940-01-01', 2, null))
/

INSERT INTO t_book VALUES (1, 1, null, null, '1984', 1948, 1, 'To know and not to know, to be conscious of complete truthfulness while telling carefully constructed lies, to hold simultaneously two opinions which cancelled out, knowing them to be contradictory and believing in both of them, to use logic against logic, to repudiate morality while laying claim to it, to believe that democracy was impossible and that the Party was the guardian of democracy, to forget, whatever it was necessary to forget, then to draw it back into memory again at the moment when it was needed, and then promptly to forget it again, and above all, to apply the same process to the process itself -- that was the ultimate subtlety; consciously to induce unconsciousness, and then, once again, to become unconscious of the act of hypnosis you had just performed. Even to understand the word ''doublethink'' involved the use of doublethink..', null, 'ORDERED')
/
INSERT INTO t_book VALUES (2, 1, null, null, 'Animal Farm', 1945, 1, null, null, 'ON STOCK')
/
INSERT INTO t_book VALUES (3, 2, null, null, 'O Alquimista', 1988, 4, null, null, 'ON STOCK')
/
INSERT INTO t_book VALUES (4, 2, null, null, 'Brida', 1990, 2, null, null, 'SOLD OUT')
/

INSERT INTO t_book_store (name)
VALUES ('Orell Füssli'),
       ('Ex Libris'),
       ('Buchhandlung im Volkshaus')
/

INSERT INTO t_book_to_book_store VALUES ('Orell Füssli', 1, 10)/
INSERT INTO t_book_to_book_store VALUES ('Orell Füssli', 2, 10)/
INSERT INTO t_book_to_book_store VALUES ('Orell Füssli', 3, 10)/
INSERT INTO t_book_to_book_store VALUES ('Ex Libris', 1, 1)/
INSERT INTO t_book_to_book_store VALUES ('Ex Libris', 3, 2)/
INSERT INTO t_book_to_book_store VALUES ('Buchhandlung im Volkshaus', 3, 1)/

INSERT INTO t_arrays VALUES (1, null, null, null, null, null, null)/
INSERT INTO t_arrays VALUES (2, '{}', '{}', '{}', '{}', '{}', '{}')/
INSERT INTO t_arrays VALUES (3, '{"a"}', '{1}', ARRAY[TO_DATE('1981-07-10', 'YYYY-MM-DD')], ARRAY[ROW('Downing Street', '10', null, E'\\x6969')]::u_street_type[], '{"England"}', ARRAY[ARRAY[1]])/
INSERT INTO t_arrays VALUES (4, '{"a", "b"}', '{1, 2}', ARRAY[TO_DATE('1981-07-10', 'YYYY-MM-DD'), TO_DATE('2000-01-01', 'YYYY-MM-DD')], ARRAY[ROW('Downing Street', '10', '{}', E'\\x6969'), ROW('Bahnhofstrasse', '12', '{1, 2}', E'\\x6969')]::u_street_type[], '{"England", "Germany"}', ARRAY[ARRAY[1], ARRAY[2]])/

INSERT INTO t_inheritance_1_1 VALUES ('1', '1')/
INSERT INTO t_inheritance_1   VALUES ('2')/
INSERT INTO t_inheritance_1   VALUES ('3')/