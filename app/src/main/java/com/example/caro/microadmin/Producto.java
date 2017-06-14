package com.example.caro.microadmin;

import java.io.Serializable;

/**
 * Created by Caro on 31/05/2017.
 */

public class Producto implements Serializable{
    private int IDProducto;
    private String codigo;
    private String nombre;
    private String URL;
    private double precioUnidad;
    private double costoManufactura;
    private int cantidad;


    public Producto(int IDProducto, String codigo, String nombre, String URL, double precioUnidad, double costoManufactura, int cantidad) {
        this.IDProducto = IDProducto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.URL = URL;
        this.precioUnidad = precioUnidad;
        this.costoManufactura = costoManufactura;
        this.cantidad = cantidad;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setCostoManufactura(double costoManufactura) {
        this.costoManufactura = costoManufactura;
    }

    public void setIDProducto(int IDProducto) {
        this.IDProducto = IDProducto;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPrecioUnidad(double precioUnidad) {
        this.precioUnidad = precioUnidad;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setCantidad(int cantidad){this.cantidad = cantidad;}

    public double getCostoManufactura() {
        return costoManufactura;
    }

    public double getPrecioUnidad() {
        return precioUnidad;
    }

    public int getIDProducto() {
        return IDProducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getURL() {
        return URL;
    }

    public int getCantidad() {
        return cantidad;
    }
}
