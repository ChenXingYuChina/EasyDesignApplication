create table workshop_passage (
	passage_id bigint references passages(id),
	workshop_id integer references workshop(id),
	primary key (passage_id)
)