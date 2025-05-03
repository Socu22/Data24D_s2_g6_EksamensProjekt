DROP DATABASE IF EXISTS CarDatabase;
CREATE DATABASE CarDatabase;
USE CarDatabase;

create table medArbejdere(medArbejder_Id int primary key auto_increment not null , navn varchar(50) not null, adgangskode varchar(50) not null , stilling varchar(50) not null );
create table bilType(bilType_Id int primary key auto_increment not null,mærke varchar(50) not null , model varchar(50) not null, udstyrsniveau varchar(50) not null, stålPris double not null , afgift double not null , udledning_Co2 double not null );
create table lager(lager_Id int primary key auto_increment not null ,navn varchar(50) not null ,adresse varchar(50) not null );
create table bil(vognNummer varchar(7) primary key not null , stelNummer varchar(17) not null,bilType_Id int not null, lager_Id int not null ,status varchar(20) not null, unique (stelNummer), foreign key (bilType_Id) references bilType(bilType_Id), foreign key (lager_Id) references lager(lager_Id));
create table kunde(kunde_Id int primary key auto_increment not null , navn varchar(50) not null );
create table lejeAftaler(aftale_Id int primary key auto_increment not null , kunde_Id int not null , vognNummer varchar(7) not null , foreign key (kunde_Id) references kunde(kunde_Id), foreign key (vognNummer) references bil(vognNummer));
create table notationer(notationer_id int primary key not null, aftale_Id int not null , beskrivelse varchar(255) not null, pris double not null , foreign key (aftale_Id) references lejeAftaler(aftale_Id));

insert into medArbejdere (navn, adgangskode, stilling)
values ('demo1','demo1','reg'), ('demo2','demo2','ska'), ('demo3','demo3','for');