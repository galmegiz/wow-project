create table wow_token (
    last_update_timestamp bigint not null,
    price bigint not null,
    primary key (last_update_timestamp)
 )
 create index idx_wow_token_last_update on wow_token (last_update_timestamp desc)