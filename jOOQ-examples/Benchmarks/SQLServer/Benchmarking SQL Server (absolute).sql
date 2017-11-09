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
-- Beware that according to SQL Server licensing, it is not allowed to publish benchmark results
DECLARE @ts DATETIME;
DECLARE @repeat INT = 10000;
DECLARE @r INT;
DECLARE @i INT;
DECLARE @dummy VARCHAR;

DECLARE @s1 CURSOR;
DECLARE @s2 CURSOR;

SET @r = 0;
WHILE @r < 5
BEGIN
  SET @r = @r + 1

  SET @s1 = CURSOR FOR
  -- Paste statement 1 here
  SELECT 1 x;

  SET @s2 = CURSOR FOR
  -- Paste statement 2 here
  WITH t(v) AS (
    SELECT 1
    UNION ALL
    SELECT v + 1 FROM t WHERE v < 10
  )
  SELECT * FROM t

  SET @ts = current_timestamp;
  SET @i = 0;
  WHILE @i < @repeat
  BEGIN
    SET @i = @i + 1

    OPEN @s1;
    FETCH NEXT FROM @s1 INTO @dummy;
    WHILE @@FETCH_STATUS = 0
    BEGIN
      FETCH NEXT FROM @s1 INTO @dummy;
    END;

    CLOSE @s1;
  END;

  DEALLOCATE @s1;
  PRINT 'Run ' + CAST(@r AS VARCHAR) + ', Statement 1: ' + CAST(DATEDIFF(ms, @ts, current_timestamp) AS VARCHAR) + 'ms';

  SET @ts = current_timestamp;
  SET @i = 0;
  WHILE @i < @repeat
  BEGIN
    SET @i = @i + 1

    OPEN @s2;
    FETCH NEXT FROM @s2 INTO @dummy;
    WHILE @@FETCH_STATUS = 0
    BEGIN
      FETCH NEXT FROM @s2 INTO @dummy;
    END;

    CLOSE @s2;
  END;

  DEALLOCATE @s2;
  PRINT 'Run ' + CAST(@r AS VARCHAR) + ', Statement 2: ' + CAST(DATEDIFF(ms, @ts, current_timestamp) AS VARCHAR) + 'ms';
END;

PRINT '';
PRINT 'Copyright Data Geekery GmbH';
PRINT 'https://www.jooq.org/benchmark';

