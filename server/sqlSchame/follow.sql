create table follow (
	a bigint references users(id),
	b bigint references users(id),
	primary key (a, b)
)