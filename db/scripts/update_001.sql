create table if not exists item
(
    id          serial primary key,
    name        varchar   not null unique,
    description text      not null,
    created     timestamp not null,
    done        boolean default false
);

create table todo_user
(
    id       serial primary key,
    name     varchar(250),
    email    varchar(250),
    password text
);

ALTER TABLE todo_user
    ADD CONSTRAINT email_unique UNIQUE (email);
