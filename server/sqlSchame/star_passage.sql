create table star_passage (
	passage_id bigint references passages(id),
	user_id bigint references users(id),
	primary key (passage_id, user_id)
)