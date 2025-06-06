-- DROP & CREATE database
DROP DATABASE IF EXISTS bilAbonnementDatabase;
CREATE DATABASE bilAbonnementDatabase;
USE bilAbonnementDatabase;

-- Tabeller
CREATE TABLE medArbejdere(
    medArbejder_Id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    navn           VARCHAR(50) NOT NULL,
    adgangskode    VARCHAR(50) NOT NULL,
    stilling       VARCHAR(50) NOT NULL
);

CREATE TABLE bilType(
    bilType_Id    INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    mærke         VARCHAR(50) NOT NULL,
    model         VARCHAR(50) NOT NULL,
    udstyrsniveau VARCHAR(50) NOT NULL,
    stålPris      DOUBLE      NOT NULL,
    afgift        DOUBLE      NOT NULL,
    udledning_Co2 DOUBLE      NOT NULL
);

CREATE TABLE lager(
    lager_Id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    navn     VARCHAR(50) NOT NULL,
    adresse  VARCHAR(50) NOT NULL
);

CREATE TABLE bil(
    vognNummer VARCHAR(7)  PRIMARY KEY,
    stelNummer VARCHAR(17) NOT NULL,
    bilType_Id INT         NOT NULL,
    lager_Id   INT         NOT NULL,
    status     VARCHAR(20) NOT NULL,
    kørteKm    DOUBLE      DEFAULT 0,
    FOREIGN KEY (bilType_Id) REFERENCES bilType(bilType_Id),
    FOREIGN KEY (lager_Id)   REFERENCES lager(lager_Id),
    UNIQUE      (stelNummer)
);

CREATE TABLE lejeAftaler(
    aftale_Id  INT  PRIMARY KEY AUTO_INCREMENT,
    kunde_Navn VARCHAR(50)  NOT NULL,
    vognNummer VARCHAR(7)   NOT NULL,
    startDato  DATE         NOT NULL,
    slutDato   DATE,
    betaltDato DATE,
    detaljer   VARCHAR(255) NOT NULL,
    FOREIGN KEY (vognNummer) REFERENCES bil(vognNummer)
);

CREATE TABLE notationer(
    notationer_id INT  PRIMARY KEY AUTO_INCREMENT,
    aftale_Id     INT,
    vognNummer    VARCHAR(7)   NOT NULL,
    beskrivelse   VARCHAR(255) NOT NULL,
    pris          DOUBLE       NOT NULL,
    FOREIGN KEY (aftale_Id)  REFERENCES lejeAftaler(aftale_Id),
    FOREIGN KEY (vognNummer) REFERENCES bil(vognNummer)
);
