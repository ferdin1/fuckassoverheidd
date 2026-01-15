<?php
// register.php - Nieuwe gebruiker registreren
require_once 'db.php';

$data = json_decode(file_get_contents("php://input"));

if (!empty($data->username) && !empty($data->password)) {
    $username = $conn->real_escape_string($data->username);
    // Beveilig het wachtwoord
    $password = password_hash($data->password, PASSWORD_BCRYPT);
    $role = 'user'; // Standaard rol

    // Check of gebruiker al bestaat
    $check = $conn->query("SELECT id FROM users WHERE username = '$username'");
    if ($check->num_rows > 0) {
        echo json_encode(["success" => false, "message" => "Gebruikersnaam is al bezet"]);
        exit;
    }

    $sql = "INSERT INTO users (username, password, role) VALUES ('$username', '$password', '$role')";

    if ($conn->query($sql)) {
        echo json_encode(["success" => true, "message" => "Account aangemaakt!"]);
    } else {
        echo json_encode(["success" => false, "message" => "Fout bij aanmaken account: " . $conn->error]);
    }
} else {
    echo json_encode(["success" => false, "message" => "Vul alle velden in"]);
}
?>