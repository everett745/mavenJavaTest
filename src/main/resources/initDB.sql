create table user ( id varchar(255) primary key, address long, queue varchar(255), name text, phone text );

create table address ( id long primary key, city text, region text, district text );

create table queue ( id varchar(255) primary key, items text );

create table deal ( id varchar(255) primary key, name text, description text, address long, requests varchar(255), owner varchar(255), performer varchar(255), dealType text, object text, dealModel text, created_at long, price text, current_status text);

create table deal_history ( id text, text text, status text, created_at long);

create table company ( id varchar(255), employees text, deals text);

INSERT INTO ADDRESS VALUES (1, 'Адыгейск', 'Адыгея', 'Южный');
INSERT INTO ADDRESS VALUES (2, 'Майкоп', 'Адыгея', 'Южный');
INSERT INTO ADDRESS VALUES (3, 'Горно-Алтайск', 'Алтай', 'Сибирский');
INSERT INTO ADDRESS VALUES (490, 'Москва', 'Ростовская область', 'Центральный');
INSERT INTO ADDRESS VALUES (744, 'Ростов-на-Дону', 'Ростовская область', 'Южный');