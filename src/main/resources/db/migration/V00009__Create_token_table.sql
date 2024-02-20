create table if not exists token
(
    expired    boolean not null,
    id         integer not null
        primary key,
    revoked    boolean not null,
    account_id integer
        constraint fkiblu4cjwvyntq3ugo31klp1c6
            references account,
    token      varchar(255)
        unique,
    token_type varchar(255)
        constraint token_token_type_check
            check ((token_type)::text = 'BEARER'::text)
);

create sequence if not exists token_seq
    increment by 50;

alter sequence token_seq owner to postgres;