create table item
   (
      id serial primary key,
      name varchar not null unique,
      description text not null,
      created timestamp not null,
      done boolean default false
);
