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
                            FOREIGN KEY (kunde_Id) REFERENCES kunde(kunde_Id),
                            FOREIGN KEY (vognNummer) REFERENCES bil(vognNummer)
);

CREATE TABLE notationer(
                           notationer_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
                           aftale_Id INT NOT NULL,
                           beskrivelse VARCHAR(255) NOT NULL,
                           pris DOUBLE NOT NULL,
                           FOREIGN KEY (aftale_Id) REFERENCES lejeAftaler(aftale_Id)
);
