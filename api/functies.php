<?php
// functies.php - CRUD voor functies
require_once 'db.php';

$method = $_SERVER['REQUEST_METHOD'];

if ($method == 'GET') {
    $sql = "SELECT * FROM functies";
    $result = $conn->query($sql);
    $functies = [];
    while ($row = $result->fetch_assoc()) {
        $functies[$row['id']] = $row;
    }
    echo json_encode($functies);
} elseif ($method == 'POST') {
    $data = json_decode(file_get_contents("php://input"));
    $stmt = $conn->prepare("INSERT INTO functies (titel, beschrijving, benodigd, opleiding, cursus, vervolg_functie) VALUES (?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("ssssss", $data->titel, $data->beschrijving, $data->benodigd, $data->opleiding, $data->cursus, $data->vervolg_functie);
    $stmt->execute();
    echo json_encode(["id" => $stmt->insert_id]);
}
?>