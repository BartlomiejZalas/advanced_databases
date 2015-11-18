-- 1. Create an index on column sold_amount in tickets table
CREATE INDEX tickets_sold_amount_idx ON tickets USING BTREE(sold_amount);

-- 2. Create a spatial index on column location in places table.
CREATE INDEX places_location_idx ON places USING GIST(location);

-- 3. Create an index on column ends_at in events table.
CREATE INDEX events_ends_at_idx ON events USING BTREE(ends_at);

-- 4. Create an index on column starts_at in events table.
CREATE INDEX events_starts_at_idx ON events USING BTREE(starts_at);

-- 6. Create a spatial index on column location in users table.
CREATE INDEX users_location_idx ON users USING GIST(location);

-- 7. Create an index on column created_at in events table.
CREATE INDEX events_created_at_idx ON events USING BTREE(created_at);

-- 8. Create an index on column place_id in events table.
CREATE INDEX events_place_id_idx ON events USING BTREE(place_id);

-- 9. Not mentioned index - index on tickets for event_id
CREATE INDEX tickets_event_id_idx ON tickets USING BTREE (event_id);

-- 5. Create a partitioning:
--    - 1 part - sold_amount < max_amount
--    - 2 part - sold_amount >= max_amount
--    on table tickets.
CREATE TABLE tickets_not_sold (
  CHECK ( sold_amount < max_amount )
) INHERITS (tickets);
CREATE TABLE tickets_sold (
  CHECK ( sold_amount >= max_amount )
) INHERITS (tickets);





