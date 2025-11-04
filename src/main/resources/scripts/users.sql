create table users
(
    id       bigint default nextval('user_id_seq'::regclass) not null
        constraint user_pk
            primary key,
    username varchar                                         not null,
    password varchar                                         not null
);

alter table users
    owner to postgres;

