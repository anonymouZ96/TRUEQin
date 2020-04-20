package com.nuevasprofesiones.dam2.pi.trueqin.modelo;

public class Sesion {
    private static Modelo modelo;
    private static int id, puntos;
    private static boolean[] resultados;

    public static void setModelo(Modelo modelo) {
        Sesion.modelo = modelo;
    }

    public static void setId(int id) {
        Sesion.id = id;
    }

    public static void setPuntos(int puntos) {
        Sesion.puntos = puntos;
    }

    public static Modelo getModelo() {
        return modelo;
    }

    public static int getId() {
        return id;
    }

    public static int getPuntos() {
        return puntos;
    }

    public static boolean[] getResultados() {
        return resultados;
    }

    public static void setResultados(boolean[] resultados) {
        Sesion.resultados = resultados;
    }
}
