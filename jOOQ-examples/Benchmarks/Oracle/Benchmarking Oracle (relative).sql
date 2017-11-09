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
        -- Paste statement 1 here
        SELECT 1 FROM dual
      ) LOOP
        NULL;
      END LOOP;
    END LOOP;

    INSERT INTO results VALUES (r, 1, SYSDATE + ((SYSTIMESTAMP - v_ts) * 86400) - SYSDATE);
    v_ts := SYSTIMESTAMP;

    FOR i IN 1..v_repeat LOOP
      FOR rec IN (
        -- Paste statement 2 here
        SELECT 1
        FROM dual
        CONNECT BY level < 100
      ) LOOP
        NULL;
      END LOOP;
    END LOOP;

    INSERT INTO results VALUES (r, 2, SYSDATE + ((SYSTIMESTAMP - v_ts) * 86400) - SYSDATE);
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