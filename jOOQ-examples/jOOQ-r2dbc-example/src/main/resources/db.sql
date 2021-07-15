DROP SCHEMA IF EXISTS r2dbc_example CASCADE;
CREATE SCHEMA r2dbc_example;

CREATE TABLE r2dbc_example.author (
  id INT NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(100) NOT NULL,
  last_name VARCHAR(100) NOT NULL,
  
  CONSTRAINT pk_author PRIMARY KEY (id)
);

CREATE TABLE r2dbc_example.book (
  id INT NOT NULL AUTO_INCREMENT,
  author_id INT NOT NULL,
  title VARCHAR(100) NOT NULL,
  
  CONSTRAINT pk_book PRIMARY KEY (id),
  CONSTRAINT fk_book_author FOREIGN KEY (id) REFERENCES r2dbc_example.author
);