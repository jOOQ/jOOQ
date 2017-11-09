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


SELECT text
FROM (
  SELECT text, 1 AS x
  FROM print
  UNION ALL
  SELECT null, 2 AS x FROM sysibm.dual
  UNION ALL
  SELECT 'Copyright Data Geekery GmbH', 3 AS x FROM sysibm.dual
  UNION ALL
  SELECT 'https://www.jooq.org/benchmark', 4 AS x FROM sysibm.dual
) t
ORDER BY x, text;

DROP TABLE print;