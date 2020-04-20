package com.nuevasprofesiones.dam2.pi.trueqin.controlador;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Sesion;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.utils.Usuario;

import java.io.IOException;

public class RegistroUsuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
    }

    public void clickRegistrar(View view) {
        SqlThreadReg sqlThreadReg;
        TextView txtNom, txtApes, txtEmail, txtTelef, txtFecha, txtContras1, txtContras2, txtErrorNom, txtErrorApes, txtErrorEmail, txtErrorTel, txtErrorFecha, txtErrorContras;
        String nombre, apes, email, telef, fecha, contras1, contras2;
        Intent intent;
        byte i;
        txtNom = findViewById(R.id.edNomReg);
        txtApes = findViewById(R.id.edApesReg);
        txtEmail = findViewById(R.id.edEmailReg);
        txtTelef = findViewById(R.id.edTelefReg);
        txtFecha = findViewById(R.id.edFechaNac);
        txtContras1 = findViewById(R.id.edPas1);
        txtContras2 = findViewById(R.id.edPas2);
        txtErrorNom = findViewById(R.id.txtErrorNomReg);
        txtErrorApes = findViewById(R.id.txtErrorApesReg);
        txtErrorEmail = findViewById(R.id.txtErrorEmailReg);
        txtErrorTel = findViewById(R.id.txtErrorTel);
        txtErrorFecha = findViewById(R.id.txtErrorFecNacReg);
        txtErrorContras = findViewById(R.id.txtErrorPass1Reg);
        txtErrorNom.setText("");
        txtErrorApes.setText("");
        txtErrorEmail.setText("");
        txtErrorTel.setText("");
        txtErrorFecha.setText("");
        txtErrorContras.setText("");

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
                    fecha.substring(fecha.lastIndexOf("/") + 1).concat("/").concat(fecha.substring(fecha.indexOf("/") + 1, fecha.lastIndexOf("/"))).concat("/").concat(fecha.substring(0, fecha.indexOf("/"))),
                    contras1,
                    contras2,
                    telef));
            sqlThreadReg.start();
            sqlThreadReg.join();
            i = 0;
            if (Sesion.getResultados()[i]) {
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtErrorEmail.setText("E-mail no válido.");
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtErrorEmail.setText("Este E-mail ya está registrado.");
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtErrorContras.setText("Contraseña no válida, debe contener números y letras.");
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtErrorContras.setText("Las contraseñas no coinciden.");
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtErrorNom.setText("Nombre no válido.");
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtErrorApes.setText("Apellidos no válidos.");
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtErrorTel.setText("Teléfono no válido");
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtErrorFecha.setText("Fecha no válida.");
                }

                if (Sesion.getId() > 0) {
                    intent = new Intent(this, ActivityInstrucciones.class);
                    startActivity(intent);
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