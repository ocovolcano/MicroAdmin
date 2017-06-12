	<?php
			$con = mysqli_connect("localhost", "id1453347_microadmin", "microadmin17", "id1453347_microadmin");

			$fecha = $_POST["fecha"];
			$jsonVentas = $_POST["listaVentas"];
			$arrayVentas = json_decode( $jsonVentas, true );
			$response = array();
	    	echo $listaVentas;

	    	function agregarVenta(){
	    		global $con, $fecha;

	    		$stmt = mysqli_prepare($con, "INSERT INTO Venta(Fecha) VALUES (?)");
	    		mysqli_stmt_bind_param($stmt, 's', $fecha);
	    		mysqli_stmt_execute($stmt);
	    		$idVenta = mysqli_insert_id($con);
	    		return agregarVentaXProducto($idVenta);

			}

			function agregarVentaXProducto($idVenta){
				global $con, $arrayVentas;
    			$stmtGetCantidad = mysqli_prepare($con, "SELECT Cantidad FROM Inventario WHERE idProducto = ?");
				$stmt = mysqli_prepare($con, "INSERT INTO VentaXProducto VALUES (?,?,?)");
				$stmtInventario = mysqli_prepare($con, "UPDATE Inventario SET Cantidad = ? WHERE IDProducto = ?");
	    		
	    		foreach($arrayVentas as $item) { 
	    			$idProducto = $item['idProducto']; 
    				$cantidad = $item['cantidad'];

	    			mysqli_stmt_bind_param($stmtGetCantidad, 'i', $idProducto);
					mysqli_stmt_execute($stmtGetCantidad);
					$result =  mysqli_stmt_get_result($statement);
					echo $result;
					if($cantidad <= $result){
						mysqli_stmt_bind_param($stmt, 'iii', $idVenta, $idProducto, $cantidad);
    					mysqli_stmt_execute($stmt);

    					$nuevaCantidad = $result - $cantidad;
    					mysqli_stmt_bind_param($stmtInventario, 'ii', $idProducto, $nuevaCantidad);
    					mysqli_stmt_execute($stmtInventario);

    					
					}else{
						
    					return false; 
					}
				}
				return true;
	    	
			}


			if(agregarVenta()){
				$response["success"] = true;
			}else{
				$response["success"] = false;
			}

			echo json_encode($response);
	?>
