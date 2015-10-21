--****************************************************************************
--Transaction "Add event with predefined participants and tickets"
--****************************************************************************

--Preconditions - there are author (with user_id=1) and participants(with user_id=2) in database
INSERT INTO users (email, password, latitude, longitude, created_at, last_activity)
VALUES('email@domain.com', 'superpass', 123.123,654.654, now(), now() ) ;

INSERT INTO users (email, password, latitude, longitude, created_at, last_activity)
VALUES('participant@domain.com', 'superpass', 123.123,654.654, now(), now()) ;

--Transaction

BEGIN;
	INSERT INTO places (name, latitude, longitude, description) VALUES('Super bar', 123.123456, 321.321654, 'Great place to have a fun');

	INSERT INTO events (
		name, description, created_at, starts_at, ends_at, user_id, place_id
	) 
	VALUES (
		'Super event', 'description...', now(), '2015-11-01', '2015-11-02', 
		1, currval('places_place_id_seq')
	);

	INSERT INTO participants VALUES(2, currval('events_event_id_seq'));

	INSERT INTO tickets (event_id, sold_amount, max_amount, price)
	VALUES (currval('events_event_id_seq'), 0, 30, 19.99);

	UPDATE users SET last_activity = now() WHERE user_id = 1;
COMMIT;


--****************************************************************************
--Transaction "Register as participant"
--****************************************************************************
-- Preconditions:
-- - event with id 3 exists in DB
-- - user with id 1 exists in DB

BEGIN;
-- IF STATEMENT?????????????
SELECT max_amount - sold_amount as free_tickets FROM tickets;

INSERT INTO participants VALUES(1, 3);
UPDATE tickets SET sold_amount = sold_amount+1;
UPDATE users SET last_activity = now() WHERE user_id = 1;

COMMIT;

--****************************************************************************
--Get top 10 profitable events with rating at least 4
--****************************************************************************
SELECT e.name, AVG(r.value), 
	(SELECT sold_amount*price as profit FROM tickets WHERE event_id = e.event_id) 
FROM events e
LEFT JOIN ratings r ON r.event_id = e.event_id
GROUP BY e.event_id
HAVING  AVG(r.value) > 4
ORDER BY profit
LIMIT 10;






