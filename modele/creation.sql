CREATE DATABASE IF NOT EXISTS SAE_RESEAUX;
USE SAE_RESEAUX;

DROP TABLE IF EXISTS LIKES;
DROP TABLE IF EXISTS DISLIKES;

DROP TABLE IF EXISTS MESSAGES;
DROP TABLE IF EXISTS AMIS;
DROP TABLE IF EXISTS UTILISATEUR;

CREATE TABLE UTILISATEUR (
    id_U INT NOT NULL,
    pseudo VARCHAR(100),
    email VARCHAR(100),
    mdp VARCHAR(100),
    PRIMARY KEY (id_U),
    UNIQUE (pseudo),
    UNIQUE (email)
);

CREATE TABLE AMIS (
    suiveur INT NOT NULL,
    suivi INT NOT NULL,
    FOREIGN KEY (suiveur) REFERENCES UTILISATEUR(id_U),
    FOREIGN KEY (suivi) REFERENCES UTILISATEUR(id_U),
    PRIMARY KEY (suiveur, suivi)
);

CREATE TABLE MESSAGES (
    id_M INT NOT NULL,
    id_U INT NOT NULL,
    contenu VARCHAR(100),
    date DATETIME,
    FOREIGN KEY (id_U) REFERENCES UTILISATEUR(id_U),
    PRIMARY KEY (id_M)
);

CREATE TABLE LIKES (
    id_M INT NOT NULL,
    id_U INT NOT NULL,
    FOREIGN KEY (id_M) REFERENCES MESSAGES(id_M),
    FOREIGN KEY (id_U) REFERENCES UTILISATEUR(id_U),
    PRIMARY KEY (id_M, id_U)
);

CREATE TABLE DISLIKES(
    id_M INT NOT NULL,
    id_U INT NOT NULL,
    FOREIGN KEY (id_M) REFERENCES MESSAGES(id_M),
    FOREIGN KEY (id_U) REFERENCES UTILISATEUR(id_U),
    PRIMARY KEY (id_M, id_U)
);


-- Insertion

INSERT INTO UTILISATEUR VALUES (1, 'ahmet', 'ahmet@gmail.com', 'ahmet');
INSERT INTO UTILISATEUR VALUES (2, 'noa', 'noa@gmail.com', 'noa');
INSERT INTO UTILISATEUR VALUES (3, 'test','test','test');
INSERT INTO AMIS VALUES (1, 2);
INSERT INTO MESSAGES VALUES (1, 1, 'je teste', '2024-01-10 09:30:00');
