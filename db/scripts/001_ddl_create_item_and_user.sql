create table if not exists item
(
    id          serial primary key,
    name        varchar   not null,
    description text      not null,
    created     timestamp not null,
    done        boolean default false
);

create table todo_user
(
    id       serial primary key,
    name     varchar,
    email    varchar,
    password text
);

ALTER TABLE todo_user
    ADD CONSTRAINT email_unique UNIQUE (email);
