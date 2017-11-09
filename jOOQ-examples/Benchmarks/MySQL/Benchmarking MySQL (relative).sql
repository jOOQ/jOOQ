CREATE TABLE IF NOT EXISTS print_relative (run INT, stmt INT, elapsed BIGINT);

delimiter //

CREATE PROCEDURE benchmark ()
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE v_ts BIGINT;
  DECLARE v_repeat INT DEFAULT 10000;
  DECLARE r, c INT;
  DECLARE a INT;

  DECLARE cur1 CURSOR FOR
    SELECT 1 AS a FROM dual;

  DECLARE cur2 CURSOR FOR
    SELECT 1 AS a FROM dual UNION SELECT 2 FROM dual UNION SELECT 3 FROM dual;

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  SET r = 0;

  REPEAT
    SET v_ts = ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000);
    SET c = 0;
    REPEAT
      OPEN cur1;

      read_loop: LOOP
        FETCH cur1 INTO a;
        IF done THEN
          LEAVE read_loop;
        END IF;
	  END LOOP;

      CLOSE cur1;
      SET c = c + 1;
    UNTIL c >= v_repeat END REPEAT;

    INSERT INTO print_relative VALUES (r, 1, ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000) - v_ts);

    SET v_ts = ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000);
    SET c = 0;
    REPEAT
      OPEN cur2;

      read_loop: LOOP
        FETCH cur2 INTO a;
        IF done THEN
          LEAVE read_loop;
        END IF;
	  END LOOP;

      CLOSE cur2;
      SET c = c + 1;
    UNTIL c >= v_repeat END REPEAT;

    INSERT INTO print_relative VALUES (r, 2, ROUND(UNIX_TIMESTAMP(CURTIME(4)) * 1000) - v_ts);

    SET r = r + 1;
  UNTIL r >= 5 END REPEAT;
END//

delimiter ;

CALL benchmark();

SELECT
  run,
  stmt,
  CAST(elapsed / MIN(elapsed) OVER() AS DECIMAL(20, 4)) ratio
FROM print_relative;

DROP PROCEDURE benchmark;

DROP TABLE print_relative;