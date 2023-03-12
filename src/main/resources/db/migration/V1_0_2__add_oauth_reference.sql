ALTER TABLE chef_user
    ADD COLUMN oauth_apple_id varchar(200) REFERENCES oauth_apple (id);
ALTER TABLE chef_user
    ADD COLUMN oauth_google_id varchar(200) REFERENCES oauth_google (id);