DO $$
DECLARE
  v_ts TIMESTAMP;
  v_repeat CONSTANT INT := 2000;
  rec RECORD;
  run INT[];
  stmt INT[];
  elapsed DECIMAL[];
  min_elapsed DECIMAL;
  i INT := 1;
BEGIN

  -- Repeat the whole benchmark several times to avoid warmup penalty
  FOR r IN 1..5 LOOP
    v_ts := clock_timestamp();

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

    run[i] := r;
    stmt[i] := 1;
    elapsed[i] := (EXTRACT(EPOCH FROM CAST(clock_timestamp() AS TIMESTAMP)) - EXTRACT(EPOCH FROM v_ts));
    i := i + 1;
    v_ts := clock_timestamp();

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

    run[i] := r;
    stmt[i] := 2;
    elapsed[i] := (EXTRACT(EPOCH FROM CAST(clock_timestamp() AS TIMESTAMP)) - EXTRACT(EPOCH FROM v_ts));
    i := i + 1;
    v_ts := clock_timestamp();

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

    run[i] := r;
    stmt[i] := 3;
    elapsed[i] := (EXTRACT(EPOCH FROM CAST(clock_timestamp() AS TIMESTAMP)) - EXTRACT(EPOCH FROM v_ts));
    i := i + 1;
    v_ts := clock_timestamp();

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

    run[i] := r;
    stmt[i] := 4;
    elapsed[i] := (EXTRACT(EPOCH FROM CAST(clock_timestamp() AS TIMESTAMP)) - EXTRACT(EPOCH FROM v_ts));
    i := i + 1;
  END LOOP;

  SELECT min(t.elapsed)
  INTO min_elapsed
  FROM unnest(elapsed) AS t(elapsed);

  FOR i IN 1..array_length(run, 1) LOOP
    RAISE INFO 'RUN %, Statement %: %', run[i], stmt[i], CAST(elapsed[i] / min_elapsed AS DECIMAL(10, 5));
  END LOOP;
END$$;