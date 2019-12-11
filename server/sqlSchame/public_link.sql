create table public_link (
	user_id bigint,
	occupation_id bigint,
	primary key (user_id, occupation_id),
	foreign key (user_id) references users(id),
	foreign key (occupation_id) references occupation(id)
)