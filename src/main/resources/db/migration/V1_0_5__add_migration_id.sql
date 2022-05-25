ALTER TABLE chef_user
    ADD COLUMN migration_id int;

ALTER TABLE base_ingredient
    ADD COLUMN migration_id int;

ALTER TABLE ingredient
    ADD COLUMN migration_id int;

ALTER TABLE recipe
    ADD COLUMN migration_id int;

ALTER TABLE cookbook
    ADD COLUMN migration_id int;

ALTER TABLE image
    ADD COLUMN migration_id int;

ALTER TABLE rating
    ADD COLUMN migration_id int;

