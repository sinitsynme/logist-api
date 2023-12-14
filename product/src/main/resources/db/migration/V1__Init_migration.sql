create sequence hibernate_sequence start 1 increment 1;
create table manufacturer (id int8 not null, contact_number varchar(255), name varchar(255), primary key (id));
create table product (id uuid not null, description varchar(255), is_packaged boolean not null, name varchar(255), path_to_image varchar(255), price numeric(19, 2), quantity_in_package int4 not null, volume float8 not null, warehouse_code varchar(255), weight float8 not null, manufacturer_id int8 not null, category_id uuid, primary key (id));
create table product_category (id uuid not null, category_code varchar(255), category_name varchar(255), primary key (id));
alter table product add constraint FK89igr5j06uw5ps04djxgom0l1 foreign key (manufacturer_id) references manufacturer;
alter table product add constraint FK5cypb0k23bovo3rn1a5jqs6j4 foreign key (category_id) references product_category;