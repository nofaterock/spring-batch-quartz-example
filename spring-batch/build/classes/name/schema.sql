create table Pay (
	id           bigint identity (1,1) not null primary key,
	amount       bigint,
	tx_name      varchar(255),
	tx_date_time datetime
);

create table Pay2 (
	id           bigint identity (1,1) not null primary key,
	amount       bigint,
	tx_name      varchar(255),
	tx_date_time datetime
);

insert into Pay (amount, tx_name, tx_date_time)
VALUES (1000, 'trade1', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (2000, 'trade2', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (3000, 'trade3', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (4000, 'trade4', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (5000, 'trade5', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (6000, 'trade6', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (7000, 'trade7', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (8000, 'trade8', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (9000, 'trade9', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (10000, 'trade10', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (11000, 'trade11', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (12000, 'trade12', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (13000, 'trade13', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (14000, 'trade14', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (15000, 'trade15', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (16000, 'trade16', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (17000, 'trade17', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (18000, 'trade18', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (19000, 'trade19', '2018-09-10 00:00:00');
insert into Pay (amount, tx_name, tx_date_time)
VALUES (20000, 'trade20', '2018-09-10 00:00:00');

