package com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.utils.Anuncio;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Sesion;

import java.io.IOException;

public class NuevoAnuncio extends AppCompatActivity {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Spinner spinCateg;
        setContentView(R.layout.activity_nuevo_anuncio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Sesion.getModelo().getCategorias());
        spinCateg = (Spinner) findViewById(R.id.spinCat);
        spinCateg.setAdapter(adapter);
    }

    public void clickOfrecer(View v) {
        TextView txtTitulo, txtDescripcion, txtUbicacion, txtPuntos;
        TextInputLayout txtInputTituloNew, txtInputDescNew, txtInputUbicNew, txtInputPuntosNew;
        String titulo, desc, puntos, ubic;
        Spinner spinCat;
        boolean exito;
        byte i;
        txtTitulo = findViewById(R.id.edTituloNew);
        txtDescripcion = findViewById(R.id.edDescripNew);
        txtPuntos = findViewById(R.id.edPuntosNew);
        txtUbicacion = findViewById(R.id.edUbicacionNew);
        spinCat = findViewById(R.id.spinCat);

        txtInputTituloNew = findViewById(R.id.txtInputTituloNew);
        txtInputDescNew = findViewById(R.id.txtInputDescNew);
        txtInputUbicNew = findViewById(R.id.txtInputUbicacioNew);
        txtInputPuntosNew = findViewById(R.id.txtInputPuntosNew);

        txtInputTituloNew.setErrorEnabled(false);
        txtInputDescNew.setErrorEnabled(false);
        txtInputUbicNew.setErrorEnabled(false);
        txtInputPuntosNew.setErrorEnabled(false);

        try {
            titulo = txtTitulo.getText().toString().trim();
            desc = txtDescripcion.getText().toString().trim();
            puntos = txtPuntos.getText().toString().trim();
            ubic = txtUbicacion.getText().toString().trim();
            SqlThreadNuevo sqlThreadNuevo;
            sqlThreadNuevo = new SqlThreadNuevo(new Anuncio(
                    titulo,
                    desc,
                    ubic,
                    Sesion.getId(),
                    puntos,
                    (byte) (spinCat.getSelectedItemPosition() + 1))
            );
            sqlThreadNuevo.start();
            sqlThreadNuevo.join();
            i = 0;
            if (Sesion.getResultados().length == 5 && Sesion.getResultados()[i]) {
                i++;
                exito = true;
                if (!Sesion.getResultados()[i]) {
                    exito = false;
                    txtInputTituloNew.setErrorEnabled(true);
                    txtInputTituloNew.setError("Título no válido");
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    exito = false;
                    txtInputDescNew.setErrorEnabled(true);
                    txtInputDescNew.setError("Descripción no válida");
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    exito = false;
                    txtInputUbicNew.setErrorEnabled(true);
                    txtInputUbicNew.setError("Ubicación no válida");
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    exito = false;
                    txtInputPuntosNew.setErrorEnabled(true);
                    txtInputPuntosNew.setError("Puntos no válidos");
                }
                if (exito) {
                    h.sendEmptyMessage(1);
                    finish();
                }
            } else {
                creaDialogosError();
            }
        } catch (
                InterruptedException ie) {
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

    protected void onDestroy() {
        super.onDestroy();
    }

    Handler h = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Toast.makeText(getApplicationContext(), "Anuncio añadido", Toast.LENGTH_SHORT).show();
            }
        }
    };

    class SqlThreadNuevo extends Thread {
        private Anuncio anuncio;

        public SqlThreadNuevo(Anuncio anuncio) {
            this.anuncio = anuncio;
        }

        public void run() {
            try {
                Sesion.getModelo().insertaAnuncio(anuncio);
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            } catch (ClassNotFoundException cnfe) {
                System.err.println(cnfe.getMessage());
            }
        }
    }

}
