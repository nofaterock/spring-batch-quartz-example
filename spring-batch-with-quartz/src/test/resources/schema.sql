create table Pay (
	id           bigint identity (1,1) not null primary key,
	amount       bigint,
	txName      varchar(255),
	txDateTime datetime
);

create table Pay2 (
	id           bigint identity (1,1) not null primary key,
	amount       bigint,
	txName      varchar(255),
	txDateTime datetime
);

insert into Pay (amount, txName, txDateTime)
VALUES (1000, 'trade1', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (2000, 'trade2', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (3000, 'trade3', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (4000, 'trade4', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (5000, 'trade5', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (6000, 'trade6', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (7000, 'trade7', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (8000, 'trade8', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (9000, 'trade9', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (10000, 'trade10', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (11000, 'trade11', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (12000, 'trade12', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (13000, 'trade13', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (14000, 'trade14', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (15000, 'trade15', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (16000, 'trade16', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (17000, 'trade17', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (18000, 'trade18', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (19000, 'trade19', '2018-09-10 00:00:00');
insert into Pay (amount, txName, txDateTime)
VALUES (20000, 'trade20', '2018-09-10 00:00:00');

