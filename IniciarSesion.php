<?php
    $con = mysqli_connect("mysql10.000webhost.com", "id1453347_microadmin", "microadmin17", "id1453347_microadmin");
    
    $Correo = $_POST["Correo"];
    $Contrasena = $_POST["Contrasena"];
    
    $statement = mysqli_prepare($con, "SELECT * FROM Usuario WHERE Correo = ? AND Contrasena = ?");
    mysqli_stmt_bind_param($statement, "ss", $Correo, $Contrasena);
    mysqli_stmt_execute($statement);
    
    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $IDUsuario, $Nombre, $Correo, $Contrasena);
    
    $response = array();
    $response["success"] = false;  
    
    while(mysqli_stmt_fetch($statement)){
        $response["success"] = true;  
        $response["Nombre"] = $Nombre;
        $response["Correo"] = $Correo;
        $response["Contrasena"] = $Contrasena;
    }
    
    echo json_encode($response);
?>
