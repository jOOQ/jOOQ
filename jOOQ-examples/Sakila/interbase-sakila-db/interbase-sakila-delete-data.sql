/*

Sakila for Interbase/Firebird is a port of the Sakila example database available for MySQL, which was originally developed by Mike Hillyer of the MySQL AB documentation team. 
This project is designed to help database administrators to decide which database to use for development of new products
The user can run the same SQL against different kind of databases and compare the performance

License: BSD
Copyright DB Software Laboratory
http://www.etl-tools.com

*/

-- Delete data
�lTER TABLE staff DROP CONSTRAINT fk_staff_address;
AlTER TABLE staff DROP CONSTRAINT fk_staff_store;
ALTER TABLE store DROP CONSTRAINT fk_store_staff;

DELETE FROM payment ;
DELETE FROM rental ;
DELETE FROM customer ;
DELETE FROM film_category ;
DELETE FROM film_text ;
DELETE FROM film_actor ;
DELETE FROM inventory ;
DELETE FROM film ;
DELETE FROM category ;
DELETE FROM staff ;
DELETE FROM store ;
DELETE FROM actor ;
DELETE FROM address ;
DELETE FROM city ;
DELETE FROM country ;
DELETE FROM language ;

ALTER TABLE staff ADD CONSTRAINT fk_staff_address FOREIGN KEY (address_id) REFERENCES address (address_id) ON UPDATE CASCADE;
ALTER TABLE staff ADD CONSTRAINT fk_staff_store FOREIGN KEY (store_id) REFERENCES store (store_id) ON UPDATE CASCADE;
ALTER TABLE store ADD CONSTRAINT fk_store_staff FOREIGN KEY (manager_staff_id) REFERENCES staff (staff_id);
