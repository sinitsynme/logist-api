create table authority (name varchar(255) not null, description varchar(255) not null, primary key (name));
create table users_authorities (user_id uuid not null, authority varchar(255) not null, primary key (user_id, authority));
alter table if exists users_authorities add constraint authority_id_fk1 foreign key (authority) references authority;
alter table if exists users_authorities add constraint user_id_fk1 foreign key (user_id) references users;