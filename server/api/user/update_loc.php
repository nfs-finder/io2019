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

$query = "UPDATE users SET location = ST_POINTFROMTEXT('POINT(" . $data->lat . " " . $data->lng . ")', 4326) WHERE id = " . $data->id . ";";

$stmt = $db->prepare($query);
$stmt->execute();

http_response_code(200);
