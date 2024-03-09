create table address (latitude float(53) not null, longitude float(53) not null, id uuid not null, primary key (id));
create table client_organization (address_id uuid not null unique, client_id uuid not null, bank_name varchar(255), bik varchar(255), client_account varchar(255), correspondent_account varchar(255), inn varchar(255), name varchar(255), primary key (client_id));
create table order_document (type smallint check (type between 0 and 2), id uuid not null, order_id uuid, name varchar(255), primary key (id));
create table order_event (registered_at timestamp(6), id uuid not null, initiator_id uuid, order_id uuid, new_payment_status varchar(255) check (new_payment_status in ('PENDING_PAYMENT','PAID','PARTIALLY_REFUNDED','REFUNDED')), new_status varchar(255) check (new_status in ('NEW','IN_PROGRESS','READY_TO_DELIVER','DELIVERY','DONE','REJECTED','RETURNED','PARTIALLY_RETURNED','ABORTED')), primary key (id));
create table ordered_product (price numeric(38,2), quantity integer not null, returned_quantity integer not null, order_id uuid not null, product_id uuid not null, primary key (order_id, product_id));
create table orders (actual_order_address_id uuid not null unique, id uuid not null, organization_id uuid not null, warehouse_id uuid, payment_status varchar(255) check (payment_status in ('PENDING_PAYMENT','PAID','PARTIALLY_REFUNDED','REFUNDED')), status varchar(255) check (status in ('NEW','IN_PROGRESS','READY_TO_DELIVER','DELIVERY','DONE','REJECTED','RETURNED','PARTIALLY_RETURNED','ABORTED')), primary key (id));
alter table if exists client_organization add constraint FKc6c159duapbaxxncqtgw650x5 foreign key (address_id) references address;
alter table if exists order_document add constraint FKta08ufoh8gu4njcftqin29dg9 foreign key (order_id) references orders;
alter table if exists order_event add constraint FKmxa81m3f6pqs2onpwwt8ncuma foreign key (order_id) references orders;
alter table if exists ordered_product add constraint FK2nijit240cv1lx2ak4x5cakm8 foreign key (order_id) references orders;
alter table if exists orders add constraint FKp6pg2sssqfoncpmvgvvab8km9 foreign key (actual_order_address_id) references address;
alter table if exists orders add constraint FK7s4iob2qwdipp34kbjlsqp7d9 foreign key (organization_id) references client_organization;