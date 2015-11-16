\timing on

begin;

delete from users u
where u.user_id in (
	select e.user_id from events e
	join places p on p.place_id = e.place_id
	join users u2 on e.user_id = u2.user_id
	where st_dwithin(u2.location, p.location, 25000) = false and e.created_at > now() - interval '1 month'
	group by e.user_id
	having count(e.user_id) > 10
);

commit;
