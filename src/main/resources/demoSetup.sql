DROP DATABASE IF EXISTS bilAbonnementDatabase;
CREATE DATABASE bilAbonnementDatabase;
USE bilAbonnementDatabase;

CREATE TABLE medArbejdere(
                             medArbejder_Id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                             navn VARCHAR(50) NOT NULL,
                             adgangskode VARCHAR(50) NOT NULL,
                             stilling VARCHAR(50) NOT NULL
);

CREATE TABLE bilType(
                        bilType_Id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                        mærke VARCHAR(50) NOT NULL,
                        model VARCHAR(50) NOT NULL,
                        udstyrsniveau VARCHAR(50) NOT NULL,
                        stålPris DOUBLE NOT NULL,
                        afgift DOUBLE NOT NULL,
                        udledning_Co2 DOUBLE NOT NULL
);

CREATE TABLE lager(
                      lager_Id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                      navn VARCHAR(50) NOT NULL,
                      adresse VARCHAR(50) NOT NULL
);

CREATE TABLE bil(
                    vognNummer VARCHAR(7) PRIMARY KEY NOT NULL,
                    stelNummer VARCHAR(17) NOT NULL,
                    bilType_Id INT NOT NULL,
                    lager_Id INT NOT NULL,
                    status VARCHAR(20) NOT NULL,
                    UNIQUE (stelNummer),
                    FOREIGN KEY (bilType_Id) REFERENCES bilType(bilType_Id),
                    FOREIGN KEY (lager_Id) REFERENCES lager(lager_Id)
);

CREATE TABLE kunde(
                      kunde_Id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                      navn VARCHAR(50) NOT NULL
);

CREATE TABLE lejeAftaler(
                            aftale_Id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                            kunde_Id INT NOT NULL,
                            vognNummer VARCHAR(7) NOT NULL,
                            startDato date NOT NULL,
                            slutDato DATE NOT NULL,
                            detaljer varchar(255) NOT NULL,
                            FOREIGN KEY (kunde_Id) REFERENCES kunde(kunde_Id),
                            FOREIGN KEY (vognNummer) REFERENCES bil(vognNummer)
);

CREATE TABLE notationer(
                           notationer_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                           aftale_Id INT,
                           vognNummer varchar(7) NOT NULL,
                           beskrivelse VARCHAR(255) NOT NULL,
                           pris DOUBLE NOT NULL,
                           FOREIGN KEY (aftale_Id) REFERENCES lejeAftaler(aftale_Id),
                           FOREIGN KEY (vognNummer) REFERENCES bil(vognNummer)
);

INSERT INTO medArbejdere (navn, adgangskode, stilling)
VALUES ('demo1','demo1','Dataregistrering'), ('demo2','demo2','Skade_&_Udbedring'), ('demo3','demo3','Forretningsudviklere');

INSERT INTO bilType (mærke, model, udstyrsniveau, stålPris, afgift, udledning_Co2)
VALUES
    ('Toyota', 'Corolla', 'Basic', 200000, 40000, 95),
    ('Honda', 'Civic', 'Deluxe', 220000, 42000, 90),
    ('Ford', 'Focus', 'Premium', 210000, 41000, 92);

INSERT INTO lager (navn, adresse)
VALUES
    ('a', '123 Main St'),
    ('b ', '456 Side St');

INSERT INTO bil (vognNummer, stelNummer, bilType_Id, lager_Id, status)
VALUES
    ('AB12345', 'WAUZZZ8V4DN674512', 1, 1, 'Available'),
    ('CD67890', '1HGBH41JXMN109169', 2, 2, 'Rented'),
    ('EF56789', '1FMCU0C72CK451093', 3, 1, 'Maintenance');

INSERT INTO kunde (navn)
VALUES
    ('John Doe'),
    ('Jane Smith');

INSERT INTO lejeAftaler (kunde_Id, vognNummer, startDato, slutDato, detaljer)
VALUES
    (1, 'CD67890', '2023-01-01', '2023-01-15', 'Rental for John Doe'),
    (2, 'AB12345', '2023-02-01', '2023-02-28', 'Rental for Jane Smith'),
    (1, 'EF56789', '2023-03-01', '2023-03-10', 'Second rental for John Doe');


INSERT INTO notationer (aftale_Id, vognNummer, beskrivelse, pris)
VALUES
    (1, 'CD67890', 'Extra insurance', 500),
    (1, 'CD67890', 'Additional driver', 300),
    (2, 'AB12345', 'GPS device', 100),
    (3, 'EF56789', 'Child seat', 75);

