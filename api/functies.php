<?php
// functies.php - CRUD voor functies
header('Content-Type: application/json');
require_once 'db.php';

$method = $_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        $sql = "SELECT * FROM functies";
        $result = $conn->query($sql);
        $functies = [];
        if ($result) {
            while ($row = $result->fetch_assoc()) {
                $functies[$row['id']] = $row;
            }
        }
        echo json_encode($functies);
        break;

    case 'POST':
        $data = json_decode(file_get_contents("php://input"));
        if (!empty($data->titel)) {
            $stmt = $conn->prepare("INSERT INTO functies (titel, beschrijving, benodigd, opleiding, cursus, vervolg_functie) VALUES (?, ?, ?, ?, ?, ?)");
            $stmt->bind_param("ssssss", $data->titel, $data->beschrijving, $data->benodigd, $data->opleiding, $data->cursus, $data->vervolg_functie);
            if ($stmt->execute()) {
                echo json_encode(["success" => true, "id" => $stmt->insert_id, "message" => "Functie toegevoegd"]);
            } else {
                http_response_code(500);
                echo json_encode(["success" => false, "message" => "Fout bij toevoegen: " . $stmt->error]);
            }
        } else {
            http_response_code(400);
            echo json_encode(["success" => false, "message" => "Titel is verplicht"]);
        }
        break;

    case 'PUT':
        $data = json_decode(file_get_contents("php://input"));
        if (!empty($data->id) && !empty($data->titel)) {
            $stmt = $conn->prepare("UPDATE functies SET titel=?, beschrijving=?, benodigd=?, opleiding=?, cursus=?, vervolg_functie=? WHERE id=?");
            $stmt->bind_param("ssssssi", $data->titel, $data->beschrijving, $data->benodigd, $data->opleiding, $data->cursus, $data->vervolg_functie, $data->id);
            if ($stmt->execute()) {
                echo json_encode(["success" => true, "message" => "Functie bijgewerkt"]);
            } else {
                http_response_code(500);
                echo json_encode(["success" => false, "message" => "Fout bij bijwerken: " . $stmt->error]);
            }
        } else {
            http_response_code(400);
            echo json_encode(["success" => false, "message" => "ID en Titel zijn verplicht"]);
        }
        break;

    case 'DELETE':
        $data = json_decode(file_get_contents("php://input"));
        // Support deleting by ID passed in URL query param or body
        $id = null;
        if (isset($_GET['id'])) {
            $id = $_GET['id'];
        } elseif (!empty($data->id)) {
            $id = $data->id;
        }

        if ($id) {
            $stmt = $conn->prepare("DELETE FROM functies WHERE id=?");
            $stmt->bind_param("i", $id);
            if ($stmt->execute()) {
                echo json_encode(["success" => true, "message" => "Functie verwijderd"]);
            } else {
                http_response_code(500);
                echo json_encode(["success" => false, "message" => "Fout bij verwijderen: " . $stmt->error]);
            }
        } else {
            http_response_code(400);
            echo json_encode(["success" => false, "message" => "ID is verplicht"]);
        }
        break;

    default:
        http_response_code(405);
        echo json_encode(["success" => false, "message" => "Method not allowed"]);
        break;
}

$conn->close();
?>