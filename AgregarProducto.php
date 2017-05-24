<?php
    $con = mysqli_connect("localhost", "id1453347_microadmin", "microadmin17", "id1453347_microadmin");
    
    $codigo = $_POST["codigo"];
    $nombre = $_POST["nombre"];
    $preciounidad = $_POST["preciounidad"];
    $costomanufactura = $_POST["costomanufactura"];
    $imagen = $_FILES["imagen"];
    $cantidad = $_POST["cantidad"];
    $idproducto;
    $uploadPath = 'img/';
    //$serverIP = gethostbyname(gethostname());
    $uploadURL = 'http://microadmin.000webhostapp.com/'.$uploadPath;
    //agregar lo del url a imagen

    function ingresarProducto() {
    global $con, $codigo, $nombre, $preciounidad, $costomanufactura, $idproducto, $imagen, $uploadURL, $uploadPath;

    //$fileInfo = pathinfo($imagen);
    $extension = '.jpg';//$fileInfo['extension'];
    $fileURL = $uploadURL. getFileName() . '.' . $extension;
    $filePath = $uploadPath. getFileName() . '.' . $extension;


    try{
        move_uploaded_file('prueba.jpg', $uploadPath);
        $statement = mysqli_prepare($con, "INSERT INTO Producto (Codigo, Nombre, PrecioUnidad, CostoManufacturaUnidad, URLImagen) VALUES (?, ?, ?, ?, ?)");
        mysqli_stmt_bind_param($statement,'ssdds', $codigo, $nombre, $preciounidad, $costomanufactura, $fileURL);
        $result = mysqli_stmt_execute($statement);

        if ($result === TRUE) {
            $idproducto = mysqli_insert_id($con);
            ingresarInventario();
        }
        mysqli_stmt_close($statement);  

    }catch(Exception $e){
        $response['error'] = false;

     }
    }

    function getFileName(){
        $con = mysqli_connect("localhost", "id1453347_microadmin", "microadmin17", "id1453347_microadmin");
        $statement = "SELECT MAX(IDProducto) AS IDProducto FROM Producto";
        $result = mysqli_fetch_array(mysqli_query($con, $statement));
        mysqli_close($con);
        if($result['IDProducto'] == null){
            return 1;
        }else{
            return ++$result['IDProducto'];
        }

    }

    function ingresarInventario(){
        global $con, $idproducto, $codigo, $cantidad;
        $statement = mysqli_prepare($con, "INSERT INTO Inventario (IDProducto, Cantidad) VALUES (?, ?)");
        mysqli_stmt_bind_param($statement,'ii', $idproducto, $cantidad);
        mysqli_stmt_execute($statement);
        mysqli_stmt_close($statement);  
    }


    ingresarProducto();
    $response = array();
    $response["success"] = true;  
?>
