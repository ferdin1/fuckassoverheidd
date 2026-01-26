<?php
// db.php - Database connection using MySQLi
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "dataRijksnoord";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die(json_encode(["success" => false, "message" => "Connection failed: " . $conn->connect_error]));
}
?>
