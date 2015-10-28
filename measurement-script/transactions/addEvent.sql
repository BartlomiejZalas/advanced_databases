\timing on

BEGIN;
	INSERT INTO places (name, latitude, longitude, description, location) 
	VALUES(
		'Super bar', 123.123456, 123.321654, 'Great place to have a fun', 
		ST_SetSRID(ST_MakePoint(123.123456, 123.321654), 4326)
	);

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
