
create table users(
  id bigserial primary key,
  email character varying(255) not null unique,
  password character varying(255) not null,
  latitude real,
  longitude real,
  created_at timestamp without time zone not null default now(),
  updated_at timestamp without time zone not null default now()
);


create table places(
  id bigserial primary key,
  name character varying(127) not null,
  latitude real not null,
  longitude real not null,
  description character varying(511)
);


create table events(
  id bigserial primary key,
  name character varying(127) not null,
  description character varying(511),
  user_id bigint not null references users(id) ,
  place_id bigint not null references places(id)
);


create table participants(
  user_id bigint not null references users(id) on delete cascade,
  event_id bigint not null references events(id) on delete cascade,
  primary key(user_id, event_id)
);


create table ratings(
  user_id bigint not null references users(id) on delete cascade,
  event_id bigint not null references events(id) on delete cascade,
  value smallint not null,
  primary key(user_id, event_id)
);

create table user_profiles(
  user_id bigint PRIMARY KEY REFERENCES users(id)
);

