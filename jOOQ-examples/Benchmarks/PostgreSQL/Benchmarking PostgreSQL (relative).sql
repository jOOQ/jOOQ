DO $$
DECLARE
  v_ts TIMESTAMP;
  v_repeat CONSTANT INT := 10000;
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
        -- Paste statement 1 here
        SELECT 1
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
        -- Paste statement 2 here
        WITH RECURSIVE t(v) AS (
          SELECT 1
          UNION ALL
          SELECT v + 1 FROM t WHERE v < 10
        )
        SELECT * FROM t
      ) LOOP
        NULL;
      END LOOP;
    END LOOP;

    run[i] := r;
    stmt[i] := 2;
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