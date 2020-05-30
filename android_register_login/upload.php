<?php

if($_SERVER['REQUEST_METHOD']=='POST'){

    $image = $_POST["photo"];
    $nombre = $_POST['nombre'];

    require_once 'connect.php';
    
    $path = "uploads/$nombre.png";
    $actualPath = "http://192.168.56.1/android_register_login/uploads/$path";

    file_put_contents($path, base64_decode($image));

    $sql = "UPDATE users_table SET image = '$nombre' WHERE id = $nombre";

    if ( mysqli_query($conn, $sql)){
        echo "Se subio con exito";
    } else {
        echo "Ocurrio un error al subir la imagen";
    }

}
?>