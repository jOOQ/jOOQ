DELETE FROM t_exotic_types/
DELETE FROM t_639_numbers_table/
DELETE FROM t_book_to_book_store/
DELETE FROM t_book_store/
DELETE FROM t_book/
DELETE FROM t_author/
DELETE FROM t_language/
DELETE FROM t_booleans/
DELETE FROM t_dates/
DELETE FROM t_identity_pk/
DELETE FROM t_triggers/

INSERT INTO t_language (id, cd, description, description_english) VALUES (1, 'en', 'English', 'English')/
INSERT INTO t_language (id, cd, description, description_english) VALUES (2, 'de', 'Deutsch', 'German')/
INSERT INTO t_language (id, cd, description, description_english) VALUES (3, 'fr', 'Français', 'French')/
INSERT INTO t_language (id, cd, description, description_english) VALUES (4, 'pt', null, null)/


INSERT INTO t_author VALUES (1, 'George', 'Orwell', '1903-06-25', 1903, null);/
INSERT INTO t_author VALUES (2, 'Paulo', 'Coelho', '1947-08-24', 1947, null);/

INSERT INTO t_book VALUES (1, 1, null, null, '1984', 1948, 1, 'To know and not to know, to be conscious of complete truthfulness while telling carefully constructed lies, to hold simultaneously two opinions which cancelled out, knowing them to be contradictory and believing in both of them, to use logic against logic, to repudiate morality while laying claim to it, to believe that democracy was impossible and that the Party was the guardian of democracy, to forget, whatever it was necessary to forget, then to draw it back into memory again at the moment when it was needed, and then promptly to forget it again, and above all, to apply the same process to the process itself -- that was the ultimate subtlety; consciously to induce unconsciousness, and then, once again, to become unconscious of the act of hypnosis you had just performed. Even to understand the word ''doublethink'' involved the use of doublethink..', null, 'ORDERED')/
INSERT INTO t_book VALUES (2, 1, null, null, 'Animal Farm', 1945, 1, null, null, 'ON STOCK')/
INSERT INTO t_book VALUES (3, 2, null, null, 'O Alquimista', 1988, 4, null, null, 'ON STOCK')/
INSERT INTO t_book VALUES (4, 2, null, null, 'Brida', 1990, 2, null, null, 'SOLD OUT')/

INSERT INTO t_book_store (name) VALUES
	('Orell Füssli'),
	('Ex Libris'),
	('Buchhandlung im Volkshaus')/

INSERT INTO t_book_to_book_store VALUES
	('Orell Füssli', 1, 10),
	('Orell Füssli', 2, 10),
	('Orell Füssli', 3, 10),
	('Ex Libris', 1, 1),
	('Ex Libris', 3, 2),
	('Buchhandlung im Volkshaus', 3, 1)/