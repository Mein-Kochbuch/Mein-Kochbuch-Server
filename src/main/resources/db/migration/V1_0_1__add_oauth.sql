CREATE TABLE IF NOT EXISTS oauth_apple
(
    id VARCHAR
(
    200
) not null,
    access_token varchar
(
    200
),
    primary key
(
    id
)
    );

CREATE TABLE IF NOT EXISTS oauth_google
(
    id VARCHAR
(
    200
) not null,
    access_token varchar
(
    200
),
    primary key
(
    id
)
    );