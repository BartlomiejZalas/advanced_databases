\timing on

BEGIN;

UPDATE events SET place_id=(
	SELECT p.place_id 
	FROM places p
	ORDER BY st_distance(
		p.location, 
		(SELECT location FROM places WHERE place_id = 1)
	)
	LIMIT 1 OFFSET 1
) 
WHERE place_id = 1;

COMMIT;
