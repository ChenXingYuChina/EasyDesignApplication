create table subcomment (
	passage_id bigint,
	father integer,
	position smallint not null,
	like_number smallint default 0,
	content text not null,
	owner bigint not null references users(id),
	primary key (passage_id, father, position),
	foreign key (passage_id, father) references comment(passage_id, position)
)