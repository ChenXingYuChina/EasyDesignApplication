create table comment (
	passage_id bigint references passages(id),
	position integer not null,
	owner bigint not null references users(id),
	like_number integer default 0,
	sub_comment smallint default 0,
	primary key (passage_id, position)
)
