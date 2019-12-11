create table link_media (
	id BIGSERIAL primary key,
	passage_id bigint unique references passages(id)
)