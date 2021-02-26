create table user ( id varchar(255) primary key, address long, queue varchar(255), name text, phone text );create table address ( id long primary key, city text, region text, district text );create table queue ( id varchar(255) primary key, items text );create table deal ( id varchar(255) primary key, name text, description text, address long, requests varchar(255), owner varchar(255), performer varchar(255), dealType text, object text, dealModel text, created_at long, price text, current_status text);create table deal_history ( id text, text text, status text, created_at long);create table company ( id varchar(255), employees text, deals text);

create table test_entity (
     id varchar(255) GENERATED ALWAYS AS IDENTITY NOT NULL primary key,
     name text,
     description text,
     dateCreate text,
     check_ text,
     region text,
     district text,
     city text
);

create table address (id long, city text, region text, district text);

insert into ADDRESS values ( 0, 'Москва', 'Московская', 'Центральный' );

create table account (
    id varchar(255) GENERATED ALWAYS AS IDENTITY NOT NULL primary key,
    owner text,
    balance decimal,
    interest_rate decimal,
    creditLimit decimal,
    overdraftFee decimal
);

delete from TEST_ENTITY where id != '';

drop table test_entity;
drop table account;