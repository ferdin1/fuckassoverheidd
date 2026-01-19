<?php
// login.php - Veilige inloggen via MySQL
header('Content-Type: application/json');
require_once 'db.php';

try {
    $data = json_decode(file_get_contents("php://input"), true);

    // Validatie
    if (empty($data['username']) || empty($data['password'])) {
        http_response_code(400);
        echo json_encode(["success" => false, "message" => "Gebruikersnaam en wachtwoord zijn verplicht"]);
        exit;
    }

    $username = trim($data['username']);
    $password = $data['password'];

    // Prepared statement voor veiligheid tegen SQL injection
    $stmt = $conn->prepare("SELECT id, username, password, role FROM users WHERE username = ?");
    if (!$stmt) {
        throw new Exception("Database error");
    }

    $stmt->bind_param("s", $username);
    $stmt->execute();
    $result = $stmt->get_result();

    if ($result->num_rows > 0) {
        $user = $result->fetch_assoc();

        // Veilige wachtwoord controle
        if (password_verify($password, $user['password'])) {
            // Login succesvol
            echo json_encode([
                "success" => true,
                "user" => [
                    "username" => $user['username'],
                    "role" => $user['role']
                ]
            ]);
        } else {
            http_response_code(401);
            echo json_encode(["success" => false, "message" => "Wachtwoord onjuist"]);
        }
    } else {
        http_response_code(401);
        echo json_encode(["success" => false, "message" => "Gebruiker niet gevonden"]);
    }

    $stmt->close();
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode(["success" => false, "message" => "Server error"]);
}

$conn->close();
?>