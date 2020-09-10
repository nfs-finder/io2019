<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// include database and object files
include_once '../config/database.php';

// get database connection
$path_to_config = "../config/dbconfig.json";
$database = new Database($path_to_config);
$db = $database->getConnection();

// get calling user id and radius
$data = json_decode(file_get_contents("php://input"));

$query = "SELECT B.id as id, B.username as username, ST_X(B.location) as lat, ST_Y(B.location) as lng, B.active_car_name 
     FROM users A, users B WHERE A.id = " . $data->id . " AND NOT B.id = " . $data->id . "
     AND ST_DISTANCE_SPHERE(A.location, B.location) <= " . $data->radius . ";";

$stmt = $db->prepare($query);
$stmt->execute();

$num = $stmt->rowCount();
$response = array();

while ($num > 0) {
    $row = $stmt->fetch(PDO::FETCH_ASSOC);
    $id = $row["id"];
    $username = $row["username"];
    $lng = $row["lng"];
    $lat = $row["lat"];
    $car = $row["active_car_name"];

    array_push($response, ["id" => $id, "username" => $username, "lng" => $lng, "lat" => $lat, "car" => $car]);
    $num = $num - 1;
}

http_response_code(200);

echo json_encode($response);
