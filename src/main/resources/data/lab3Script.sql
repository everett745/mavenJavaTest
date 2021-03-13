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

drop table test_entity;

delete from TEST_ENTITY where id != '';

create table address (id long, city text, region text, district text);

insert into ADDRESS values ( 0, 'Москва', 'Московская', 'Центральный' );


---- LAB - 3

--------- MappedSuperClass
drop table if exists creditAccount;
drop table if exists debitAccount;

create table creditAccount (
    id varchar(255) GENERATED ALWAYS AS IDENTITY NOT NULL primary key,
    owner text,
    balance decimal,
    interest_rate decimal,
    creditLimit decimal
);

create table debitAccount (
  id varchar(255) GENERATED ALWAYS AS IDENTITY NOT NULL primary key,
  owner text,
  balance decimal,
  interest_rate decimal,
  overdraftFee decimal
);

--------- TablePerClass
drop table if exists hibernate_sequences;
drop table if exists creditAccount;
drop table if exists debitAccount;

create table creditAccount (
    id varchar(255) GENERATED ALWAYS AS IDENTITY NOT NULL primary key,
    owner text,
    balance decimal,
    interest_rate decimal,
    creditLimit decimal
);

create table debitAccount (
  id varchar(255) GENERATED ALWAYS AS IDENTITY NOT NULL primary key,
  owner text,
  balance decimal,
  interest_rate decimal,
  overdraftFee decimal
);
create table hibernate_sequences (
 next_val int,
 sequence_name varchar(255)
);

--------- SingleTable
drop table if exists hibernate_sequences;
drop table if exists account;

create table account (
                         id varchar(255) GENERATED ALWAYS AS IDENTITY NOT NULL primary key,
                         owner text,
                         balance decimal,
                         interest_rate decimal,
                         dtype text,
                         creditLimit decimal,
                         overdraftFee decimal
);
create table hibernate_sequences (
                                     next_val int,
                                     sequence_name varchar(255)
);

--------- JoinedTable
drop table if exists hibernate_sequences;
drop table if exists account;
drop table if exists creditAccount;
drop table if exists debitAccount;


create table account
(
    id integer not null primary key,
    owner text,
    balance decimal,
    interest_rate decimal
);
create table creditAccount
(
    id integer not null primary key,
    creditLimit decimal
);

create table debitAccount
(
    id integer not null primary key,
    overdraftFee decimal
);
create table hibernate_sequences (
     next_val int,
     sequence_name varchar(255)
);



drop table if exists account;
drop table if exists creditAccount;
drop table if exists debitAccount;
drop table if exists HIBERNATE_SEQUENCES;
drop table if exists test_entity;
drop table if exists ADDRESS_MAP;
drop table if exists ADDRESS_SET;
drop table if exists LIST;
drop table if exists MAP;
drop table if exists SET;
drop table if exists LAB4_ENTITY;