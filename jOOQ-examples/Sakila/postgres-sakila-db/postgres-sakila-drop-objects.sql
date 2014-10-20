-- Drop Views

DROP VIEW customer_list;
DROP VIEW film_list;
DROP VIEW nicer_but_slower_film_list;
DROP VIEW sales_by_film_category;
DROP VIEW sales_by_store;
DROP VIEW staff_list;

-- Drop Tables


DROP TABLE payment CASCADE;
DROP TABLE rental CASCADE;
DROP TABLE inventory CASCADE;
DROP TABLE film_category CASCADE;
DROP TABLE film_actor CASCADE;
DROP TABLE film CASCADE;
DROP TABLE language CASCADE;
DROP TABLE customer CASCADE;
DROP TABLE actor CASCADE;
DROP TABLE category CASCADE;
DROP TABLE store CASCADE;
DROP TABLE address CASCADE;
DROP TABLE staff CASCADE;
DROP TABLE city CASCADE;
DROP TABLE country CASCADE;

--Procedures

DROP FUNCTION film_in_stock(integer, integer);
DROP FUNCTION film_not_in_stock(integer, integer);
DROP FUNCTION get_customer_balance(integer, timestamp without time zone);
DROP FUNCTION inventory_held_by_customer(integer);
DROP FUNCTION inventory_in_stock(integer);
DROP FUNCTION last_day(timestamp without time zone);
DROP FUNCTION rewards_report(integer, numeric);
DROP FUNCTION last_updated();
DROP FUNCTION _group_concat(text, text) CASCADE;

-- DROP SEQUENCES
DROP SEQUENCE actor_actor_id_seq;
DROP SEQUENCE address_address_id_seq;
DROP SEQUENCE category_category_id_seq;
DROP SEQUENCE city_city_id_seq;
DROP SEQUENCE country_country_id_seq;
DROP SEQUENCE customer_customer_id_seq;
DROP SEQUENCE film_film_id_seq;
DROP SEQUENCE inventory_inventory_id_seq;
DROP SEQUENCE language_language_id_seq;
DROP SEQUENCE payment_payment_id_seq;
DROP SEQUENCE rental_rental_id_seq;
DROP SEQUENCE staff_staff_id_seq;
DROP SEQUENCE store_store_id_seq;

DROP DOMAIN year;