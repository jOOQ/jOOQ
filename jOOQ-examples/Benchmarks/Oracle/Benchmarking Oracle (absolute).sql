-- This version displays actual execution times.
-- Beware that according to Oracle licensing, it is not allowed to publish benchmark results
SET SERVEROUTPUT ON
DECLARE
  v_ts TIMESTAMP WITH TIME ZONE;
  v_repeat CONSTANT NUMBER := 10000;
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

    dbms_output.put_line('Run ' || r ||', Statement 1 : ' || (SYSTIMESTAMP - v_ts));
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

    dbms_output.put_line('Run ' || r ||', Statement 2 : ' || (SYSTIMESTAMP - v_ts));
    dbms_output.put_line('');
  END LOOP;
END;
/
