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

  dbms_output.put_line('');
  dbms_output.put_line('Copyright Data Geekery GmbH');
  dbms_output.put_line('https://www.jooq.org/benchmark');
END;
/
