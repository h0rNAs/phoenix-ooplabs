create table IF NOT EXISTS users
(
    id       bigserial
        constraint user_pk
            primary key,
    username varchar                                         not null,
    password varchar                                         not null
);

alter table users
    owner to postgres;

