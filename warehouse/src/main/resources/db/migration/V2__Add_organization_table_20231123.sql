create table organization (id int8 not null, primary key (id));
alter table warehouse add column organization_id int8 not null;
alter table driver add constraint FKn84qa625n5hhlitexbhbpjbj9 foreign key (warehouse_id) references warehouse;
alter table warehouse add constraint FK363w26pip2e3j8p65pao5xkvc foreign key (organization_id) references organization;