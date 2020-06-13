package com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Sesion;

import java.io.IOException;
import java.sql.SQLException;

public class EdMisDatos extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ed_mis_datos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        TextView txtNomApes, txtFecNac, edEmail, edTelef;
        String[] datosUser;
        SqlThreadGetDatos sqlThreadGetDatos;

        sqlThreadGetDatos = new SqlThreadGetDatos();
        try {
            sqlThreadGetDatos.setId(Sesion.getId());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void operacEdDatosPerfil(View view) {
        String[] datos;
        boolean exito;
        TextView edEmail, edTelef, edContrasOld, edContras1, edContras2;
        TextInputLayout txtInputEmailPerfil, txtInputTelefPerfil, txtInputPasOldPerfil, txtInputPas1Perfil;
        String email, telef, contras1, contras2, contrasOld;
        SqlThreadSetDatos sqlThreadSetDatos;
        try {
            edEmail = findViewById(R.id.edEmailPerfil);
            txtInputEmailPerfil = findViewById(R.id.txtInputEmailPerfil);
            edTelef = findViewById(R.id.edTelefPerfil);
            txtInputTelefPerfil = findViewById(R.id.txtInputTelefPerfil);
            edContrasOld = findViewById(R.id.edPasOldPerfil);
            edContras1 = findViewById(R.id.edPas1Perfil);
            edContras2 = findViewById(R.id.edPas2Perfil);
            txtInputPasOldPerfil = findViewById(R.id.txtInputContrasOldPerfil);
            txtInputPas1Perfil = findViewById(R.id.txtInputContras1Perfil);

            txtInputEmailPerfil.setErrorEnabled(false);
            txtInputTelefPerfil.setErrorEnabled(false);
            txtInputPasOldPerfil.setErrorEnabled(false);
            txtInputPas1Perfil.setErrorEnabled(false);

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
                    txtInputEmailPerfil.setError(getString(R.string.email_error));
                    txtInputEmailPerfil.setErrorEnabled(true);
                }
                if (!Sesion.getResultados()[2]) {
                    exito = false;
                    txtInputEmailPerfil.setError(getString(R.string.email_usado_error));
                    txtInputEmailPerfil.setErrorEnabled(true);
                }
                if (!Sesion.getResultados()[3]) {
                    exito = false;
                    txtInputTelefPerfil.setError(getString(R.string.tel_error));
                    txtInputTelefPerfil.setErrorEnabled(true);
                }
                if (!Sesion.getResultados()[4]) {
                    exito = false;
                    txtInputPas1Perfil.setError(getString(R.string.pass_error));
                    txtInputPas1Perfil.setErrorEnabled(true);
                }
                if (!Sesion.getResultados()[5]) {
                    exito = false;
                    txtInputPas1Perfil.setError(getString(R.string.pass_nocoincide_error));
                    txtInputPas1Perfil.setErrorEnabled(true);
                }
                if (!Sesion.getResultados()[6]) {
                    exito = false;
                    txtInputPasOldPerfil.setError(getString(R.string.passold_error));
                    txtInputPasOldPerfil.setErrorEnabled(true);
                }
                if (exito) {
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                    Toast.makeText(this, R.string.actualizado_ed_datos, Toast.LENGTH_SHORT).show();
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
        builder.setMessage(R.string.error_dialogo).setTitle("ERROR");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}

class SqlThreadGetDatos extends Thread {
    private String[] datosUser;
    private int idUs;
    private boolean exito;

    public void run() {
        try {
            this.exito = false;
            this.datosUser = Sesion.getModelo().obtieneDatosUsuarios(idUs);
            this.exito = true;
        } catch (SQLException sqle) {
            System.err.println(sqle.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
        }
    }

    public void setId(int idUs) {
        this.idUs = idUs;
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