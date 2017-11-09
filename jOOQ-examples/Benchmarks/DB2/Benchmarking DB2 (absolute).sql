-- This version displays actual execution times.
-- Beware that according to DB2 licensing, it is not allowed to publish benchmark results
BEGIN
  DECLARE CONTINUE HANDLER FOR SQLSTATE '42710' BEGIN END;
  EXECUTE IMMEDIATE 'CREATE TABLE print (text VARCHAR(500))';
END

BEGIN
  DECLARE v_ts TIMESTAMP;
  DECLARE v_repeat INTEGER DEFAULT 100;
  DECLARE v_i INTEGER;
  DECLARE v_j INTEGER;

  -- Repeat the whole benchmark several times to avoid warmup penalty
  SET v_i = 1;

  DELETE FROM print;

  REPEAT
    SET v_j = 1;
    SET v_ts = CURRENT_TIMESTAMP;

    REPEAT
      FOR rec AS cur CURSOR FOR
        -- Paste statement 1 here
        SELECT 1 AS a FROM sysibm.dual
      DO
        BEGIN END;
      END FOR;

      SET v_j = v_j + 1;
      UNTIL v_j = v_repeat
    END REPEAT;

    INSERT INTO print VALUES ('Run ' || v_i ||', Statement 1 : ' || (CURRENT_TIMESTAMP - v_ts));

    SET v_j = 1;
    SET v_ts = CURRENT_TIMESTAMP;

    REPEAT
      FOR rec AS cur CURSOR FOR
        -- Paste statement 2 here
        WITH t(a) AS (
          SELECT 1 AS a FROM sysibm.dual
          UNION ALL
          SELECT a + 1
          FROM t
          WHERE a < 100
        )
        SELECT a FROM t
      DO
        BEGIN END;
      END FOR;

      SET v_j = v_j + 1;
      UNTIL v_j > v_repeat
    END REPEAT;

    INSERT INTO print VALUES ('Run ' || v_i ||', Statement 2 : ' || (CURRENT_TIMESTAMP - v_ts));

    SET v_i = v_i + 1;
    UNTIL v_i > 5
  END REPEAT;
END

SELECT * FROM print;

DROP TABLE print;
