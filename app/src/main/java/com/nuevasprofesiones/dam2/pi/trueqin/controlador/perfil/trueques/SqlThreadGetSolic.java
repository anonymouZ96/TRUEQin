package com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil.trueques;

import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Sesion;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.utils.Trueque;

import java.io.IOException;

public class SqlThreadGetSolic extends Thread {
    private boolean exito, op;
    private Trueque[] vecTrueques;

    protected SqlThreadGetSolic(boolean op) {
        this.op = op;
    }

    public void run() {
        try {
            vecTrueques = Sesion.getModelo().getTrueqes(this.op);
            this.exito = true;
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
        }
    }

    public Trueque[] getVecTrueques() {
        return this.vecTrueques;
    }

    public boolean getExito() {
        return this.exito;
    }
}
