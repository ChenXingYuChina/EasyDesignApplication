create table passage_type (
	passage_id bigint references passages(id),
	passage_type smallint not null,
	primary key (passage_id)
)