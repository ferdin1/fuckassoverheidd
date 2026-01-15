<?php
// pins.php - CRUD voor pins
require_once 'db.php';

$method = $_SERVER['REQUEST_METHOD'];

if ($method == 'GET') {
    $sql = "SELECT * FROM pins";
    $result = $conn->query($sql);
    $pins = [];
    while ($row = $result->fetch_assoc()) {
        $pins[] = $row;
    }
    echo json_encode($pins);
} elseif ($method == 'POST') {
    $data = json_decode(file_get_contents("php://input"));
    $stmt = $conn->prepare("INSERT INTO pins (lat, lng, functionId) VALUES (?, ?, ?)");
    $stmt->bind_param("ddi", $data->lat, $data->lng, $data->functionId);
    $stmt->execute();
    echo json_encode(["id" => $stmt->insert_id]);
} elseif ($method == 'DELETE') {
    $id = $_GET['id'];
    $conn->query("DELETE FROM pins WHERE id = $id");
    echo json_encode(["success" => true]);
}
?>