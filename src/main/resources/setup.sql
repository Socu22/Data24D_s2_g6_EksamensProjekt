DROP DATABASE IF EXISTS CarDatabase;
CREATE DATABASE CarDatabase;
USE CarDatabase;

create table medArbejdere(medArbejder_Id int primary key , navn varchar(50), adgangskode varchar(50), stilling varchar(50));

create table bilType(bilType_Id int primary key,mærke varchar(50), model varchar(50), udstyrsniveau varchar(50), stålPris double, afgift double, udledning_Co2 double);
create table lager(lager_Id int primary key,navn varchar(50),adresse varchar(50));
create table bil(vognNummer varchar(7) primary key, stelNummer varchar(17),bilType_Id int, lager_Id int,status varchar(20), unique (stelNummer), foreign key (bilType_Id) references bilType(bilType_Id), foreign key (lager_Id) references lager(lager_Id));
create table kunde(kunde_Id int primary key, navn varchar(50));
create table lejeAftaler(aftale_Id int primary key, kunde_Id int, vognNummer varchar(7), foreign key (kunde_Id) references kunde(kunde_Id), foreign key (vognNummer) references bil(vognNummer));
create table notationer(notationer int primary key, aftale_Id int, beskrivelse varchar(255), pris double, foreign key (aftale_Id) references lejeAftaler(aftale_Id))
