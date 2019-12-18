create table workshop (
	id serial primary key,
	level smallint not null default 0,
	people_number smallint not null default 1,
	passage_number integer not null default 0,
	fans_number integer not null default 0,
	head_image bigint,
	name varchar(20) not null
)