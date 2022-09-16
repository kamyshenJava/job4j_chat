insert into roles (role) values ('ROLE_USER'), ('ROLE_ADMIN')
on conflict do nothing;

insert into persons (username, enabled, password, role_id)
values ('admin', true, '123456',
        (select id from roles where role = 'ROLE_ADMIN'))
on conflict do nothing;