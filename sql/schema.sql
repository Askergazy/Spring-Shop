create table categories
(
    id   serial8,
    name varchar not null,
    primary key (id)
);

create table products
(
    id          serial8,
    category_id int8    not null,
    name        varchar not null,
    price       int4    not null,
    primary key (id),
    foreign key (category_id) references categories (id)
);

create table options
(
    id          serial8,
    category_id int8    not null,
    name        varchar not null,
    primary key (id),
    foreign key (category_id) references categories (id)
);

create table values
(
    id         serial8,
    product_id int8    not null,
    option_id  int8    not null,
    value      varchar not null,
    primary key (id),
    foreign key (product_id) references products (id),
    foreign key (option_id) references options (id)
);
DROP TABLE IF EXISTS users CASCADE;




CREATE TABLE users
(
    id                serial8,
    role              int2,
    login             varchar,
    password          varchar,
    name              varchar,
    last_name         varchar,
    registration_data timestamp,
    primary key (id)
);



DROP TABLE cart_items CASCADE ;

CREATE TABLE cartItems
(
    id         serial8,
    quantity int4,
    product_id int8 not null,
    user_id    int8 not null,
    foreign key (product_id) references  products(id),
    foreign key (user_id) references  users(id),
    primary key (id)
);

CREATE TABLE orders
(
    id         serial8,
    user_id    int8 not null,
    status     int2,
    address    varchar,
    order_data timestamp,
    primary key (id),
    foreign key (user_id) references users
);

CREATE TABLE ordered_products
(
    id         serial8,
    order_id   int8 not null,
    product_id int8 not null,
    count      int2 not null,
    primary key (id),
    foreign key (order_id) references orders,
    foreign key (product_id) references products
);


CREATE TABLE reviews
(
    id          serial8,
    user_id     int8 not null,
    product_id  int8 not null,
    status      boolean,
    rating      int4,
    review_text varchar,
    review_date timestamp,
    foreign key (user_id) references users,
    foreign key (product_id) references products
);

ALTER TABLE orders
    ADD COLUMN status int2;



--     id          serial8,
--     category_id int8    not null,
--     name        varchar not null,
--     price       int4    not null,
--     primary key (id),
--     foreign key (category_id) references categories (id)
