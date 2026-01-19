<?php
// register.php - Veilige registratie
header('Content-Type: application/json');
require_once 'db.php';

try {
    $data = json_decode(file_get_contents("php://input"), true);

    // Validatie
    if (empty($data['username']) || empty($data['password'])) {
        http_response_code(400);
        echo json_encode(["success" => false, "message" => "Vul alle velden in"]);
        exit;
    }

    $username = trim($data['username']);
    $password = $data['password'];
    $role = 'user'; // Standaard rol

    // Validatie username
    if (strlen($username) < 3 || strlen($username) > 50) {
        http_response_code(400);
        echo json_encode(["success" => false, "message" => "Gebruikersnaam moet 3-50 karakters zijn"]);
        exit;
    }

    // Validatie password
    if (strlen($password) < 6) {
        http_response_code(400);
        echo json_encode(["success" => false, "message" => "Wachtwoord moet minstens 6 karakters zijn"]);
        exit;
    }

    // Check of gebruiker al bestaat met prepared statement
    $stmt = $conn->prepare("SELECT id FROM users WHERE username = ?");
    $stmt->bind_param("s", $username);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        http_response_code(409);
        echo json_encode(["success" => false, "message" => "Gebruikersnaam is al bezet"]);
        $stmt->close();
        exit;
    }
    $stmt->close();

    // Hash het wachtwoord
    $hashedPassword = password_hash($password, PASSWORD_BCRYPT);

    // Insert nieuwe gebruiker met prepared statement
    $stmt = $conn->prepare("INSERT INTO users (username, password, role) VALUES (?, ?, ?)");
    if (!$stmt) {
        throw new Exception("Database error");
    }

    $stmt->bind_param("sss", $username, $hashedPassword, $role);

    if ($stmt->execute()) {
        echo json_encode(["success" => true, "message" => "Account succesvol aangemaakt! Je kunt nu inloggen."]);
    } else {
        http_response_code(500);
        echo json_encode(["success" => false, "message" => "Fout bij aanmaken account"]);
    }

    $stmt->close();
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Server error"]);
}

$conn->close();
?>