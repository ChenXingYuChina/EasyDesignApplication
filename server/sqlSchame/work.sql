create table work (
    id BIGSERIAL primary key,
	user_id bigint references users(id),
	start_Time date not null,
	end_time date,
	company text not null,
	occupation bigint references occupation(id),
	unique (user_id, start_time, end_time, company, occupation)
)