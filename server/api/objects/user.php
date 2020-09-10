<?php
class WrongCredentialsExceptions extends Exception {}

class User {
    private $conn;
    private $table_name = "users";

    public $id;
    public $email;
    public $username;
    public $password;
    public $lng;
    public $lat;

    public function __construct($db) {
        $this->conn = $db;
    }

    function login($email, $password) {
        $query = "SELECT id, email, username, password FROM ".$this->table_name." WHERE email='".$email."' AND password='".$password."';";
        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        
        $num = $stmt->rowCount();

        if ($num > 0) {
            $row = $stmt->fetch(PDO::FETCH_ASSOC);
            $this->id = $row["id"];
            $this->username = $row["username"];
        } else {
            throw new WrongCredentialsExceptions($query);
        }
    }
}