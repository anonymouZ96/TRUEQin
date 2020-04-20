package com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Sesion;

import java.io.IOException;
import java.sql.SQLException;

public class EdMisDatos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ed_mis_datos);
        TextView txtNomApes, txtFecNac, edEmail, edTelef;
        String[] datosUser;
        SqlThreadGetDatos sqlThreadGetDatos;

        sqlThreadGetDatos = new SqlThreadGetDatos();
        try {
            sqlThreadGetDatos.start();
            sqlThreadGetDatos.join();
            if (sqlThreadGetDatos.getExito()) {
                datosUser = sqlThreadGetDatos.getDatosUser();

                txtNomApes = findViewById(R.id.txtNombrePerfil);
                txtFecNac = findViewById(R.id.txtFecNacPerfil);
                edEmail = findViewById(R.id.edEmailPerfil);
                edTelef = findViewById(R.id.edTelefPerfil);

                txtNomApes.setText(txtNomApes.getText().toString().concat(" ").concat(datosUser[0]));
                txtFecNac.setText(txtFecNac.getText().toString().concat(" ").concat(datosUser[1]));
                edEmail.setHint(datosUser[2]);
                edTelef.setHint(datosUser[3]);
            } else {
                creaDialogosError();
            }
        } catch (InterruptedException ie) {
            System.err.println(ie.getMessage());
            creaDialogosError();
        }
    }

    public void operacEdDatosPerfil(View view) {
        TextView edEmail, txtErrorEmail, edTelef, txtErrorTel, edContrasOld, edContras1, edContras2, txtErrorContras;
        String email, telef, contras1, contras2, contrasOld;
        SqlThreadSetDatos sqlThreadSetDatos;

        try {
            edEmail = findViewById(R.id.edEmailPerfil);
            txtErrorEmail = findViewById(R.id.txtErrorEmailPerfil);
            edTelef = findViewById(R.id.edTelefPerfil);
            txtErrorTel = findViewById(R.id.txtErrorTel);
            edContrasOld = findViewById(R.id.edPasOldPerfil);
            edContras1 = findViewById(R.id.edPas1Perfil);
            edContras2 = findViewById(R.id.edPas2Perfil);
            txtErrorContras = findViewById(R.id.txtErrorPass1);
            txtErrorEmail.setText("");
            txtErrorTel.setText("");
            txtErrorContras.setText("");
            String[] datos;
            boolean exito;
            email = edEmail.getText().toString().trim();
            telef = edTelef.getText().toString().trim();
            contras1 = edContras1.getText().toString().trim();
            contras2 = edContras2.getText().toString();
            contrasOld = edContrasOld.getText().toString();
            datos = new String[5];
            datos[0] = email;
            datos[1] = telef;
            datos[2] = contras1;
            datos[3] = contras2;
            datos[4] = contrasOld;
            sqlThreadSetDatos = new SqlThreadSetDatos(datos);
            sqlThreadSetDatos.start();
            sqlThreadSetDatos.join();
            if (Sesion.getResultados().length == 7 && Sesion.getResultados()[0]) {
                exito = true;
                if (!Sesion.getResultados()[1]) {
                    exito = false;
                    txtErrorEmail.setText("E-mail no válido");
                }
                if (!Sesion.getResultados()[2]) {
                    exito = false;
                    txtErrorEmail.setText("Este E-mail ya está registrado");
                }
                if (!Sesion.getResultados()[3]) {
                    exito = false;
                    txtErrorEmail.setText("Este teléfono no es válido");
                }
                if (!Sesion.getResultados()[4]) {
                    exito = false;
                    txtErrorEmail.setText("Esta contraseña no es válida");
                }
                if (!Sesion.getResultados()[5]) {
                    exito = false;
                    txtErrorEmail.setText("Las contraseñas no coinciden");
                }
                if (!Sesion.getResultados()[6]) {
                    exito = false;
                    txtErrorEmail.setText("La contraseña antigua no es correcta");
                }
                if (exito) {
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                }
            } else {
                creaDialogosError();
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

    class SqlThreadGetDatos extends Thread {
        private String[] datosUser;
        private boolean exito;

        public void run() {
            try {
                this.exito = false;
                this.datosUser = Sesion.getModelo().obtieneDatosUsuarios(Sesion.getId());
                this.exito = true;
            } catch (SQLException sqle) {
                System.err.println(sqle.getMessage());
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            } catch (ClassNotFoundException cnfe) {
                System.err.println(cnfe.getMessage());
            }
        }

        public String[] getDatosUser() {
            return this.datosUser;
        }

        public boolean getExito() {
            return this.exito;
        }
    }

    class SqlThreadSetDatos extends Thread {
        private String[] datosUser;

        SqlThreadSetDatos(String[] datosUser) {
            this.datosUser = datosUser;
        }

        public void run() {
            try {
                Sesion.getModelo().editarMisDatos(this.datosUser);
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            } catch (ClassNotFoundException cnfe) {
                System.err.println(cnfe.getMessage());
            }
        }

    }
}
