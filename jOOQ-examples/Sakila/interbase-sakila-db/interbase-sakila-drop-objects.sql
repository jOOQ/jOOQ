/*

Sakila for Interbase/Firebird is a port of the Sakila example database available for MySQL, which was originally developed by Mike Hillyer of the MySQL AB documentation team. 
This project is designed to help database administrators to decide which database to use for development of new products
The user can run the same SQL against different kind of databases and compare the performance

License: BSD
Copyright DB Software Laboratory
http://www.etl-tools.com

*/

-- Drop Views

DROP VIEW customer_list;
DROP VIEW film_list;
--DROP VIEW nicer_but_slower_film_list;
DROP VIEW sales_by_film_category;
DROP VIEW sales_by_store;
DROP VIEW staff_list;

-- Drop Tables

DROP TABLE payment;
DROP TABLE rental;
DROP TABLE inventory;
DROP TABLE film_text;
DROP TABLE film_category;
DROP TABLE film_actor;
DROP TABLE film;
DROP TABLE language;
DROP TABLE customer;
DROP TABLE actor;
DROP TABLE category;
ALTER TABLE staff DROP CONSTRAINT fk_staff_address;
ALTER TABLE store DROP CONSTRAINT fk_store_staff;
ALTER TABLE staff DROP CONSTRAINT fk_staff_store;
DROP TABLE store;
DROP TABLE address;
DROP TABLE staff;
DROP TABLE city;
DROP TABLE country;

-- DROP GENERATORS

DROP GENERATOR ACTOR_GENERATOR;
DROP GENERATOR ADDRESS_GENERATOR;
DROP GENERATOR CATEGORY_GENERATOR;
DROP GENERATOR CITY_GENERATOR;
DROP GENERATOR COUNTRY_GENERATOR;
DROP GENERATOR CUSTOMER_GENERATOR;
DROP GENERATOR FILM_GENERATOR;
DROP GENERATOR INVENTORY_GENERATOR;
DROP GENERATOR LANGUAGE_GENERATOR;
DROP GENERATOR PAYMENT_GENERATOR;
DROP GENERATOR RENTAL_GENERATOR;
DROP GENERATOR STAFF_GENERATOR;
DROP GENERATOR STORE_GENERATOR;

-- Procedures and views
--drop procedure film_in_stock;
--drop procedure film_not_in_stock;
--drop function get_customer_balance;
--drop function inventory_held_by_customer;
--drop function inventory_in_stock;
--drop procedure rewards_report;
