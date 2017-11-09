-- This version displays relative execution times (fastest execution = 1)
-- According to our understanding of SQL Server licensing, such benchmark results may be published
-- as they cannot be compared to other databases and do not provide absolute time values
DECLARE @ts DATETIME;
DECLARE @repeat INT = 2000;
DECLARE @r INT;
DECLARE @i INT;
DECLARE @dummy1 VARCHAR;
DECLARE @dummy2 VARCHAR;
DECLARE @dummy3 INT;

DECLARE @s1 CURSOR;
DECLARE @s2 CURSOR;
DECLARE @s3 CURSOR;
DECLARE @s4 CURSOR;

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
    SELECT
      first_name, last_name, count(*) c
    FROM actor
    JOIN film_actor ON actor.actor_id = film_actor.actor_id
    WHERE last_name LIKE 'A%'
    GROUP BY first_name, last_name
    ORDER BY count(*) DESC

  SET @s2 = CURSOR FOR
    SELECT
      first_name, last_name,
      count(*) a
    FROM (
      SELECT *
      FROM actor
      WHERE last_name LIKE 'A%'
    ) a
    JOIN film_actor
    ON a.actor_id = film_actor.actor_id
    GROUP BY
      first_name, last_name
    ORDER BY count(*) DESC

  SET @s3 = CURSOR FOR
    SELECT * FROM (
      SELECT
      first_name, last_name, (
        SELECT count(*)
        FROM film_actor fa
        WHERE a.actor_id =
        fa.actor_id
      ) AS c
      FROM actor a
      WHERE last_name LIKE 'A%'
    ) a
    WHERE c > 0
    ORDER BY c DESC

  SET @s4 = CURSOR FOR
    SELECT
      first_name, last_name, c
    FROM actor
    JOIN (
      SELECT actor_id,count(*) c
      FROM film_actor
      GROUP BY actor_id
    ) fa
    ON actor.actor_id = fa.actor_id
    WHERE last_name LIKE 'A%'
    ORDER BY c DESC

  SET @ts = current_timestamp;
  SET @i = 0;
  WHILE @i < @repeat
  BEGIN
    SET @i = @i + 1

    OPEN @s1;
    FETCH NEXT FROM @s1 INTO @dummy1, @dummy2, @dummy3;
    WHILE @@FETCH_STATUS = 0
    BEGIN
      FETCH NEXT FROM @s1 INTO @dummy1, @dummy2, @dummy3;
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
    FETCH NEXT FROM @s2 INTO @dummy1, @dummy2, @dummy3;
    WHILE @@FETCH_STATUS = 0
    BEGIN
      FETCH NEXT FROM @s2 INTO @dummy1, @dummy2, @dummy3;
    END;

    CLOSE @s2;
  END;

  DEALLOCATE @s2;
  INSERT INTO @results VALUES (@r, 2, DATEDIFF(ms, @ts, current_timestamp));

  SET @ts = current_timestamp;
  SET @i = 0;
  WHILE @i < @repeat
  BEGIN
    SET @i = @i + 1

    OPEN @s3;
    FETCH NEXT FROM @s3 INTO @dummy1, @dummy2, @dummy3;
    WHILE @@FETCH_STATUS = 0
    BEGIN
      FETCH NEXT FROM @s3 INTO @dummy1, @dummy2, @dummy3;
    END;

    CLOSE @s3;
  END;

  DEALLOCATE @s3;
  INSERT INTO @results VALUES (@r, 3, DATEDIFF(ms, @ts, current_timestamp));

  SET @ts = current_timestamp;
  SET @i = 0;
  WHILE @i < @repeat
  BEGIN
    SET @i = @i + 1

    OPEN @s4;
    FETCH NEXT FROM @s4 INTO @dummy1, @dummy2, @dummy3;
    WHILE @@FETCH_STATUS = 0
    BEGIN
      FETCH NEXT FROM @s4 INTO @dummy1, @dummy2, @dummy3;
    END;

    CLOSE @s4;
  END;

  DEALLOCATE @s4;
  INSERT INTO @results VALUES (@r, 4, DATEDIFF(ms, @ts, current_timestamp));
END;

SELECT 'Run ' + CAST(run AS VARCHAR) + ', Statement ' + CAST(stmt AS VARCHAR) + ': ' + CAST(CAST(elapsed / MIN(elapsed) OVER() AS DECIMAL(10, 5)) AS VARCHAR)
FROM @results;
