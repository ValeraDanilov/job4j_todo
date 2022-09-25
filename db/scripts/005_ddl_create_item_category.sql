create table item_category
(
    id      serial primary key,
    item_id int not null references item (id),
    category_id int not null references category (id)
);
