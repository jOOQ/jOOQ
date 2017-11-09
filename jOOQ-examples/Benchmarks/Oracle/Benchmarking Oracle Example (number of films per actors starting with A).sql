-- This version displays relative execution times (fastest execution = 1)
-- According to our understanding of Oracle licensing, such benchmark results may be published
-- as they cannot be compared to other databases and do not provide absolute time values
SET SERVEROUTPUT ON
CREATE TABLE results (
  run     NUMBER(2),
  stmt    NUMBER(2),
  elapsed NUMBER
);

DECLARE
  v_ts TIMESTAMP WITH TIME ZONE;
  v_repeat CONSTANT NUMBER := 2000;
BEGIN

  -- Repeat the whole benchmark several times to avoid warmup penalty
  FOR r IN 1..5 LOOP
    v_ts := SYSTIMESTAMP;

    FOR i IN 1..v_repeat LOOP
      FOR rec IN (
        SELECT
          first_name, last_name, count(*) c
        FROM actor
        JOIN film_actor ON actor.actor_id = film_actor.actor_id
        WHERE last_name LIKE 'A%'
        GROUP BY first_name, last_name
        ORDER BY count(*) DESC
      ) LOOP
        NULL;
      END LOOP;
    END LOOP;

    INSERT INTO results VALUES (r, 1, SYSDATE + ((SYSTIMESTAMP - v_ts) * 86400) - SYSDATE);
    v_ts := SYSTIMESTAMP;

    FOR i IN 1..v_repeat LOOP
      FOR rec IN (
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
      ) LOOP
        NULL;
      END LOOP;
    END LOOP;

    INSERT INTO results VALUES (r, 2, SYSDATE + ((SYSTIMESTAMP - v_ts) * 86400) - SYSDATE);
    v_ts := SYSTIMESTAMP;

    FOR i IN 1..v_repeat LOOP
      FOR rec IN (
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
      ) LOOP
        NULL;
      END LOOP;
    END LOOP;

    INSERT INTO results VALUES (r, 3, SYSDATE + ((SYSTIMESTAMP - v_ts) * 86400) - SYSDATE);
    v_ts := SYSTIMESTAMP;

    FOR i IN 1..v_repeat LOOP
      FOR rec IN (
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
      ) LOOP
        NULL;
      END LOOP;
    END LOOP;

    INSERT INTO results VALUES (r, 4, SYSDATE + ((SYSTIMESTAMP - v_ts) * 86400) - SYSDATE);
  END LOOP;

  FOR rec IN (
    SELECT
      run, stmt,
      CAST(elapsed / MIN(elapsed) OVER() AS NUMBER(10, 5)) ratio
    FROM results
  )
  LOOP
    dbms_output.put_line('Run ' || rec.run ||
      ', Statement ' || rec.stmt ||
      ' : ' || rec.ratio);
  END LOOP;
END;
/

DROP TABLE results;