create schema if not exists beam;
use beam;

create table beam
(
    first_name   varchar(256) not null,
    last_name    varchar(256) not null,
    company_name varchar(256) not null,
    address      varchar(256) not null,
    city         varchar(256) not null,
    country      varchar(256) not null,
    postal       varchar(256) not null,
    phone1       varchar(256) not null,
    phone2       varchar(256) not null,
    email        varchar(256) not null,
    web          varchar(256) not null
);