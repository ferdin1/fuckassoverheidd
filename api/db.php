<?php
// db.php - Database verbinding
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");
header("Content-Type: application/json");

$host = 'localhost';
$user = 'root';
$pass = '';
$dbname = 'cjib_db';

$conn = new mysqli($host, $user, $pass, $dbname);

if ($conn->connect_error) {
    die(json_encode(["error" => "Verbinding mislukt: " . $conn->connect_error]));
}
?>
