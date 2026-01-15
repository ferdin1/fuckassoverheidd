-- 1. Maak de database aan
CREATE DATABASE IF NOT EXISTS cjib_db;
USE cjib_db;

-- 2. Tabel voor gebruikers (Inlogsysteem)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('user', 'admin') DEFAULT 'user'
);

-- 3. Tabel voor functies (Functiezoeker)
CREATE TABLE IF NOT EXISTS functies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titel VARCHAR(255) NOT NULL,
    beschrijving TEXT,
    benodigd TEXT,
    opleiding VARCHAR(50),
    cursus VARCHAR(255),
    vervolg_functie VARCHAR(255)
);

-- 4. Tabel voor pins (Kaart)
CREATE TABLE IF NOT EXISTS pins (
    id INT AUTO_INCREMENT PRIMARY KEY,
    lat DOUBLE NOT NULL,
    lng DOUBLE NOT NULL,
    functionId INT,
    FOREIGN KEY (functionId) REFERENCES functies(id) ON DELETE CASCADE
);

-- 5. Basis data toevoegen
-- Standaard Admin (Wachtwoord is 'admin')
INSERT INTO users (username, password, role) VALUES ('admin', '$2y$10$O9lK.Y/R8YF1X0X.w0C1X.FfTj1hV1Qp7W6.2hFjH.W.e.W0.W0', 'admin') 
ON DUPLICATE KEY UPDATE username=username;

INSERT INTO functies (titel, beschrijving, benodigd, opleiding, cursus, vervolg_functie) VALUES 
('Medewerker Administratie', 'Verantwoordelijk voor het verwerken van gegevens.', 'MBO-diploma', 'MBO', 'Basisadministratie', 'Junior Administratie'),
('Juridisch Medewerker', 'Ondersteunt bij juridische processen.', 'HBO-diploma', 'HBO', 'Bestuursrecht Basis', 'Rechtshulp Medewerker');
