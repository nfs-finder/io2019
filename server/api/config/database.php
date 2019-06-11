<?php
class Database{
    private $host;
    private $db_name;
    private $username;
    private $password;
    public $conn;

    function __construct($path_to_config) {
        $data = json_decode(file_get_contents($path_to_config));
        $this->host = $data->host;
        $this->db_name = $data->db_name;
        $this->username = $data->username;
        $this->password = $data->password;
    }
 
    // get the database connection
    public function getConnection(){
        $this->conn = null;
 
        try {
            $this->conn = new PDO("mysql:host=" . $this->host . ";dbname=" . $this->db_name, $this->username, $this->password);
            $this->conn->exec("set names utf8");
        } catch(PDOException $exception) {
            echo "Connection error: " . $exception->getMessage();
        }
 
        return $this->conn;
    }
}
