CREATE TABLE IF NOT EXISTS base_ingredient
(
    id       VARCHAR(200) not null,
    name     varchar(255),
    singular varchar(255),
    synonyms TEXT[],
    primary key (id)
);

CREATE TABLE IF NOT EXISTS base_ingredient_children
(
    base_ingredient_id VARCHAR(200) not null,
    children_id        VARCHAR(200) not null,
    primary key (base_ingredient_id, children_id)
);

CREATE TABLE IF NOT EXISTS chef_user
(
    id                      VARCHAR(200) not null,
    account_non_expired     boolean      not null,
    account_non_locked boolean not null,
    credentials_non_expired boolean not null,
    enabled boolean not null,
    joined_at timestamp with time zone,
                            name varchar (255) not null,
    password varchar
(
    255
),
    username varchar
(
    255
),
    primary key
(
    id
)
    );

CREATE TABLE IF NOT EXISTS chef_user_authorities
(
    chef_user_id varchar
(
    200
) REFERENCES chef_user
(
    id
),
    authorities varchar
(
    200
),
    primary key
(
    chef_user_id,
    authorities
)
    );

CREATE TABLE IF NOT EXISTS image
(
    id VARCHAR
(
    200
) not null,
    owner_id VARCHAR
(
    200
) REFERENCES chef_user
(
    id
),
    primary key
(
    id
)
    );

CREATE TABLE IF NOT EXISTS recipe
(
    id           VARCHAR(200)             not null,
    created_at   timestamp with time zone not null,
    difficulty   int4,
    duration     int4                     not null,
    instruction  varchar(255),
    name         varchar(255),
    portions     int4                     not null,
    privacy      boolean                  not null,
    relevance    numeric(19, 2),
    average_rating double precision,
    owner_id VARCHAR
(
    200
) REFERENCES chef_user (id),
    thumbnail_id VARCHAR(200) REFERENCES image (id),
    primary key (id)
);

CREATE TABLE IF NOT EXISTS chef_user_favorite_recipes
(
    chef_user_id        VARCHAR(200) not null REFERENCES chef_user (id),
    favorite_recipes_id VARCHAR(200) not null REFERENCES recipe (id),
    primary key (chef_user_id, favorite_recipes_id)
);

CREATE TABLE IF NOT EXISTS cookbook
(
    id           VARCHAR(200) not null,
    created_at   timestamp with time zone,
    name         varchar(255),
    privacy      boolean      not null,
    owner_id     VARCHAR(200) REFERENCES chef_user (id),
    thumbnail_id VARCHAR(200) REFERENCES image (id),
    primary key (id)
);

CREATE TABLE IF NOT EXISTS cookbook_content
(
    cookbook_id VARCHAR(200) not null REFERENCES cookbook (id),
    recipe_id   VARCHAR(200) not null REFERENCES recipe (id),
    primary key (cookbook_id, recipe_id)
);

CREATE TABLE IF NOT EXISTS ingredient
(
    id                 VARCHAR(200) not null,
    amount             numeric(19, 2),
    text               varchar(255),
    base_ingredient_id VARCHAR(200) REFERENCES base_ingredient (id),
    primary key (id)
);

CREATE TABLE IF NOT EXISTS rating
(
    value
    double
    precision,
    recipe_id
    VARCHAR
(
    200
) REFERENCES recipe (id),
    user_id   VARCHAR(200) REFERENCES chef_user (id),
    primary key (recipe_id, user_id)
);

CREATE TABLE IF NOT EXISTS recipe_ingredients
(
    recipe_id      VARCHAR(200) not null REFERENCES recipe (id),
    ingredients_id VARCHAR(200) not null REFERENCES ingredient (id),
    primary key (recipe_id, ingredients_id)
);

CREATE TABLE IF NOT EXISTS tag
(
    id   VARCHAR(200) not null,
    name varchar(255),
    primary key (id)
);

CREATE TABLE IF NOT EXISTS recipe_tagging
(
    recipe_id VARCHAR(200) not null REFERENCES recipe (id),
    tag_id    VARCHAR(200) not null REFERENCES tag (id),
    primary key (recipe_id, tag_id)
);

alter TABLE chef_user
    add constraint UK_bpqxcphta4i94xiikqnmvvs9n unique (username);
