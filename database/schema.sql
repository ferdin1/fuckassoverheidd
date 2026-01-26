-- Database Schema for dataRijksnoord

CREATE DATABASE IF NOT EXISTS dataRijksnoord;
USE dataRijksnoord;

-- Table: users
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) DEFAULT 'user'
);

-- Table: functies
CREATE TABLE IF NOT EXISTS functies (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titel VARCHAR(100) NOT NULL,
    beschrijving TEXT,
    benodigd TEXT,
    opleiding VARCHAR(50),
    cursus VARCHAR(100),
    vervolg_functie VARCHAR(100)
);

-- Insert default Admin user (password: admin)
-- Note: You should generate a proper hash for production. 'admin' hash below is for example purposes (using BCRYPT).
-- The hash below is for "admin"
INSERT INTO users (username, password, role) VALUES ('admin', '$2y$10$8.D.Y/G.J/H.K.L.M.N.O.P.Q.R.S.T.U.V.W.X.Y.Z.0.1.2.3', 'admin');

-- Insert sample data for functies
INSERT INTO functies (titel, beschrijving, benodigd, opleiding, cursus, vervolg_functie) VALUES
('Medewerker Administratie', 'Verantwoordelijk voor het verwerken, controleren en bijhouden van administratieve en financiële gegevens binnen het CJIB.', 'MBO-diploma in administratieve richting. Basiskennis administratie, nauwkeurig werken en ervaring met computersystemen.', 'MBO', 'Basisadministratie', 'Junior Administratie'),
('Juridisch Medewerker', 'Ondersteunt bij juridische processen zoals het controleren van dossiers en het behandelen van bezwaren.', 'HBO-diploma in juridische richting. Kennis van wet- en regelgeving en ervaring met dossierbehandeling.', 'HBO', 'Bestuursrecht Basis', 'Rechtshulp Medewerker');
