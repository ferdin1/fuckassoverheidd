<?php
/**
 * ============================================
 * DATABASE CONFIGURATIE BESTAND
 * ============================================
 * 
 * Dit bestand bevat alle database instellingen.
 * Hier maak je verbinding met MySQL (via XAMPP/WAMP).
 * 
 * BELANGRIJK: Pas deze instellingen aan naar jouw lokale setup!
 */

// ============================================
// STAP 1: DATABASE INSTELLINGEN
// ============================================

// Hostname - meestal "localhost" als je lokaal werkt
define('DB_HOST', 'localhost');

// Database naam - maak deze aan in phpMyAdmin
define('DB_NAME', 'rijksoverheid_loopbaan');

// Gebruikersnaam - standaard "root" bij XAMPP
define('DB_USER', 'root');

// Wachtwoord - standaard leeg bij XAMPP, anders jouw wachtwoord
define('DB_PASS', '');

// ============================================
// STAP 2: VERBINDING MAKEN MET DATABASE
// ============================================

/**
 * Functie om verbinding te maken met de database
 * 
 * @return PDO Database verbinding object
 * @throws PDOException Als verbinding mislukt
 */
function getDatabaseConnection() {
    try {
        // DSN = Data Source Name (bevat alle connectie info)
        $dsn = "mysql:host=" . DB_HOST . ";dbname=" . DB_NAME . ";charset=utf8mb4";
        
        // PDO opties voor betere error handling en veiligheid
        $options = [
            // Gooi exceptions bij fouten (makkelijker debuggen)
            PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,
            
            // Haal data op als associatieve arrays (makkelijker te gebruiken)
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,
            
            // Gebruik echte prepared statements (veiliger tegen SQL injection)
            PDO::ATTR_EMULATE_PREPARES => false,
        ];
        
        // Maak de PDO verbinding
        $pdo = new PDO($dsn, DB_USER, DB_PASS, $options);
        
        return $pdo;
        
    } catch (PDOException $e) {
        // Als verbinding mislukt, toon foutmelding
        die("Database verbinding mislukt: " . $e->getMessage());
    }
}

// ============================================
// STAP 3: HELPER FUNCTIES
// ============================================

/**
 * Stuur JSON response terug naar de frontend
 * 
 * @param mixed $data De data om terug te sturen
 * @param int $statusCode HTTP status code (200 = OK, 400 = Error, etc.)
 */
function sendJsonResponse($data, $statusCode = 200) {
    // Zet de juiste headers voor JSON
    header('Content-Type: application/json');
    header('HTTP/1.1 ' . $statusCode);
    
    // CORS headers (zodat frontend en backend kunnen communiceren)
    header('Access-Control-Allow-Origin: *');
    header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
    header('Access-Control-Allow-Headers: Content-Type');
    
    // Converteer data naar JSON en stuur terug
    echo json_encode($data, JSON_UNESCAPED_UNICODE);
    exit;
}

/**
 * Haal JSON data op uit de request body
 * 
 * @return array De gedecodeerde JSON data
 */
function getJsonInput() {
    // Lees de raw input van de request
    $json = file_get_contents('php://input');
    
    // Decodeer JSON naar PHP array
    $data = json_decode($json, true);
    
    return $data ?? [];
}

/**
 * Valideer of vereiste velden aanwezig zijn
 * 
 * @param array $data De data om te checken
 * @param array $requiredFields Lijst van vereiste velden
 * @return array|null Null als alles OK, anders array met foutmelding
 */
function validateRequiredFields($data, $requiredFields) {
    $missingFields = [];
    
    foreach ($requiredFields as $field) {
        if (!isset($data[$field]) || empty($data[$field])) {
            $missingFields[] = $field;
        }
    }
    
    if (!empty($missingFields)) {
        return [
            'success' => false,
            'message' => 'Ontbrekende velden: ' . implode(', ', $missingFields)
        ];
    }
    
    return null;
}

// ============================================
// STAP 4: DATABASE SETUP FUNCTIE
// ============================================

/**
 * Maak alle benodigde tabellen aan
 * Draai deze functie 1x om je database op te zetten
 */
function setupDatabase() {
    $pdo = getDatabaseConnection();
    
    // TABEL 1: Users (voor login/registratie)
    $pdo->exec("
        CREATE TABLE IF NOT EXISTS users (
            id INT AUTO_INCREMENT PRIMARY KEY,
            username VARCHAR(50) UNIQUE NOT NULL,
            password VARCHAR(255) NOT NULL,
            role ENUM('admin', 'user') DEFAULT 'user',
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
    ");
    
    // TABEL 2: Functies (alle functies/banen)
    $pdo->exec("
        CREATE TABLE IF NOT EXISTS functies (
            id INT AUTO_INCREMENT PRIMARY KEY,
            titel VARCHAR(100) NOT NULL,
            beschrijving TEXT,
            benodigd TEXT,
            opleiding VARCHAR(50),
            cursus VARCHAR(100),
            vervolg_functie VARCHAR(100),
            locatie VARCHAR(100),
            lat DECIMAL(10, 8),
            lng DECIMAL(11, 8),
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
    ");
    
    // TABEL 3: Pins (kaart locaties)
    $pdo->exec("
        CREATE TABLE IF NOT EXISTS pins (
            id INT AUTO_INCREMENT PRIMARY KEY,
            lat DECIMAL(10, 8) NOT NULL,
            lng DECIMAL(11, 8) NOT NULL,
            title VARCHAR(100) NOT NULL,
            description TEXT,
            link VARCHAR(255),
            function_id INT,
            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
            FOREIGN KEY (function_id) REFERENCES functies(id) ON DELETE SET NULL
        ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
    ");
    
    echo "Database tabellen succesvol aangemaakt!";
}

// ============================================
// UNCOMMENT ONDERSTAANDE REGEL OM DATABASE OP TE ZETTEN
// Draai dit bestand 1x in je browser, dan comment het weer uit
// ============================================
// setupDatabase();

?>
