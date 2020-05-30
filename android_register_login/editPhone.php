<?php

if($_SERVER['REQUEST_METHOD'] == 'POST'){


    $phone = $_POST['phone'];
    $id = $_POST['id'];

    require_once 'connect.php';

    
    $sql = "UPDATE users_table SET phone = '$phone' WHERE id = $id";

    $query = "SELECT phone FROM users_table WHERE phone = '$phone' ";
    $q = mysqli_query($conn, $query);

    if( mysqli_num_rows($q) >= 1){

        $result["success"] ="2";
        $result["message"] ="exists";

        echo json_encode($result);
        mysqli_close($conn);

    } elseif( mysqli_query($conn, $sql) ){
        $result["success"] = "1";
        $result["message"] = "success";

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