package com.nuevasprofesiones.dam2.pi.trueqin.modelo;

import com.nuevasprofesiones.dam2.pi.trueqin.modelo.utils.Anuncio;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.utils.Trueque;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.utils.Usuario;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;

public class Modelo {

    final private String SERVER = "192.168.1.36";
    //final private String SERVER = "192.168.43.206";
    final private int PUERTO = 6000;
    private Socket clientSocket;
    private InetSocketAddress address;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private DataInputStream dis;
    private DataOutputStream dos;
    private String[] categorias;

    public Modelo() throws NullPointerException, IOException {
        this.conexion();
        this.buscarCateg();
    }

    public void conexion() throws NullPointerException, IOException {
        this.clientSocket = new Socket();
        this.address = new InetSocketAddress(SERVER, PUERTO);
        this.clientSocket.connect(address);
        this.ois = new ObjectInputStream(this.clientSocket.getInputStream());
        this.dis = new DataInputStream(this.clientSocket.getInputStream());
        this.oos = new ObjectOutputStream(this.clientSocket.getOutputStream());
        this.dos = new DataOutputStream(this.clientSocket.getOutputStream());
    }

    public void obtienePuntos() throws IOException {
        this.dos.writeByte(12);
        this.dos.flush();
        Sesion.setPuntos(this.dis.readShort());
    }

    public void cierraConexion() throws IOException {
        this.dos.writeByte(13);
        this.ois.close();
        this.oos.close();
        this.dos.close();
        this.dis.close();
        this.clientSocket.close();
    }

    public void inicioSesion(String email, String contras) throws IOException, ClassNotFoundException {
        String[] valores = {email, contras};
        this.dos.writeByte(1);
        this.dos.flush();
        this.oos.writeObject(valores);
        this.oos.flush();
        valores = (String[]) this.ois.readObject();
        Sesion.setResultados((boolean[]) this.ois.readObject());
        if (Sesion.getResultados()[1] && Integer.parseInt(valores[0]) > 0) {
            Sesion.setId(Integer.parseInt(valores[0]));
            Sesion.setPuntos(Integer.parseInt(valores[1]));
        }
    }

    public void registroUser(Usuario usuario) throws IOException, ClassNotFoundException {
        int idUS;
        this.dos.writeByte(2);
        this.dos.flush();
        this.oos.writeObject(usuario);
        this.oos.flush();
        idUS = this.dis.readInt();
        Sesion.setResultados((boolean[]) this.ois.readObject());
        if (Sesion.getResultados()[0]) {
            Sesion.setId(idUS);
            Sesion.setPuntos(300);
        }
    }

    public String[] obtieneDatosUsuarios(int idUs) throws SQLException, IOException, ClassNotFoundException {
        String[] datosUsuarios;
        this.dos.writeByte(7);
        this.dos.flush();
        this.dos.writeInt(idUs);
        this.dos.flush();
        datosUsuarios = (String[]) this.ois.readObject();
        return datosUsuarios;
    }

    public String[] obtieneAutorAnuncio(int idAnunc) {
        String[] datos;
        datos = null;
        try {
            this.dos.writeByte(4);
            this.dos.flush();
            this.dos.writeByte(2);
            this.dos.flush();
            this.dos.writeInt(idAnunc);
            this.dos.flush();
            datos = (String[]) this.ois.readObject();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
        }
        return datos;
    }

    public void insertaAnuncio(Anuncio anunc) throws IOException, ClassNotFoundException {
        this.dos.writeByte(8);
        this.dos.flush();
        anunc.setIdUs(Sesion.getId());
        this.oos.writeObject(anunc);
        this.oos.flush();
        Sesion.setResultados((boolean[]) this.ois.readObject());
    }

    public Anuncio[] buscarAnuncios(String texto, String ubicacion) throws IOException, ClassNotFoundException {
        Anuncio[] vecAnuncio;
        String[] valores;
        valores = new String[2];

        this.dos.writeByte(3);
        this.dos.flush();

        this.dos.writeByte(2);
        this.dos.flush();
        valores[0] = texto;
        valores[1] = ubicacion;
        this.oos.writeObject(valores);
        this.dos.flush();
        vecAnuncio = (Anuncio[]) this.ois.readObject();
        Sesion.setResultados((boolean[]) this.ois.readObject());
        return vecAnuncio;
    }

    public Anuncio[] buscarAnuncios(byte idCateg) throws IOException, ClassNotFoundException {
        Anuncio[] vecAnuncio;
        this.dos.writeByte(3);
        this.dos.flush();
        this.dos.writeByte(3);
        this.dos.flush();
        this.dos.writeByte(idCateg);
        this.dos.flush();
        this.dos.writeInt(Sesion.getId());
        this.dos.flush();
        vecAnuncio = (Anuncio[]) this.ois.readObject();
        return vecAnuncio;
    }

    public Anuncio[] buscarAnuncios() throws IOException, ClassNotFoundException {
        Anuncio[] vecAnuncio;
        this.dos.writeByte(3);
        this.dos.flush();
        this.dos.writeByte(1);
        this.dos.flush();
        vecAnuncio = (Anuncio[]) this.ois.readObject();
        return vecAnuncio;
    }

    public Anuncio verAnuncio(int idAnunc) {
        Anuncio anuncio;
        anuncio = null;
        try {
            this.dos.writeByte(4);
            this.dos.flush();
            this.dos.writeByte(1);
            this.dos.flush();
            this.dos.writeInt(idAnunc);
            this.dos.flush();
            anuncio = (Anuncio) this.ois.readObject();
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
        }
        return anuncio;
    }

    public boolean solicitar(int idAnunc) throws IOException {
        this.dos.writeByte(4);
        this.dos.flush();
        this.dos.writeByte(5);
        this.dos.flush();
        this.dos.writeInt(Sesion.getId());
        this.dos.flush();
        this.dos.writeInt(idAnunc);
        this.dos.flush();
        return this.dis.readBoolean();
    }

    public boolean cancelar(int idAnunc) throws IOException {
        this.dos.writeByte(4);
        this.dos.flush();
        this.dos.writeByte(6);
        this.dos.flush();
        this.dos.writeInt(idAnunc);
        this.dos.flush();
        return this.dis.readBoolean();
    }

    public String[] getCategorias() {
        return this.categorias;
    }

    public void buscarCateg() {
        try {
            this.dos.writeByte(5);
            this.dos.flush();
            this.categorias = (String[]) this.ois.readObject();
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    public void editarMisDatos(String[] nuevo) throws IOException, ClassNotFoundException {
        this.dos.writeByte(10);
        this.dos.flush();
        this.oos.writeObject(nuevo);
        this.oos.flush();
        Sesion.setResultados((boolean[]) this.ois.readObject());
    }

    public void editarAnuncio(String nuevo, int id, byte op) throws IOException, ClassNotFoundException {
        String[] valores = {nuevo, Integer.toString(id), Byte.toString(op)};
        this.dos.writeByte(11);
        this.dos.flush();
        this.oos.writeObject(valores);
        this.oos.flush();
        Sesion.setResultados((boolean[]) this.ois.readObject());
    }

    public Trueque[] getTrueqes(boolean op) throws IOException, ClassNotFoundException {  // True solicitudes / False solicitantes
        Trueque[] vecTrueques;
        this.dos.writeByte(6);
        this.dos.flush();
        this.dos.writeByte(2);
        this.dos.flush();
        this.dos.writeBoolean(op);
        this.dos.flush();
        vecTrueques = (Trueque[]) this.ois.readObject();
        return vecTrueques;
    }

    public boolean confirmarTrueque(int idAnunc, int idUS, byte opcion) throws IOException {
        int[] valores;
        this.dos.writeByte(6);
        this.dos.flush();
        this.dos.writeByte(1);
        this.dos.flush();
        valores = new int[3];
        valores[0] = idAnunc;
        valores[1] = idUS;
        valores[2] = opcion;
        this.oos.writeObject(valores);
        this.oos.flush();
        return this.dis.readBoolean();
    }

    public boolean check(String cad, byte op) throws IOException {
        this.dos.writeByte(9);
        this.dos.flush();
        this.dos.writeByte(op);
        this.dos.flush();
        this.dos.writeUTF(cad);
        this.dos.flush();
        return this.dis.readBoolean();
    }

}
