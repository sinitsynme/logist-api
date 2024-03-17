alter table orders add column created_at timestamp(6);
update orders set created_at = now() where true;
alter table orders alter column created_at set not null;
alter table orders add column finished_at timestamp(6);
