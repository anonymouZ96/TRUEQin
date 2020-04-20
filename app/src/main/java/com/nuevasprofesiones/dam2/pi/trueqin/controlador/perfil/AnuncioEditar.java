package com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Sesion;

import java.io.IOException;

public class AnuncioEditar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent i;
        byte op;
        TextView txtTit;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio_editar);
        txtTit = findViewById(R.id.txtNuevoCont);
        i = getIntent();
        op = i.getByteExtra("operac", (byte) -1);
        if (op == 1) {
            txtTit.setText(txtTit.getText().toString().concat("Inserte el nuevo título"));
        } else {
            if (op == 2) {
                txtTit.setText(txtTit.getText().toString().concat("Inserte la nueva descripción"));
            } else {
                if (op == 3) {
                    txtTit.setText(txtTit.getText().toString().concat("Inserte la nueva ubicación"));
                } else {
                    if (op == 4) {
                        txtTit.setText(txtTit.getText().toString().concat("Inserte los nuevos puntos necesarios"));
                    }
                }
            }
        }
    }

    public void actualizaContenido(View v) {
        Intent i;
        byte op;
        boolean exito;
        SqlThreadEditar sqlThreadEditar;
        TextView edCont, txtErr;
        i = getIntent();
        try {
            edCont = findViewById(R.id.edNewCont);
            txtErr = findViewById(R.id.txtErrorEdit);
            op = i.getByteExtra("operac", (byte) -1);
            sqlThreadEditar = new SqlThreadEditar(edCont.getText().toString(), i.getIntExtra("idAnuncio", -1), op);
            sqlThreadEditar.start();
            sqlThreadEditar.join();
            if (Sesion.getResultados()[0]) {
                exito = true;
                if (!Sesion.getResultados()[1]) {
                    if (op == 1) {
                        exito = false;
                        txtErr.setText("Título no válido.");
                    } else {
                        if (op == 2) {
                            exito = false;
                            txtErr.setText("Descripción no válida.");
                        } else {
                            if (op == 3) {
                                exito = false;
                                txtErr.setText("Ubicación no válida");
                            } else {
                                if (op == 4) {
                                    exito = false;
                                    txtErr.setText("Puntos necesarios no válidos.");
                                }
                            }
                        }
                    }
                } else {
                    if (exito) {
                        Toast.makeText(this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }
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

    public void onDestroy() {
        super.onDestroy();
    }

    class SqlThreadEditar extends Thread {
        private String newCont;
        private int idAnunc;
        private byte oper;

        SqlThreadEditar(String newCont, int idAnunc, byte oper) {
            this.newCont = newCont;
            this.idAnunc = idAnunc;
            this.oper = oper;
        }

        public void run() {
            try {
                Sesion.getModelo().editarAnuncio(this.newCont, this.idAnunc, this.oper);
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            } catch (ClassNotFoundException cnfe) {
                System.err.println(cnfe.getMessage());
            }
        }
    }
}
