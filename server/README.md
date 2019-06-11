# REST API Server
The requests from NFS Finder client are processed by simple REST API Server written in PHP.
## Structure
File structure of our servers looks like this:
```
- api - root directory for server
  - config - database class and credentials file
  - objects - utility classes such as user class or, in future, car class
  - user - requests related to user operations
  - [cars, races etc] - requests related to other operations (not implemented)
```
## Setting up the credentials for server to connect to the database
Database class constructor takes a path to JSON file from which it will extract credentials to connect to database. The file should be a dictionary with four keys: `host`, `db_name`, `username`, `password`. Each key should have a string value.

## Format of requests
Each request is a JSON object containing data needed to perform requested operation. 

Example (successful login):
```
Client sends: {"email": correct_email, "password": correct_pswd}
Client receives: response code 200 OK and JSON {"id": user_id, "username": username}
```
Example (failed login):
```
Client sends: {"email": email, "password": wrong_pswd}
Client receives: response code 401 Unauthorized and message "Wrong credentials!"
```

## Database tables
For further developing we assume that database we connect to has three tables - `users`, `users_cars`, `races_history`. 
### users table:
```
+-----------------+---------------------+------+-----+---------+----------------+
| Field           | Type                | Null | Key | Default | Extra          |
+-----------------+---------------------+------+-----+---------+----------------+
| id              | bigint(20) unsigned | NO   | PRI | NULL    | auto_increment |
| email           | varchar(254)        | NO   |     | NULL    |                |
| username        | varchar(50)         | NO   |     | NULL    |                |
| password        | varchar(50)         | NO   |     | NULL    |                |
| location        | point               | YES  |     | NULL    |                |
| active_car_name | varchar(40)         | NO   |     | NULL    |                |
+-----------------+---------------------+------+-----+---------+----------------+
```
### users_cars table:
```
+------------+---------------------+------+-----+---------+----------------+
| Field      | Type                | Null | Key | Default | Extra          |
+------------+---------------------+------+-----+---------+----------------+
| id         | bigint(20) unsigned | NO   | PRI | NULL    | auto_increment |
| user_id    | bigint(20) unsigned | NO   | MUL | NULL    |                |
| model_name | varchar(40)         | NO   |     | NULL    |                |
+------------+---------------------+------+-----+---------+----------------+
```
### races_history table:
```
+------------+---------------------+------+-----+---------+----------------+
| Field      | Type                | Null | Key | Default | Extra          |
+------------+---------------------+------+-----+---------+----------------+
| id         | bigint(20) unsigned | NO   | PRI | NULL    | auto_increment |
| user1_id   | bigint(20) unsigned | YES  | MUL | NULL    |                |
| user2_id   | bigint(20) unsigned | YES  |     | NULL    |                |
| user1_time | time(2)             | YES  |     | NULL    |                |
| user2_time | time(2)             | YES  |     | NULL    |                |
| start_loc  | point               | NO   |     | NULL    |                |
| end_loc    | point               | NO   |     | NULL    |                |
+------------+---------------------+------+-----+---------+----------------+
```
#### Point data type
The type `point` used in definitions is so called a spatial data type. We create it using SQL function `ST_POINTFROMTEXT(point, srid)` where the point is a string of form `Point (num num)` (no comma between numbers!) and srid defines the shape of a surface. We use `srid = 4326` to denote a point on sphere. To extract longitude and latitude we use SQL functions `ST_X(point)` and `ST_Y(point)`. To calculate distance between two points we use SQL function `ST_DISTANCE_SPHERE(p1, p2)`.
