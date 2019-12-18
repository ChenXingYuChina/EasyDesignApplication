create table workshop_member_link (
	workshop_id integer references workshop(id),
	user_id bigint references users(id),
	position smallint not null,
	primary key (workshop_id, user_id),
	unique (user_id)
)