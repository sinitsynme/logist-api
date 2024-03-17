alter table orders drop constraint if exists orders_actual_order_address_id_key;
alter table orders drop constraint if exists FKp6pg2sssqfoncpmvgvvab8km9;
alter table orders drop column actual_order_address_id;
alter table orders add column actual_order_address_id uuid not null;
alter table if exists orders add constraint actual_address_id_not_null foreign key (actual_order_address_id) references address;

