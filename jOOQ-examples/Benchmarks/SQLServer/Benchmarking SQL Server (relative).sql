-- This version displays relative execution times (fastest execution = 1)
-- According to our understanding of SQL Server licensing, such benchmark results may be published
-- as they cannot be compared to other databases and do not provide absolute time values
DECLARE @ts DATETIME;
DECLARE @repeat INT = 2000;
DECLARE @r INT;
DECLARE @i INT;
DECLARE @dummy VARCHAR;

DECLARE @s1 CURSOR;
DECLARE @s2 CURSOR;

DECLARE @results TABLE (
  run     INT,
  stmt    INT,
  elapsed DECIMAL
);

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
  INSERT INTO @results VALUES (@r, 1, DATEDIFF(ms, @ts, current_timestamp));

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
  INSERT INTO @results VALUES (@r, 2, DATEDIFF(ms, @ts, current_timestamp));
END;

SELECT 'Run ' + CAST(run AS VARCHAR) + ', Statement ' + CAST(stmt AS VARCHAR) + ': ' + CAST(CAST(elapsed / MIN(elapsed) OVER() AS DECIMAL(10, 5)) AS VARCHAR)
FROM @results;
