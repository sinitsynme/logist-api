create sequence hibernate_sequence start 1 increment 1;
create table address (id uuid not null, latitude float8 not null, longitude float8 not null, primary key (id));
create table cargo_truck (id uuid not null, created_at date, registration_number varchar(255) not null, updated_at date, updated_by varchar(255), cargo_truck_type_id varchar(255) not null, warehouse_id int8, primary key (id));
create table cargo_truck_type (brand varchar(255) not null, max_volume float8, max_weight float8, primary key (brand));
create table destination (id uuid not null, completed_at date, order_id uuid not null, status varchar(255), address_id uuid not null, primary key (id));
create table driver (id int8 not null, created_at date, driving_license_number varchar(255) not null, first_name varchar(255) not null, last_name varchar(255) not null, patronymic varchar(255), updated_at date, updated_by varchar(255), warehouse_id int8, primary key (id));
create table holiday (id uuid not null, holiday_type varchar(255), period_from date not null, period_to date not null, driver_id int8 not null, primary key (id));
create table organization (id int8 not null, name varchar(255) not null, primary key (id));
create table route (id uuid not null, completed_at date, planned_at date, route_status varchar(255), cargo_truck_id uuid, driver_id int8, warehouse_id int8, primary key (id));
create table route_destination_list (route_id uuid not null, destination_list_id uuid not null, order_index int4 not null, primary key (route_id, order_index));
create table stored_product (product_id uuid not null, quantity int4 not null, reserved_quantity int4 not null, warehouse_id int8 not null, primary key (product_id));
create table warehouse (id int8 not null, contact_number varchar(255), email varchar(255), name varchar(255) not null, address_id uuid not null, organization_id int8 not null, primary key (id));
alter table route_destination_list add constraint UK_jr635f48ahbtb2jl3omvq5rd0 unique (destination_list_id);
alter table cargo_truck add constraint FK3qo8eswrxreubfal0778uu4un foreign key (cargo_truck_type_id) references cargo_truck_type;
alter table cargo_truck add constraint FKm4mafa606yip181jar3b85rvg foreign key (warehouse_id) references warehouse;
alter table destination add constraint FKn0obgfthaq1r8ku3ysej74yk foreign key (address_id) references address;
alter table driver add constraint FKn84qa625n5hhlitexbhbpjbj9 foreign key (warehouse_id) references warehouse;
alter table holiday add constraint FKqfy796o6y6eeym78uek3v3k7s foreign key (driver_id) references driver;
alter table route add constraint FK9icafeautd9mlegj4haqwqdm5 foreign key (cargo_truck_id) references cargo_truck;
alter table route add constraint FKh19pmfiodsqk8cx1l68h0all3 foreign key (driver_id) references driver;
alter table route add constraint FKpjnf93f6os9nhm0qpnrkgw0or foreign key (warehouse_id) references warehouse;
alter table route_destination_list add constraint FK5e5uhh4mpkk3t1q8jxhtqbwux foreign key (destination_list_id) references destination;
alter table route_destination_list add constraint FKjqvp9cdje4iud28onybeygp6o foreign key (route_id) references route;
alter table stored_product add constraint FK9b9yivqkhr0o8ys105qrf6jah foreign key (warehouse_id) references warehouse;
alter table warehouse add constraint FKp7xymgre8vt94ihf75e9movyt foreign key (address_id) references address;;
alter table warehouse add constraint FK363w26pip2e3j8p65pao5xkvc foreign key (organization_id) references organization;