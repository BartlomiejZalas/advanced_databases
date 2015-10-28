\timing on

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
