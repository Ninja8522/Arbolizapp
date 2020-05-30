<?php

if($_SERVER['REQUEST_METHOD']=='POST'){

	$email = $_POST["email"];
	$username = $_POST["username"];
    $password =	$_POST["password"];

    require_once 'connect.php';

    $sql = "SELECT * FROM users_table WHERE email='$email' OR username ='$username' ";

    $response = mysqli_query($conn, $sql);

    $result = array();
    $result["login"] = array();

    if(mysqli_num_rows($response) == 1){

        $row = mysqli_fetch_assoc($response);

        if( password_verify ($password, $row['password'])) {

            $index['id'] = $row['id'];
            $index['name'] = $row['name'];
            $index['email'] = $row['email'];
            $index['user_type'] = $row['user_type'];
            $index['username'] = $row['username'];
            $index['phone'] = $row['phone'];
            $index['image'] = $row['image'];


            array_push($result["login"], $index);

            $result["success"] = "1";
            $result["message"] = "success";
            echo json_encode($result);
            header('Content-Type: application/json');
            mysqli_close($conn);

        }else {

            $result["success"] = "0";
            $result["message"] = "error";
            echo json_encode($result);
            header('Content-Type: application/json');
            mysqli_close($conn);

        }

    }

}



?>