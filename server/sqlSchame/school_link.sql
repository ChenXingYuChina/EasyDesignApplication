create table school_link (
	school_id bigint,
	public boolean default true,
	diploma smallint,
	user_id bigint,
	primary key (school_id, user_id, diploma),
	foreign key (school_id) references school(id),
	foreign key (user_id) references Users(id)
)