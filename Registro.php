<?php
    $con = mysqli_connect("mysql10.000webhost.com", "id1453347_microadmin", "microadmin17", "id1453347_microadmin");
    
    $Nombre = $_POST["nombre"];
    $Correo = $_POST["correo"];
    $Contrasena = $_POST["contrasena"];
    $statement = mysqli_prepare($con, "INSERT INTO user (Nombre, Correo, Contrasena) VALUES (?, ?, ?)");
    mysqli_stmt_bind_param($statement, "siss", $Nombre, $Correo, $Contrasena);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>
