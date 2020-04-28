package com.nuevasprofesiones.dam2.pi.trueqin.controlador;

import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Sesion;

import java.io.IOException;

class SqlThreadClose extends Thread {

    public void run() {
        try {
            Sesion.getModelo().cierraConexion();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }
}