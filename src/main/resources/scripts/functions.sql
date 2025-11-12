create table IF NOT EXISTS functions
(
    id                  bigserial not null
        constraint function_pk
            primary key,
    user_id             bigint  not null,
    name                varchar,
    function_type       varchar(16) check (function_type in ('SIMPLE', 'TABULATED', 'COMPOSITE')) not null,
    definition          jsonb
);

alter table functions
    owner to postgres;

