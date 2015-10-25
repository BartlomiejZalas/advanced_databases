-- insert data to tickets table
insert into tickets(event_id, sold_amount, max_amount, price)
select
	e.event_id,
	(select count(*) from participants p where p.event_id = e.event_id),
	trunc((select count(*) from participants p where p.event_id = e.event_id) + 1 + 9 * random()),
	trunc(random() * 90 + 10)
from events e;


alter table users add column location geography(POINT, 4326);
update users set location = ST_SetSRID(ST_MakePoint(longitude, latitude), 4326);

alter table places add column location geography(POINT, 4326);
update places set location = ST_SetSRID(ST_MakePoint(longitude, latitude), 4326);
alter table places alter column location set not null;
