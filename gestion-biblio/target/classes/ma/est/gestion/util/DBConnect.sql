USE gestion_bibliotheque;

DROP TABLE IF EXISTS emprunt;
DROP TABLE IF EXISTS livre;
DROP TABLE IF EXISTS adherents ;
DROP TABLE IF EXISTS categorie;
DROP TABLE IF EXISTS log;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS utilisateur;


CREATE TABLE IF NOT EXISTS adherents (
  numAdherent int NOT NULL AUTO_INCREMENT,
  nom varchar(100) NOT NULL,
  prenom varchar(100) NOT NULL,
  email varchar(100) NOT NULL,
  PRIMARY KEY (numAdherent)
) ENGINE=InnoDB;


INSERT INTO adherents (numAdherent, nom, prenom, email) VALUES
(1, 'Moussa', 'Youssouf', 'm.yf@email.com'),
(2, 'Jean', 'Bogou', 'j.bg@email.com'),
(3, 'Abdourahman', 'Mohamed', 'a.md@email.com'),
(4, 'Ammar', 'Ammar', 'am.am@email.com');



CREATE TABLE IF NOT EXISTS categorie (
  nom_categorie varchar(50) NOT NULL,
  description varchar(255) DEFAULT NULL,
  PRIMARY KEY (nom_categorie)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS emprunt (
  codeEmprunt varchar(150) NOT NULL,
  numAdherent int NOT NULL,
  dateEmprunt date NOT NULL,
  dateRetour date NOT NULL,
  statut enum('Actif','Retourne','En retard') NOT NULL,
  codeLivre varchar(50) NOT NULL,
  PRIMARY KEY (codeEmprunt),
  KEY numAdherent (numAdherent),
  KEY codeLivre (codeLivre)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS livre (
  id int NOT NULL AUTO_INCREMENT,
  code varchar(50) DEFAULT NULL,
  titre varchar(100) DEFAULT NULL,
  auteur varchar(100) DEFAULT NULL,
  nombreExemplaire int DEFAULT NULL,
  categorie varchar(50) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;


INSERT INTO livre (code, titre, auteur, nombreExemplaire, categorie) VALUES
('1é-Y', 'Misérables', 'V.Hugo', 5, 'Litérrature'),
('23-L', 'LEs Misérables', 'V.Hugo', 5, 'Roman'),
('34-Y', 'Think and become rich', 'N.Hill', 7, 'Vie Pratique');


CREATE TABLE IF NOT EXISTS log (
  id int NOT NULL AUTO_INCREMENT,
  date_action timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  action varchar(255) DEFAULT NULL,
  utilisateur_id int DEFAULT NULL,
  PRIMARY KEY (id),
  KEY utilisateur_id (utilisateur_id)
) ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS role (
  id_role int NOT NULL AUTO_INCREMENT,
  nom_role varchar(50) NOT NULL,
  PRIMARY KEY (id_role),
  UNIQUE KEY nom_role (nom_role)
) ENGINE=InnoDB;


INSERT INTO role (id_role, nom_role) VALUES
(1, 'ADMIN'),
(2, 'BIBLIOTHECAIRE');


CREATE TABLE IF NOT EXISTS utilisateur (
  id INT NOT NULL AUTO_INCREMENT,
  login VARCHAR(50) NOT NULL,
  password VARCHAR(100) NOT NULL,
  statut ENUM('ACTIF','INACTIF') DEFAULT 'ACTIF',
  role VARCHAR(50) DEFAULT 'USER',
  numAdherent INT NOT NULL,

  PRIMARY KEY (id),
  UNIQUE KEY login (login),
  KEY idx_numAdherent (numAdherent),

  CONSTRAINT fk_utilisateur_adherent
    FOREIGN KEY (numAdherent)
    REFERENCES adherents(numAdherent)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB;



INSERT INTO adherents (nom, prenom, email) VALUES
('Benali', 'Ahmed', 'ahmed.benali@email.com'),
('El Amrani', 'Sara', 'sara.elamrani@email.com'),
('Haddad', 'Youssef', 'youssef.haddad@email.com'),
('Zahraoui', 'Imane', 'imane.zahraoui@email.com'),
('Berrada', 'Omar', 'omar.berrada@email.com'),
('Kabbaj', 'Salma', 'salma.kabbaj@email.com'),
('Alaoui', 'Hamza', 'hamza.alaoui@email.com');

INSERT INTO livre (code, titre, auteur, nombreExemplaire, categorie) VALUES
('L001', 'Introduction à Java', 'James Gosling', 5, 'Informatique'),
('L002', 'Python pour les débutants', 'Guido van Rossum', 4, 'Informatique'),
('L003', 'Bases de données relationnelles', 'Edgar Codd', 3, 'Base de données'),
('L004', 'Algorithmique', 'Thomas Cormen', 6, 'Informatique'),
('L005', 'Réseaux informatiques', 'Andrew Tanenbaum', 2, 'Réseaux'),
('L006', 'Systèmes d’exploitation', 'Abraham Silberschatz', 3, 'Systèmes');