-- liquibase formatted sql

-- changeset belchikdm:1
create table notification_task(
    id bigserial primary key,
    chat_id bigint not null,
    text_notification varchar(512) not null,
    datetime_notification timestamp not null,
    datetime_create_notification timestamp not null
);