<?php
// required headers
header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: POST");
header("Access-Control-Max-Age: 3600");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// include database and object files
include_once '../config/database.php';
include_once '../objects/user.php';

// get database connection
$path_to_config = "../config/dbconfig.json";
$database = new Database($path_to_config);
$db = $database->getConnection();

// prepare product object
$user = new User($db);

// get id of product to be edited
$data = json_decode(file_get_contents("php://input"));

try {
    $user->login($data->email, $data->password);

    $response = array();
    $response["id"] = $user->id;
    $response["username"] = $user->username;

    http_response_code(200);

    echo json_encode($response);
} catch (WrongCredentialsExceptions $ex) {
    http_response_code(401);

    echo json_encode(array("message" => $ex->getMessage()));
}

