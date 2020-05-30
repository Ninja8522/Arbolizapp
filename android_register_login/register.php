<?php

if($_SERVER['REQUEST_METHOD'] == 'POST'){

    $user_type = $_POST['user_type'];
    $email = $_POST['email'];
    $name = $_POST['name'];
    $username = $_POST['username'];
    $password = $_POST['password'];
    $phone = $_POST['phone'];
    $image = $_POST['image'];

    $password = password_hash($password, PASSWORD_DEFAULT);

    require_once 'connect.php';

    $sql = "INSERT INTO users_table(user_type, email, name, username, password, phone, image) VALUES ('$user_type', '$email', '$name', '$username', '$password', '$phone', '$image')";

    $query = "SELECT username FROM users_table WHERE username ='$username' ";
    $q = mysqli_query($conn, $query);
    $querycorreo = "SELECT email FROM users_table WHERE email = '$email' ";
    $r = mysqli_query($conn, $querycorreo);

    $last= "SELECT MAX(id) AS id FROM users_table";
    $l = mysqli_query($conn, $last);

    if( mysqli_num_rows($q) >= 1){

        $result["success"] ="2";
        $result["message"] ="exists";

        echo json_encode($result);
        mysqli_close($conn);

    } elseif ( mysqli_num_rows($r) >= 1) {
        $result["success"] ="3";
        $result["message"] ="exists";

        echo json_encode($result);
        mysqli_close($conn);

    } elseif( mysqli_query($conn, $sql) ){

        $result["success"] = "1";
        $result["message"] = "success";
        if (mysqli_num_rows($l) == 1) 
        {
            $row = mysqli_fetch_assoc($l);

           $result["id"]= ($row["id"] + 1);
        }        
        echo json_encode($result);
        mysqli_close($conn);

    } else {

        $result["success"] = "0";
        $result["message"] = "error";

        echo json_encode($result);
        mysqli_close($conn);
    }

}

?>