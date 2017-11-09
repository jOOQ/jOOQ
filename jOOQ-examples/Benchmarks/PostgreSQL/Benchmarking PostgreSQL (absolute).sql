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

  RAISE INFO '';
  RAISE INFO 'Copyright Data Geekery GmbH';
  RAISE INFO 'https://www.jooq.org/benchmark';
END$$;