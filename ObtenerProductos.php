<?php
$connection = mysqli_connect("localhost", "id1453347_microadmin", "microadmin17", "id1453347_microadmin");
  $sql="SELECT P.IDProducto as idProducto, P.Codigo as codigo, P.Nombre as nombre, P.PrecioUnidad as precioUnidad, P.CostoManufacturaUnidad as costoManufactura, P.URLImagen as urlImagen, I.Cantidad as cantidad FROM Producto as P INNER JOIN Inventario as I ON P.IDProducto = I.IDProducto ";
   $result= mysqli_query($connection, $sql);
   //$result = $connection->query($sql);
     if ($result->num_rows != null && $result->num_rows > 0) {
       $arr = []; 
       $i = 0;
       while ($row = mysqli_fetch_array($result))
       {
          $arr2 = [];
          $arr2["idProducto"] = $row["idProducto"];
          $arr2["codigo"] = $row["codigo"];
          $arr2["nombre"] = $row["nombre"];
          $arr2["urlImagen"] = $row["urlImagen"];
          $arr2["precioUnidad"] = $row["precioUnidad"];
          $arr2["costoManufactura"] = $row["costoManufactura"];
          $arr2["cantidad"] = $row["cantidad"];
          $arr[$i]=$arr2;
          $code = base64_encode( file_get_contents($row['urlImagen']));
          $i++;
           
       }
        echo json_encode ($arr);
    } else {
        echo "0 results";
    }
?>
