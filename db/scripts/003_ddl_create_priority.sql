CREATE TABLE priorities
(
    id       SERIAL PRIMARY KEY,
    name     TEXT,
    position int
);

ALTER TABLE item
    ADD COLUMN priority_id int
        REFERENCES priorities (id);

