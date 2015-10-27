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



DO
$do$
BEGIN
IF exists (SELECT 1 FROM TICKETS WHERE event_id = 10 AND sold_amount < max_amount) THEN
	INSERT INTO participants VALUES(1, 3);
	UPDATE tickets SET sold_amount = sold_amount+1 WHERE event_id = 10;;
	UPDATE users SET last_activity = now() WHERE user_id = 1;
END IF;
COMMIT;
END
$do$

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


-- Get my nearest 10 places (places which were visited by user)
-- Find all events, in which user was participant
-- Join places to events
-- Calculate distance between user and place
-- Order places by distance.
-- Let's assume, that current user is user no 100
select p.* from places p
	join events e on p.place_id = e.place_id
	join participants part on part.event_id = e.event_id and part.user_id = 100
order by st_distance(p.location, (select location from users where user_id = 100))
limit 10;


-- Get most popular, active events in the area (within 25km radius), paged, 15 events per page. Add user an events from the list.
-- Select places wihin area
-- Join events
-- Order by number of participants descending
-- Drop first page * 15 results
-- Take 15 results.
-- Add user an event from the list if there are tickets available.
-- assume page 0 and again assume user 100
BEGIN;

insert into participants
select 100, e.event_id from events e
	join places p on p.place_id = e.place_id
where st_dwithin(p.location, (select location from users where user_id = 100), 25000, true) and
				e.ends_at < now() and e.starts_at > now() and
				exists(select t.ticket_id from tickets t where sold_amount < max_amount and event_id = e.event_id)
order by (select count(*) from participants part where part.event_id = e.event_id) desc
offset 0
limit 15;

commit;



-- Get authors who created  within last month at least 10 events which are outside of an area where the user is (25km radius) and delete them. Very primitive spam detection.
-- Join events to each user
-- Join places to each event
-- Calculate distance between user and place (distance)
-- Count events created by user (sum of events) in last month
-- Select users which distance is greater than 25 and sum of events is greater than 10 and remove duplications (users).
-- Remove users.
begin;

delete from users u
where u.user_id in (
	select e.user_id from events e
	join places p on p.place_id = e.place_id
	where st_dwithin(p.location, u.location, 25000) = false and e.created_at > now() - interval '1 month'
	group by e.user_id
	having count(e.user_id) > 10
);

commit;



