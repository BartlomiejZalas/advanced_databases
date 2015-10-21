
create table users(
  user_id bigserial primary key,
  email character varying(255) not null unique,
  password character varying(255) not null,
  latitude real,
  longitude real,
  created_at timestamp without time zone not null default now(),
  last_activity timestamp without time zone not null default now()
);


create table places(
  place_id bigserial primary key,
  name character varying(127) not null,
  latitude real not null,
  longitude real not null,
  description character varying(511)
);


create table events(
  event_id bigserial primary key,
  name character varying(127) not null,
  description character varying(511),
  created_at timestamp without time zone not null default now(),
  starts_at timestamp without time zone not null,
  ends_at timestamp without time zone not null,
  user_id bigint not null references users(user_id) ,
  place_id bigint not null references places(place_id)
);


create table participants(
  user_id bigint not null references users(user_id) on delete cascade,
  event_id bigint not null references events(event_id) on delete cascade,
  primary key(user_id, event_id)
);


create table ratings(
  user_id bigint not null references users(user_id) on delete cascade,
  event_id bigint not null references events(event_id) on delete cascade,
  value smallint not null,
  primary key(user_id, event_id)
);

create table tickets(
  ticket_id bigint primary key,
  event_id bigint not null REFERENCES events(event_id) on delete cascade,
  sold_amount int not null default 0,
  max_amount int,
  price real not null
);
