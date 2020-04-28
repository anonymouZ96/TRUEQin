package com.nuevasprofesiones.dam2.pi.trueqin.controlador;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Sesion;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.utils.Usuario;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RegistroUsuario extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void clickRegistrar(View view) {
        SqlThreadReg sqlThreadReg;
        TextView txtNom, txtApes, txtEmail, txtTelef, txtFecha, txtContras1, txtContras2;
        TextInputLayout txtInputNomReg, txtInputApesReg, txtInputEmailReg, txtInputFechaReg, txtInputTelReg, txtInputPas1Reg;
        String nombre, apes, email, telef, fecha, contras1, contras2;
        Intent intent;
        byte i;
        txtInputNomReg = findViewById(R.id.txtInputNomReg);
        txtInputApesReg = findViewById(R.id.txtInputApesReg);
        txtInputEmailReg = findViewById(R.id.txtInputEmailReg);
        txtInputFechaReg = findViewById(R.id.txtInputFechaReg);
        txtInputTelReg = findViewById(R.id.txtInputTelefReg);
        txtInputPas1Reg = findViewById(R.id.txtInputContras1Reg);
        txtNom = findViewById(R.id.edNomReg);
        txtApes = findViewById(R.id.edApesReg);
        txtEmail = findViewById(R.id.edEmailReg);
        txtTelef = findViewById(R.id.edTelefReg);
        txtFecha = findViewById(R.id.edFechaNacReg);
        txtContras1 = findViewById(R.id.edPas1Reg);
        txtContras2 = findViewById(R.id.edPas2Reg);

        txtInputNomReg.setErrorEnabled(false);
        txtInputApesReg.setErrorEnabled(false);
        txtInputEmailReg.setErrorEnabled(false);
        txtInputFechaReg.setErrorEnabled(false);
        txtInputTelReg.setErrorEnabled(false);
        txtInputPas1Reg.setErrorEnabled(false);

        nombre = txtNom.getText().toString().trim();
        apes = txtApes.getText().toString().trim();
        email = txtEmail.getText().toString().trim();
        fecha = txtFecha.getText().toString().trim();
        contras1 = txtContras1.getText().toString().trim();
        contras2 = txtContras2.getText().toString().trim();
        telef = txtTelef.getText().toString().trim();

        try {
            sqlThreadReg = new SqlThreadReg(new Usuario(nombre,
                    apes,
                    email,
                    fecha,
                    contras1,
                    contras2,
                    telef));
            sqlThreadReg.start();
            sqlThreadReg.join();
            i = 0;
            if (Sesion.getResultados()[i]) {
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtInputEmailReg.setError("E-mail no válido");
                    txtInputEmailReg.setErrorEnabled(true);
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtInputEmailReg.setError("Este E-mail ya está registrado");
                    txtInputEmailReg.setErrorEnabled(true);
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtInputPas1Reg.setError("Contraseña no válida, debe contener números y letras");
                    txtInputPas1Reg.setErrorEnabled(true);
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtInputPas1Reg.setError("Las contraseñas no coinciden");
                    txtInputPas1Reg.setErrorEnabled(true);
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtInputNomReg.setError("Nombre no válido");
                    txtInputNomReg.setErrorEnabled(true);
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtInputApesReg.setError("Apellidos no válidos");
                    txtInputApesReg.setErrorEnabled(true);
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtInputTelReg.setError("Teléfono no válido");
                    txtInputTelReg.setErrorEnabled(true);
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtInputFechaReg.setError("Fecha no válida.");
                    txtInputFechaReg.setErrorEnabled(true);
                }

                if (Sesion.getId() > 0) {
                    creaDialogosGuardar(email, contras1);
//                    intent = new Intent(this, ActivityInstrucciones.class);
//                    startActivity(intent);
                }
            }
        } catch (InterruptedException ie) {
            System.err.println(ie.getMessage());
            creaDialogosError();
        }

    }

    private void creaDialogosError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Se ha producido un error").setTitle("ERROR");
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
                    creaDialogosError();
                } catch (IOException ioe) {
                    System.err.println(ioe.getMessage());
                    creaDialogosError();
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

    class SqlThreadReg extends Thread {
        Usuario usuario;

        public SqlThreadReg(Usuario usuario) {
            this.usuario = usuario;
        }

        public void run() {
            try {
                Sesion.getModelo().registroUser(usuario);
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            } catch (ClassNotFoundException cnfe) {
                System.err.println(cnfe.getMessage());
            }
        }
    }
}