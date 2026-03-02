create table account
(
    id         uuid                     not null primary key default gen_random_uuid(),
    created_at timestamp with time zone not null             default now(),
    updated_at timestamp with time zone not null             default now(),
    email      varchar(255)             not null unique,
    password   varchar(255)             not null
);