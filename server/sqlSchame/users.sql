create table Users (
	id BIGSERIAL primary key,
	name varchar(40),
	Email text unique,
	phone_number varchar(20),
	password varchar(20),
	coin bigint,
	fans_number bigint default 0,
	follower_number bigint default 0,
	passage_number bigint default 0,
	head_image bigint,
	back_image bigint default null,
	identity_type smallint
)