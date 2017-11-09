-- Copyright Data Geekery GmbH
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--     http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--
-- This version displays actual execution times.
-- According to our understanding of DB2 licensing, such benchmark results may be published
-- as they cannot be compared to other databases and do not provide absolute time values
BEGIN
  DECLARE CONTINUE HANDLER FOR SQLSTATE '42710' BEGIN END;
  EXECUTE IMMEDIATE 'CREATE TABLE print_relative (run INTEGER, stmt INTEGER, elapsed DECIMAL(20, 4))';
END

BEGIN
  DECLARE v_ts TIMESTAMP;
  DECLARE v_repeat INTEGER DEFAULT 100;
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
        -- Paste statement 1 here
        SELECT 1 AS a FROM sysibm.dual
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

    INSERT INTO print_relative VALUES (v_i, 2, (CURRENT_TIMESTAMP - v_ts));

    SET v_i = v_i + 1;
    UNTIL v_i > 5
  END REPEAT;
END


SELECT run, stmt, ratio, copyright
FROM (
  SELECT
    run,
    stmt,
    CAST(elapsed / MIN(elapsed) OVER() AS DECIMAL(20, 4)) AS ratio,
    CAST(NULL AS VARCHAR(100)) AS copyright,
    1 AS x
  FROM print_relative
  UNION ALL
  SELECT null, null, null, null, 2 AS x FROM sysibm.dual
  UNION ALL
  SELECT null, null, null, 'Copyright Data Geekery GmbH', 3 AS x FROM sysibm.dual
  UNION ALL
  SELECT null, null, null, 'https://www.jooq.org/benchmark', 4 AS x FROM sysibm.dual
) t
ORDER BY x, run, stmt;

DROP TABLE print_relative;
