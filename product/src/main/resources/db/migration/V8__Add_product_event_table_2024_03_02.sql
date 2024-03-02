create table product_event (id uuid not null, product_id uuid not null, registered_at timestamp not null, initiator_id uuid not null, new_status varchar(255) not null, primary key (id));
