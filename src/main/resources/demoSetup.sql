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

-- Medarbejdere
INSERT INTO medArbejdere (navn, adgangskode, stilling) VALUES
    ('demo' , 'demo' , 'DEMO'      ),
    ('demo1', 'demo1', 'DATA'      ),
    ('demo2', 'demo2', 'SKADE'     ),
    ('demo3', 'demo3', 'FORRETNING');

-- Biltyper
INSERT INTO bilType (mærke, model, udstyrsniveau, stålPris, afgift, udledning_Co2) VALUES
    ('Toyota'    ,'Corolla','Basic'      ,200000,40000,95 ),
    ('Honda'     ,'Civic'  ,'Deluxe'     ,220000,42000,90 ),
    ('Ford'      ,'Focus'  ,'Premium'    ,210000,41000,92 ),
    ('BMW'       ,'320i'   ,'Sport'      ,350000,70000,120),
    ('Audi'      ,'A3'     ,'Business'   ,330000,66000,110),
    ('Volkswagen','Golf'   ,'Comfortline',280000,56000,100),
    ('Hyundai'   ,'Ioniq'  ,'Electric'   ,300000,11   ,1  );

-- Lagerlokationer
INSERT INTO lager (navn, adresse) VALUES
    ('DS-Forhandler', ' '),
    ('Roskilde', '123 Main St'   ),
    ('Nakskov' , '456 Side St'   ),
    ('Odense'  , '789 Back St'   ),
    ('Aalborg' , '135 North Ave' ),
    ('Esbjerg' , '369 East Lane' );

-- Biler (inkl. kørteKm for simulere vore logik for solgte biler)
INSERT INTO bil (vognNummer, stelNummer, bilType_Id, lager_Id, status, kørteKm) VALUES
    ('AB12345', 'WAUZZZ8V4DN674512', 1, 1, 'TILGAENGELIG', 0     ),
    ('CD67890', '1HGBH41JXMN109169', 2, 2, 'TILGAENGELIG', 0     ),
    ('EF56789', '1FMCU0C72CK451093', 3, 3, 'TILGAENGELIG', 0     ),
    ('XY92831', '1HGCM82633A004321', 4, 4, 'TILGAENGELIG', 0     ),
    ('QT63829', '2FTRX18W1XCA98765', 5, 4, 'TILGAENGELIG'   , 0     ),
    ('BH10475', 'WDBUF70J94A345678', 6, 5, 'UNLIMITED'   , 0     ),
    ('KU73190', '3N1AB7AP7HY256789', 7, 5, 'UNLIMITED'   , 0     ),
    ('ZA87364', 'JN1CV6EK9BM234567', 4, 6, 'UNLIMITED'     , 0     ),
    ('MW28374', '1N4AL3AP9FC198765', 5, 6, 'LIMITED'     , 0     ),
    ('LD98430', 'WA1DGAFE6FD017654', 6, 1, 'SOLGT'       , 120000),
    ('RP19383', 'SALWR2VF2FA623890', 7, 2, 'SOLGT'       , 98000 ),
    ('TE48923', 'KM8J33A40GU265431', 4, 3, 'SOLGT'       , 110500),
    ('CG57492', '5UXWX9C5XG0D34567', 5, 4, 'SOLGT'       , 102000);

-- Lejeaftaler (kun for biler med status 'udlejet' eller 'solgt')

-- UNLIMITED aftaler (udlejet biler)
INSERT INTO lejeAftaler (kunde_Navn, vognNummer, startDato, slutDato, detaljer) VALUES
    ('Anders Hansen', 'BH10475', '2025-02-01', '2025-12-01', 'Unlimited aftale - minimum 3 måneder- test Tilføj måneder'),
    ('Mette Jensen' , 'KU73190', '2025-09-12', '2025-12-12', 'Unlimited aftale - minimum 3 måneder- Test Aflys = bil tilgængelig '  ),
    ('Peter Nielsen', 'ZA87364', '2025-01-01', '2025-04-01', 'Unlimited aftale - minimum 3 måneder-  test afslut = bil tilbageleveret');

-- LIMITED aftaler (udlejet biler)
INSERT INTO lejeAftaler (kunde_Navn, vognNummer, startDato, slutDato, detaljer) VALUES
    ('Lone Sørensen', 'MW28374', '2024-11-01', '2025-04-01', 'Limited 5 måneder - test afslut = bil tilbageleveret');

-- SOLGTE aftaler (status 'solgt')
INSERT INTO lejeAftaler (kunde_Navn, vognNummer, startDato, slutDato, detaljer) VALUES
    ('Klaus Mortensen', 'LD98430', '2024-01-01', '2024-09-01', 'Solgt bil, limited lejeaftale over 8 måneder'),
    ('Anette Pedersen', 'RP19383', '2024-02-01', '2024-10-01', 'Solgt bil, limited lejeaftale over 8 måneder'),
    ('Thomas Olsen'   , 'TE48923', '2024-03-01', '2024-11-01', 'Solgt bil, limited lejeaftale over 8 måneder'),
    ('Sofie Hansen'   , 'CG57492', '2024-04-01', '2024-12-01', 'Solgt bil, limited lejeaftale over 8 måneder');

-- Notationer - kun bil (NULL i aftale_Id)
INSERT INTO notationer (aftale_Id, vognNummer, beskrivelse, pris) VALUES
    (NULL, 'AB12345', 'Serviceeftersyn udført', 2500),
    (NULL, 'CD67890', 'Ny vinterdæk monteret' , 3200),
    (NULL, 'EF56789', 'Lakskade på bagskærm'  , 4500);

-- Notationer - tilknyttet lejeaftaler (aftale_Id + vognNummer)
-- Antager aftale_Id i rækkefølge, som de blev indsat ovenfor:

-- Udlejet biler lejeaftaler: aftale_Id 6-9
-- Solgte biler lejeaftaler: aftale_Id 10-13

INSERT INTO notationer (aftale_Id, vognNummer, beskrivelse, pris) VALUES

-- Udlejet biler notationer:
    (1, 'BH10475', 'Rengøring efter lejeperiode', 500),
    -- (2, 'KU73190', 'Ekstra kilometer afregnet'  , 1200),
    (3, 'ZA87364', 'Forsinkelse i aflevering'   , 800),
    (4, 'MW28374', 'Småskader repareret'        , 2200),

-- Solgte biler notationer:
    (5, 'LD98430', 'Stenslag på forrude'        , 1500),
    (6, 'RP19383', 'Ridser i højre dør'         , 2000),
    (7, 'TE48923', 'Bule på bagklap'            , 3500),
    (8, 'CG57492', 'Mindre lakskade på kofanger', 1800);
