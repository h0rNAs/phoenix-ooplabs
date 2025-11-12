create table IF NOT EXISTS users
(
    id       bigserial
        constraint user_pk
            primary key,
    username varchar(16)      not null
        constraint users_pk
            unique,
    password varchar          not null
);

alter table users
    owner to postgres;

