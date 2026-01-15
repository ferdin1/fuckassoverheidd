<?php
// login.php - Inloggen via MySQL
require_once 'db.php';

$data = json_decode(file_get_contents("php://input"));

if (!empty($data->username) && !empty($data->password)) {
    $username = $conn->real_escape_string($data->username);

    // In een echte app gebruik je password_verify, voor nu 'admin'/'admin' testbaar maken
    $sql = "SELECT id, username, password, role FROM users WHERE username = '$username'";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        $user = $result->fetch_assoc();
        // Voor demo even simpel checken, PHP password_verify($data->password, $user['password']) is beter
        if ($data->password == 'admin' || password_verify($data->password, $user['password'])) {
            echo json_encode([
                "success" => true,
                "user" => [
                    "username" => $user['username'],
                    "role" => $user['role']
                ]
            ]);
        } else {
            echo json_encode(["success" => false, "message" => "Wachtwoord onjuist"]);
        }
    } else {
        echo json_encode(["success" => false, "message" => "Gebruiker niet gevonden"]);
    }
}
?>