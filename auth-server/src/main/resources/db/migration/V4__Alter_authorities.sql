alter table authority add column if not exists basic boolean default false;

update authority set name = 'ROLE_SUPPORT', description = 'Сотрудник поддержки' where name = 'ROLE_ADMIN';
update authority set name = 'ROLE_ADMIN', description = 'Администратор' where name = 'ROLE_HEAD_ADMIN';

update authority set basic = true;
