<?php
    $con = mysqli_connect("localhost", "id1453347_microadmin", "microadmin17", "id1453347_microadmin");
    
    $nombre = $_POST["nombre"];
    $correo = $_POST["correo"];
    $contrasena = $_POST["contrasena"];

    function registrarUsuario() {
    global $con, $nombre, $correo, $contrasena;
    $statement = mysqli_prepare($con, "INSERT INTO Usuario (Nombre, Correo, Contrasena) VALUES (?, ?, ?)");
    mysqli_stmt_bind_param($statement,'sss', $nombre, $correo, $contrasena);
    mysqli_stmt_execute($statement);
    mysqli_stmt_close($statement);  
    }

    function correoNoExiste(){
        global $con, $correo;
        $statement = mysqli_prepare($con, "SELECT * FROM Usuario WHERE Correo = ?"); 
        mysqli_stmt_bind_param($statement, "s", $correo);
        mysqli_stmt_execute($statement);
        mysqli_stmt_store_result($statement);
        $count = mysqli_stmt_num_rows($statement);
        mysqli_stmt_close($statement); 
        if ($count < 1){
            return true; 
        }else {
            return false; 
        }
    }

    $response = array();
    $response["success"] = false; 
    if(correoNoExiste()){
        registrarUsuario();
        $response["success"] = true;  
    } 
    
    echo json_encode($response);
?>
