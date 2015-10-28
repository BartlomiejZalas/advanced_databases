\timing on

DO
$do$
BEGIN
IF exists (SELECT 1 FROM TICKETS WHERE event_id = 10 AND sold_amount < max_amount) THEN
	INSERT INTO participants VALUES(1, 3);
	UPDATE tickets SET sold_amount = sold_amount+1 WHERE event_id = 10;
	UPDATE users SET last_activity = now() WHERE user_id = 1;
END IF;
END
$do$
