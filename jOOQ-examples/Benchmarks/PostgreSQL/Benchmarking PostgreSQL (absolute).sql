DO $$
DECLARE
  v_ts TIMESTAMP;
  v_repeat CONSTANT INT := 10000;
  rec RECORD;
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

    RAISE INFO 'Run %, Statement 1: %', r, (clock_timestamp() - v_ts);
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

    RAISE INFO 'Run %, Statement 2: %', r, (clock_timestamp() - v_ts);
    RAISE INFO '';
  END LOOP;
END$$;