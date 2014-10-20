-- Delete data
ALTER TABLE staff DROP CONSTRAINT fk_staff_address;
ALTER TABLE staff DROP CONSTRAINT fk_staff_store;
DELETE FROM payment ;
DELETE FROM rental ;
DELETE FROM customer ;
DELETE FROM film_category ;
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