create table user_refresh_token(user_id uuid not null, refresh_token varchar(500) not null, expires_at timestamp not null, primary key (user_id));
alter table if exists user_refresh_token add constraint user_id_fk1 foreign key (user_id) references users;
