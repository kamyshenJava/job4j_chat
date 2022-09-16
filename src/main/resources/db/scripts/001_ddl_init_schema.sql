CREATE TABLE if not exists roles(
    id serial primary key,
    role text NOT NULL unique
);

create table if not exists persons(
    id serial primary key,
    username text NOT NULL unique,
    password text NOT NULL,
    enabled boolean default true,
    role_id int references roles(id)
);

create table if not exists rooms(
    id serial primary key,
    name text not null unique,
    description text
);

create table if not exists messages(
    id serial primary key,
    body text,
    created timestamp without time zone not null default now(),
    room_id int references rooms(id),
    person_id int references persons(id)
)



