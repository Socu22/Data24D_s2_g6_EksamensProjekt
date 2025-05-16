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
    vognNummer VARCHAR(7) PRIMARY KEY NOT NULL,
    stelNummer VARCHAR(17)   NOT NULL,
    bilType_Id INT           NOT NULL,
    lager_Id   INT           NOT NULL,
    status     VARCHAR(20)   NOT NULL,
    FOREIGN KEY (bilType_Id) REFERENCES bilType(bilType_Id),
    FOREIGN KEY (lager_Id)   REFERENCES lager  (lager_Id),
    UNIQUE      (stelNummer)
);

CREATE TABLE kunde(
    kunde_Id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    navn     VARCHAR(50) NOT NULL
);

CREATE TABLE lejeAftaler(
    aftale_Id  INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    kunde_Id   INT           NOT NULL,
    vognNummer VARCHAR(7)    NOT NULL,
    startDato  DATE          NOT NULL,
    slutDato   DATE          NOT NULL,
    detaljer   VARCHAR(255)  NOT NULL,
    FOREIGN KEY (kunde_Id)   REFERENCES kunde(kunde_Id),
    FOREIGN KEY (vognNummer) REFERENCES bil  (vognNummer)
);

CREATE TABLE notationer(
    notationer_id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
    aftale_Id     INT,
    vognNummer    VARCHAR(7)   NOT NULL,
    beskrivelse   VARCHAR(255) NOT NULL,
    pris          DOUBLE       NOT NULL,
    FOREIGN KEY (aftale_Id)  REFERENCES lejeAftaler(aftale_Id),
    FOREIGN KEY (vognNummer) REFERENCES bil        (vognNummer)
);


--
-- Medarbejdere
INSERT INTO medArbejdere (navn, adgangskode, stilling) VALUES
    ('demo' ,'demo' ,'Demo'    ),
    ('demo1','demo1','Dataregistrering'    ),
    ('demo2','demo2','Skade_&_Udbedring'   ),
    ('demo3','demo3','Forretningsudviklere');

-- Biltyper (3 originale + 4 nye)
INSERT INTO bilType (mærke, model, udstyrsniveau, stålPris, afgift, udledning_Co2) VALUES
    ('Toyota',     'Corolla', 'Basic',       200000, 40000, 95 ),
    ('Honda',      'Civic',   'Deluxe',      220000, 42000, 90 ),
    ('Ford',       'Focus',   'Premium',     210000, 41000, 92 ),
    ('BMW',        '320i',    'Sport',       350000, 70000, 120),
    ('Audi',       'A3',      'Business',    330000, 66000, 110),
    ('Volkswagen', 'Golf',    'Comfortline', 280000, 56000, 100),
    ('Hyundai',    'Ioniq',   'Electric',    300000, 0,     0  );

-- Lagerlokationer (6 stk.)
INSERT INTO lager (navn, adresse) VALUES
    ('Lager A', '123 Main St'   ),
    ('Lager B', '456 Side St'   ),
    ('Lager C', '789 Back St'   ),
    ('Lager D', '135 North Ave' ),
    ('Lager E', '246 South Blvd'),
    ('Lager F', '369 East Lane' );

-- Biler (både eksisterende og nye)
INSERT INTO bil (vognNummer, stelNummer, bilType_Id, lager_Id, status) VALUES
    ('AB12345', 'WAUZZZ8V4DN674512', 1, 1, 'Tilgængelig'    ),
    ('CD67890', '1HGBH41JXMN109169', 2, 2, 'Udlejet'        ),
    ('EF56789', '1FMCU0C72CK451093', 3, 3, 'Vedligeholdelse'),
    ('XY92831', '1HGCM82633A004321', 4, 4, 'Tilgængelig'    ),
    ('QT63829', '2FTRX18W1XCA98765', 5, 4, 'Slettet'        ),
    ('BH10475', 'WDBUF70J94A345678', 6, 5, 'Udlejet'        ),
    ('KU73190', '3N1AB7AP7HY256789', 7, 5, 'Vedligeholdelse'),
    ('ZA87364', 'JN1CV6EK9BM234567', 4, 6, 'Slettet'        ),
    ('MW28374', '1N4AL3AP9FC198765', 5, 6, 'Udlejet'        ),
    ('LD98430', 'WA1DGAFE6FD017654', 6, 1, 'Tilgængelig'    ),
    ('RP19383', 'SALWR2VF2FA623890', 7, 2, 'Tilgængelig'    ),
    ('TE48923', 'KM8J33A40GU265431', 4, 3, 'Slettet'        ),
    ('CG57492', '5UXWX9C5XG0D34567', 5, 4, 'Tilgængelig'    );

-- Kunder
INSERT INTO kunde (navn) VALUES
    ('John Doe'),
    ('Jane Smith');

-- Lejeaftaler (flere rækker, danske detaljer)
INSERT INTO lejeAftaler (kunde_Id, vognNummer, startDato, slutDato, detaljer) VALUES
    (1, 'CD67890', '2025-01-01', '2025-01-15', 'Lejeaftale for John Doe - januar'       ),
    (2, 'AB12345', '2025-02-01', '2025-02-28', 'Lejeaftale for Jane Smith - februar'    ),
    (1, 'EF56789', '2025-03-01', '2025-03-10', 'Lejeaftale for John Doe - marts'        ),
    (2, 'BH10475', '2025-04-05', '2025-04-20', 'Lejeaftale for Jane Smith - forårstur'  ),
    (1, 'LD98430', '2025-05-01', '2025-05-15', 'Lejeaftale for John Doe - sommer'       ),
    (2, 'RP19383', '2025-06-10', '2025-06-25', 'Lejeaftale for Jane Smith - sommerferie'),
    (1, 'CG57492', '2025-07-01', '2025-07-10', 'Korttidsleje for John Doe'              ),
    (2, 'XY92831', '2025-08-01', '2025-08-15', 'Lejeaftale for Jane Smith - august'     );

-- Notationer uden aftale (aftale_Id IS NULL)
INSERT INTO notationer (aftale_Id, vognNummer, beskrivelse, pris) VALUES
    (NULL, 'XY92831', 'Ridse på venstre dør',      1500),
    (NULL, 'BH10475', 'Service udført',            0   ),
    (NULL, 'KU73190', 'Skift af dæk',              3500),
    (NULL, 'TE48923', 'Lakskade udbedret',         2200),
    (NULL, 'LD98430', 'Bil rengjort og klargjort', 0   );

-- Notationer med aftale (aftale_Id IS NOT NULL)
INSERT INTO notationer (aftale_Id, vognNummer, beskrivelse, pris) VALUES
    (1, 'CD67890', 'Extra insurance',      500 ),
    (1, 'CD67890', 'Additional driver',    300 ),
    (2, 'AB12345', 'GPS device',           100 ),
    (3, 'EF56789', 'Child seat',           75  ),
    (1, 'CD67890', 'Ridse ved aflevering', 1200),
    (1, 'CD67890', 'Manglende brændstof',  400 ),
    (2, 'AB12345', 'Ekstra rengøring',     300 ),
    (3, 'EF56789', 'Sen aflevering',       500 ),
    (3, 'EF56789', 'Skade på fælg',        800 );
