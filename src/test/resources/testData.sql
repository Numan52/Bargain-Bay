create table users
(
    id         uuid not null
        primary key,
    email      varchar(255)
        constraint uk6dotkott2kjsp8vw4d0m25fb7
            unique,
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255),
    username   varchar(255)
        constraint ukr43af9ap4edm43mmtq01oddj6
            unique
);


create table category
(
    id        uuid not null primary key,
    name      varchar(255),
    parent_id uuid
        constraint fk2y94svpmqttx80mshyny85wqr
            references category
);

create table ad
(
    id                 uuid              not null
        primary key,
    condition          varchar(255)      not null,
    description        varchar(1000)     not null,
    price              real              not null,
    title              varchar(255)      not null,
    user_id            uuid
        constraint fktofr7l4mo2aajd4mm1k7n93a6
            references users,
    created_at         timestamp(6)      not null,
    has_priority       boolean           not null,
    last_bumped_at     timestamp(6)      not null,
    views_count        integer default 0 not null,
    title_tokens       tsvector,
    description_tokens tsvector,
    category_id        uuid
        constraint fkb4xjnvgyd7go1ehcnjgg7usqi
            references category
);


create table ad_image
(
    id    uuid not null
        primary key,
    url   varchar(255)
        constraint uk5flaad8starqpgby3cyvsvt3v
            unique,
    ad_id uuid
        constraint fk7c3vmaufihouhhht18f7vyq08
            references ad
);


-- Insert User
INSERT INTO users (id, first_name, last_name, password, username, email)
VALUES (gen_random_uuid(), 'Martin', 'Smith', 'password', 'martin11', 'martin@gmail.com');

-- Insert Categories
INSERT INTO category (id, parent_id, name)
VALUES
    ('aefb83ad-4c9f-4c6a-b599-d8f67f4a1676', NULL, 'Electronics'),
    (gen_random_uuid(), 'aefb83ad-4c9f-4c6a-b599-d8f67f4a1676', 'Laptops'),
    (gen_random_uuid(), NULL, 'Books');

-- Insert Ads
INSERT INTO ad (id, title, price, description, condition, created_at, category_id, last_bumped_at, user_id, has_priority)
VALUES
    (gen_random_uuid(), 'Book1', 10, 'book description', 'new', NOW(),
     (SELECT id FROM category WHERE name = 'Books'), NOW(),
     (SELECT id FROM users WHERE username = 'martin11'), false),

    (gen_random_uuid(), 'Gaming laptop 1', 500, 'Gaming laptop description', 'new', NOW(),
     (SELECT id FROM category WHERE name = 'Laptops'), NOW(),
     (SELECT id FROM users WHERE username = 'martin11'), false),

    (gen_random_uuid(), 'Gaming laptop 2', 600, 'Gaming laptop description 2', 'new', NOW(),
     (SELECT id FROM category WHERE name = 'Laptops'), NOW(),
     (SELECT id FROM users WHERE username = 'martin11'), false),

    (gen_random_uuid(), 'Printer', 600, 'Printer description', 'new', NOW(),
     (SELECT id FROM category WHERE name = 'Electronics'), NOW(),
     (SELECT id FROM users WHERE username = 'martin11'), false);
