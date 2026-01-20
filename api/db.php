<?php
// db.php - Database verbinding
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: GET, POST, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type");
header("Content-Type: application/json");


$host = '127.0.0.1';
$user = 'root';
$pass = '';
$dbname = 'datarijksnoord';
$port = 3306;

$conn = new mysqli($host, $user, $pass, $dbname, $port);

if ($conn->connect_error) {
    die(json_encode(["error" => "Verbinding mislukt: " . $conn->connect_error]));
}
?>
