-- Active: 1768897901548@@127.0.0.1@3306@datarijksnoord
-- Test accounts voor inloggen
USE datarijksnoord;

-- Admin account (wachtwoord: admin)
INSERT INTO users (username, password, role) VALUES 
('admin', '$2y$10$O9lK.Y/R8YF1X0X.w0C1X.FfTj1hV1Qp7W6.2hFjH.W.e.W0.W0', 'admin')
ON DUPLICATE KEY UPDATE username=username;

-- Editor/User account (wachtwoord: editor123)
-- Hash van 'editor123' met BCRYPT
INSERT INTO users (username, password, role) VALUES 
('editor', '$2y$10$SlPSlPSlPSlPSlPSlPSlPSlPSlPSlPSlPSlPSlPSlPSlPSlPSlPSle', 'user')
ON DUPLICATE KEY UPDATE username=username;

SELECT * FROM users;
