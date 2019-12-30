create table passages (
	id BIGSERIAL primary key,
	title text not null,
	comment_number integer default 0,
	like_number integer default 0,
	list_image bigint default 0,
	public_time date default now(),
	type smallint not null default 1,
	owner bigint references users(id)
)