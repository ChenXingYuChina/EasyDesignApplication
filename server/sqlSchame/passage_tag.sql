create table passage_tag (
	passage_id bigint references passages(id),
	passage_type smallint not null,
	primary key (passage_id, passage_type)
)