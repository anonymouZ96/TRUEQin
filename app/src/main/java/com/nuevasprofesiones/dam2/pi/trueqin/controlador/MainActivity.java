package com.nuevasprofesiones.dam2.pi.trueqin.controlador;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Modelo;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Sesion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SqlThreadMod sqlThreadMod;
        String cad;
        TextView edUser, edPass;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        try {
            sqlThreadMod = new SqlThreadMod();
            sqlThreadMod.start();
            sqlThreadMod.join();
            try {
                cad = leeArchivoDatosIni();
                edUser = findViewById(R.id.edEmail);
                edPass = findViewById(R.id.edContras);
                edUser.setText(cad.substring(0, cad.indexOf(";")));
                edPass.setText(cad.substring(cad.indexOf(";") + 1));
            } catch (FileNotFoundException fnfe) {
                System.err.println(fnfe.getMessage());
            } catch (EOFException eofe) {
                System.err.println(eofe.getMessage());
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            }
            if (sqlThreadMod.getExito()) {
                Button btnIniSes = findViewById(R.id.btnIniSesion);
                btnIniSes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        abrePrincipal();
                    }
                });
                Button btnReg = findViewById(R.id.btnRegistro);
                btnReg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        abreRegistro();
                    }
                });
            } else {
                creaDialogosError("Se ha producido un error");
            }
        } catch (InterruptedException ie) {
            System.err.println(ie.getMessage());
            creaDialogosError("Se ha producido un error");
        }
    }

    private String leeArchivoDatosIni() throws FileNotFoundException, IOException {
        String cad, user, pass;
        FileInputStream fileInputStream;
        DataInputStream dataInputStream;
        File f;
        byte i;
        f = new File(getFilesDir().getPath().concat("/inis.dat".replace('/', File.separatorChar)));
        user = "";
        pass = "";
        if (!f.exists()) {
            f.createNewFile();
        } else {
            fileInputStream = new FileInputStream(new File(getFilesDir().getPath().concat("/inis.dat".replace('/', File.separatorChar))));
            dataInputStream = new DataInputStream(fileInputStream);
            cad = "";
            for (i = 0; i < 60; i++) {
                cad += (char) dataInputStream.readChar();
            }
            user = cad.trim();
            cad = "";
            for (i = 0; i < 30; i++) {
                cad += (char) dataInputStream.readChar();
            }
            pass = cad.trim();
            if (dataInputStream != null) {
                dataInputStream.close();
            }
        }
        return user.concat(";").concat(pass);
    }

    private boolean escribeArchivoDatosIni(String user, String pass) throws FileNotFoundException, IOException {
        FileOutputStream fileOutputStream;
        StringBuilder stringBuilder;
        DataOutputStream dataOutputStream;
        boolean exito;
        fileOutputStream = new FileOutputStream(new File(getFilesDir().getPath().concat("/inis.dat".replace('/', File.separatorChar))), false);
        dataOutputStream = new DataOutputStream(fileOutputStream);
        stringBuilder = new StringBuilder(user);
        stringBuilder.setLength(60);
        dataOutputStream.writeChars(stringBuilder.toString());
        stringBuilder = new StringBuilder(pass);
        stringBuilder.setLength(30);
        dataOutputStream.writeChars(stringBuilder.toString());
        if (dataOutputStream != null) {
            dataOutputStream.close();
        }
        exito = true;
        return exito;
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
            Sesion.getModelo().cierraConexion();
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    public void abreRegistro() {
        Intent i;
        i = new Intent(this, RegistroUsuario.class);
        startActivity(i);
    }

    public void abrePrincipal() {
        Button btnIniSes;
        SqlThreadIniSesion sqlThreadIniSesion;
        Intent intent;
        boolean exito;
        TextView txtEmail, txtContras;
        TextInputLayout txtInputEmail, txtInputContras;
        String email, contras, cad;

        txtInputEmail = findViewById(R.id.txtInputEmail);
        txtInputContras = findViewById(R.id.txtInputContras);
        txtEmail = findViewById(R.id.edEmail);
        txtContras = findViewById(R.id.edContras);
        btnIniSes = findViewById(R.id.btnIniSesion);

        txtInputEmail.setErrorEnabled(false);
        txtInputContras.setErrorEnabled(false);

        try {
            email = txtEmail.getText().toString().trim();
            contras = txtContras.getText().toString().trim();
            btnIniSes.setEnabled(false);
            sqlThreadIniSesion = new SqlThreadIniSesion(email, contras);
            sqlThreadIniSesion.start();
            sqlThreadIniSesion.join();
            if (Sesion.getResultados()[0]) {
                if (Sesion.getResultados()[1]) {
                    exito = true;
                    if (!Sesion.getResultados()[2]) {
                        txtEmail.setError("E-mail no válido");
                        txtInputEmail.setErrorEnabled(true);
                        exito = false;
                    }
                    if (!Sesion.getResultados()[3]) {
                        txtContras.setError("Contraseña no válida.");
                        txtInputContras.setErrorEnabled(true);
                        exito = false;
                    }
                    if (exito) {
                        if (Sesion.getId() > 0) {
                            try {
                                cad = leeArchivoDatosIni();
                                if (!email.equals(cad.substring(0, cad.indexOf(";"))) && !contras.equals(cad.substring(cad.indexOf(";") + 1))) {
                                    creaDialogosGuardar(email, contras);
                                } else {
                                    intent = new Intent(this, ActivityInstrucciones.class);
                                    startActivity(intent);
                                }
                            } catch (FileNotFoundException fnfe) {
                                System.err.println(fnfe.getMessage());
                            } catch (EOFException eofe) {
                                System.err.println(eofe.getMessage());
                                creaDialogosGuardar(email, contras);
                            } catch (IOException ioe) {
                                System.err.println(ioe.getMessage());
                            }
                        } else {
                            Toast.makeText(this, "El usuario o la contraseña introducida no es correcta", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    creaDialogosError("Se ha producido un error");
                }
            } else {
                creaDialogosError("Ha introducido varias veces las credenciales de forma incorrecta, espere un poco y vuélvalas a introducir");
            }
            btnIniSes.setEnabled(true);
        } catch (InterruptedException ie) {
            System.err.println(ie.getMessage());
            creaDialogosError("Se ha producido un error");
        }
    }

    private void creaDialogosError(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg).setTitle("ERROR");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void creaDialogosGuardar(final String email, final String pass) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea guardar sus datos de inicio de sesión?").setTitle("Guardar datos");
        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent;
                try {
                    escribeArchivoDatosIni(email, pass);
                    intent = new Intent(getApplicationContext(), ActivityInstrucciones.class);
                    startActivity(intent);
                } catch (FileNotFoundException fnfe) {
                    System.err.println(fnfe.getMessage());
                    creaDialogosError("Se ha producido un error");
                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                    creaDialogosError("Se ha producido un error");
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent;
                intent = new Intent(getApplicationContext(), ActivityInstrucciones.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    class SqlThreadIniSesion extends Thread {
        private String email, contras;

        public SqlThreadIniSesion(String email, String contras) {
            this.email = email;
            this.contras = contras;
        }

        public void run() {
            try {
                Sesion.getModelo().exitoInicioSes(this.email, this.contras);
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            } catch (ClassNotFoundException cnfe) {
                System.err.println(cnfe.getMessage());
            }
        }
    }

    class SqlThreadMod extends Thread {
        private boolean exito;

        public void run() {
            Modelo modelo;
            this.exito = false;
            try {
                modelo = new Modelo();
                Sesion.setModelo(modelo);
                this.exito = true;
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            }
        }

        public boolean getExito() {
            return this.exito;
        }
    }
}