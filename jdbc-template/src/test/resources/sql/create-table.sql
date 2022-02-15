create table users
(
    id    bigserial primary key,
    name  varchar(100) not null,
    email varchar(50)  not null,
    constraint name_email_unq
        unique (name, email)
);