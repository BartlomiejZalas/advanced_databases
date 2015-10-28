\timing on

SELECT e.name, AVG(r.value), 
	(SELECT sold_amount*price as profit FROM tickets WHERE event_id = e.event_id) 
FROM events e
LEFT JOIN ratings r ON r.event_id = e.event_id
GROUP BY e.event_id
HAVING  AVG(r.value) > 4
ORDER BY profit
LIMIT 10;
