create table unauthorized (
    user_id bigint primary key references users(id)
)