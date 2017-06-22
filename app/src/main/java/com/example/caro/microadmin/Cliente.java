package com.example.caro.microadmin;

/**
 * Created by Caro on 21/06/2017.
 */

public class Cliente {

    private int IDCliente;
    private String Nombre;
    private String PrimerApellido;
    private String SegundoApellido;
    private String Cedula;
    private String Telefono;

    public Cliente(int IDCliente, String nombre, String primerApellido, String segundoApellido, String cedula, String telefono) {
        Nombre = nombre;
        PrimerApellido = primerApellido;
        SegundoApellido = segundoApellido;
        Cedula = cedula;
        Telefono = telefono;
        this.IDCliente = IDCliente;
    }

    public String getCedula() {
        return Cedula;
    }

    public void setCedula(String cedula) {
        Cedula = cedula;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getPrimerApellido() {
        return PrimerApellido;
    }

    public void setPrimerApellido(String primerApellido) {
        PrimerApellido = primerApellido;
    }

    public String getSegundoApellido() {
        return SegundoApellido;
    }

    public void setSegundoApellido(String segundoApellido) {
        SegundoApellido = segundoApellido;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }
}
