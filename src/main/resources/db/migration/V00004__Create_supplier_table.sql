create table if not exists supplier
(
    supplier_id  bigint primary key generated by default as identity,
    title        varchar(50) not null,
    surname      varchar(50) not null,
    address      varchar(50) not null,
    phone_number varchar(20) not null
);