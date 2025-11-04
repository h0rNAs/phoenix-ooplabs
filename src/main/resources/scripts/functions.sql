create table functions
(
    id         bigint default nextval('function_id_seq'::regclass) not null
        constraint function_pk
            primary key,
    user_id    bigint                                              not null,
    type       function_type                                       not null,
    name       varchar                                             not null,
    defenition json
);

alter table functions
    owner to postgres;

