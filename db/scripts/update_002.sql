ALTER TABLE item ADD COLUMN user_item int not null references todo_user (id);
