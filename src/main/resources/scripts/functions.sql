DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'function_type') THEN
        CREATE TYPE function_type AS ENUM('SIMPLE', 'TABULATED', 'COMPOSITE');
    END IF;
END$$;

create table IF NOT EXISTS functions
(
    id         bigserial not null
        constraint function_pk
            primary key,
    user_id    bigint                                              not null,
    type       function_type                                       not null,
    definition json
);

alter table functions
    owner to postgres;

