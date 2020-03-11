# Installation instructions

## Create schema user
Connect as an admin user such as `SYSTEM` or `SYS` and create a new schema user:

```sql
SQL> CREATE USER SAKILA IDENTIFIED BY sakila;

SQL> GRANT CONNECT, RESOURCE, CREATE VIEW, UNLIMITED TABLESPACE TO SAKILA;
```

## Create data model
Connect as the `SAKILA` user and run:

```sql
SQL> @oracle-sakila-schema.sql
```

## Load data
Connect as the `SAKILA` user and run:

```sql
SQL> @oracle-sakila-insert-data.sql
```

Once the script is completed, run the following verification:

```sql
SQL> SELECT table_name FROM user_tables;

TABLE_NAME
--------------
ACTOR
COUNTRY
CITY
ADDRESS
LANGUAGE
CATEGORY
CUSTOMER
FILM
FILM_ACTOR
FILM_CATEGORY
FILM_TEXT
INVENTORY
STAFF
STORE
PAYMENT
RENTAL

16 rows selected.

SQL> SELECT COUNT(*) FROM film;

  COUNT(*)
----------
      1000
```

## Delete data
Connect as the `SAKILA` user and run:

```sql
SQL> @oracle-sakila-delete-data.sql
```

## Drop all objects
Connect as the `SAKILA` user and run:

```sql
SQL> @oracle-sakila-drop-objects.sql
```

## Remove schema user
Connect as an admin user such as `SYSTEM` or `SYS` and drop the schema user:

```sql
SQL> DROP USER SAKILA CASCADE;
```
