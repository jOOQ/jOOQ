CREATE TABLE user (
  id INT PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL
);

CREATE UNIQUE INDEX name_idx ON user (first_name, last_name);
