alter table if exists client_organization drop constraint if exists FKc6c159duapbaxxncqtgw650x5;
alter table if exists client_organization drop column if exists address_id;
alter table if exists address add column if not exists organization_id varchar(255);
alter table if exists address drop constraint if exists FKkvkgwg4em275xfuiw2ocrtboq;
alter table if exists address add constraint FKkvkgwg4em275xfuiw2ocrtboq foreign key (organization_id) references client_organization;