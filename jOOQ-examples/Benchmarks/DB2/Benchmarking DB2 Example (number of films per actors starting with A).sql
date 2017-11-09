-- This version displays actual execution times.
-- According to our understanding of DB2 licensing, such benchmark results may be published
-- as they cannot be compared to other databases and do not provide absolute time values
BEGIN
  DECLARE CONTINUE HANDLER FOR SQLSTATE '42710' BEGIN END;
  EXECUTE IMMEDIATE 'CREATE TABLE print_relative (run INTEGER, stmt INTEGER, elapsed DECIMAL(20, 4))';
END

BEGIN
  DECLARE v_ts TIMESTAMP;
  DECLARE v_repeat INTEGER DEFAULT 2000;
  DECLARE v_i INTEGER;
  DECLARE v_j INTEGER;

  -- Repeat the whole benchmark several times to avoid warmup penalty
  SET v_i = 1;

  DELETE FROM print_relative;

  REPEAT
    SET v_j = 1;
    SET v_ts = CURRENT_TIMESTAMP;

    REPEAT
      FOR rec AS cur CURSOR FOR
        SELECT
          first_name, last_name, count(*) c
        FROM actor
        JOIN film_actor ON actor.actor_id = film_actor.actor_id
        WHERE last_name LIKE 'A%'
        GROUP BY first_name, last_name
        ORDER BY count(*) DESC
      DO
        BEGIN END;
      END FOR;

      SET v_j = v_j + 1;
      UNTIL v_j = v_repeat
    END REPEAT;

    INSERT INTO print_relative VALUES (v_i, 1, (CURRENT_TIMESTAMP - v_ts));

    SET v_j = 1;
    SET v_ts = CURRENT_TIMESTAMP;

    REPEAT
      FOR rec AS cur CURSOR FOR
        SELECT
          first_name, last_name,
          count(*) a
        FROM (
          SELECT *
          FROM actor
          WHERE last_name LIKE 'A%'
        ) a
        JOIN film_actor
        ON a.actor_id = film_actor.actor_id
        GROUP BY
          first_name, last_name
        ORDER BY count(*) DESC
      DO
        BEGIN END;
      END FOR;

      SET v_j = v_j + 1;
      UNTIL v_j = v_repeat
    END REPEAT;

    INSERT INTO print_relative VALUES (v_i, 2, (CURRENT_TIMESTAMP - v_ts));

    SET v_j = 1;
    SET v_ts = CURRENT_TIMESTAMP;

    REPEAT
      FOR rec AS cur CURSOR FOR
        SELECT * FROM (
          SELECT
            first_name, last_name, (
              SELECT count(*)
              FROM film_actor fa
              WHERE a.actor_id =
                fa.actor_id
            ) AS c
          FROM actor a
          WHERE last_name LIKE 'A%'
        ) a
        WHERE c > 0
        ORDER BY c DESC
      DO
        BEGIN END;
      END FOR;

      SET v_j = v_j + 1;
      UNTIL v_j = v_repeat
    END REPEAT;

    INSERT INTO print_relative VALUES (v_i, 3, (CURRENT_TIMESTAMP - v_ts));

    SET v_j = 1;
    SET v_ts = CURRENT_TIMESTAMP;

    REPEAT
      FOR rec AS cur CURSOR FOR
        SELECT
          first_name, last_name, c
        FROM actor
        JOIN (
          SELECT actor_id,count(*) c
          FROM film_actor
          GROUP BY actor_id
        ) fa
        ON actor.actor_id = fa.actor_id
        WHERE last_name LIKE 'A%'
        ORDER BY c DESC
      DO
        BEGIN END;
      END FOR;

      SET v_j = v_j + 1;
      UNTIL v_j = v_repeat
    END REPEAT;

    INSERT INTO print_relative VALUES (v_i, 4, (CURRENT_TIMESTAMP - v_ts));

    SET v_i = v_i + 1;
    UNTIL v_i = 5
  END REPEAT;
END

SELECT
  run,
  stmt,
  CAST(elapsed / MIN(elapsed) OVER() AS DECIMAL(20, 4)) ratio
FROM print_relative;

DROP TABLE print_relative;
