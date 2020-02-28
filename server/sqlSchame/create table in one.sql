create table Users (
	id BIGSERIAL primary key,
	name varchar(40),
	Email text unique,
	phone_number varchar(20),
	password varchar(128) not null,
	coin bigint,
	fans_number bigint default 0,
	follower_number bigint default 0,
	passage_number bigint default 0,
	head_image bigint,
	back_image bigint default null,
	identity_type smallint
);

create table school (
	id BIGSERIAL primary key,
	country text,
	name text
);

create table occupation (
	id BIGSERIAL primary key,
	industry text not null,
	position text not null
);
create table school_link (
	school_id bigint,
	public boolean default true,
	diploma smallint,
	user_id bigint,
	primary key (school_id, user_id, diploma),
	foreign key (school_id) references school(id),
	foreign key (user_id) references Users(id)
);
create table work (
    id BIGSERIAL primary key,
	user_id bigint references users(id),
	start_Time date not null,
	end_time date,
	company text not null,
	occupation bigint references occupation(id),
	unique (user_id, start_time, end_time, company, occupation)
);
create table workshop (
	id serial primary key,
	level smallint not null default 0,
	people_number smallint not null default 1,
	passage_number integer not null default 0,
	fans_number integer not null default 0,
	head_image bigint,
	name varchar(20) not null
);
create table public_link (
	user_id bigint,
	occupation_id bigint,
	primary key (user_id, occupation_id),
	foreign key (user_id) references users(id),
	foreign key (occupation_id) references occupation(id)
);
create table passages (
	id BIGSERIAL primary key,
	title text not null,
	comment_number integer default 0,
	like_number integer default 0,
	list_image bigint default 0,
	public_time date default now(),
	type smallint not null default 1,
	owner bigint references users(id)
);
create table workshop_member_link (
	workshop_id integer references workshop(id),
	user_id bigint references users(id),
	position smallint not null,
	primary key (workshop_id, user_id),
	unique (user_id)
);
create table workshop_passage (
	passage_id bigint references passages(id),
	workshop_id integer references workshop(id),
	primary key (passage_id)
);
create table unauthorized (
    user_id bigint primary key references users(id)
);
create table comment (
	passage_id bigint references passages(id),
	position integer not null,
	owner bigint not null references users(id),
	like_number integer default 0,
	sub_comment smallint default 0,
	primary key (passage_id, position)
);
create table subcomment (
	passage_id bigint,
	father integer,
	position smallint not null,
	like_number smallint default 0,
	content text not null,
	owner bigint not null references users(id),
	primary key (passage_id, father, position),
	foreign key (passage_id, father) references comment(passage_id, position)
);
create table passage_tag (
	passage_id bigint references passages(id),
	passage_type smallint not null,
	primary key (passage_id, passage_type)
);
create table link_media (
	id BIGSERIAL primary key,
	passage_id bigint unique references passages(id)
);
create table follow (
	a bigint references users(id),
	b bigint references users(id),
	primary key (a, b)
);
create table star_passage (
	passage_id bigint references passages(id),
	user_id bigint references users(id),
	primary key (passage_id, user_id)
);