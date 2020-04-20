package com.nuevasprofesiones.dam2.pi.trueqin.controlador.general;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil.AnuncioEditar;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.utils.Anuncio;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Sesion;

import java.io.IOException;
import java.sql.SQLException;

public class AnuncioActivity extends AppCompatActivity {

    private byte k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio);
        k = 0;
        operacAnunucio();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (k == 1) {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
    }

    private void operacAnunucio() {
        Anuncio anuncio;
        TextView txtTitulo, txtDesc, txtUbic, txtPuntos, txtNombre, txtContacto;
        SqlThreadRellanaAnuncio sqlThreadRellanaAnuncio;
        try {
            final Intent i;
            txtTitulo = findViewById(R.id.txtTituloAnunc);
            txtDesc = findViewById(R.id.txtDescAnunc);
            txtUbic = findViewById(R.id.txtUbicAnunc);
            txtPuntos = findViewById(R.id.txtPuntosAnunc);
            txtNombre = findViewById(R.id.txtNomAnunc);
            txtContacto = findViewById(R.id.txtContAnunc);
            i = getIntent();
            sqlThreadRellanaAnuncio = new SqlThreadRellanaAnuncio();
            sqlThreadRellanaAnuncio.start();
            sqlThreadRellanaAnuncio.join();
            if (sqlThreadRellanaAnuncio.getExito()) {
                anuncio = sqlThreadRellanaAnuncio.getAnuncio();
                txtTitulo.setText(anuncio.getTitulo());
                txtDesc.setText(txtDesc.getText().toString().concat(anuncio.getDescrip()));
                txtUbic.setText(txtUbic.getText().toString().concat(anuncio.getUbicacion()));
                txtPuntos.setText(txtPuntos.getText().toString().concat(anuncio.getPuntos()));
                txtNombre.setText(txtNombre.getText().toString().concat(sqlThreadRellanaAnuncio.getNom()));
                txtContacto.setText(txtContacto.getText().toString().concat("\tE-mail: ").concat(sqlThreadRellanaAnuncio.getEmail()).concat("\n\tTeléfono: ").concat(sqlThreadRellanaAnuncio.getTel()));
                if (i.getByteExtra("oper", (byte) -1) == 1) {
                    txtTitulo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            intent = new Intent(getApplicationContext(), AnuncioEditar.class);
                            intent.putExtra("idAnuncio", i.getIntExtra("idAnuncio", -1));
                            intent.putExtra("operac", (byte) 1);
                            k = 1;
                            startActivity(intent);
                        }
                    });
                    txtDesc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            intent = new Intent(getApplicationContext(), AnuncioEditar.class);
                            intent.putExtra("idAnuncio", i.getIntExtra("idAnuncio", -1));
                            intent.putExtra("operac", (byte) 2);
                            k = 1;
                            startActivity(intent);
                        }
                    });
                    txtUbic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            intent = new Intent(getApplicationContext(), AnuncioEditar.class);
                            intent.putExtra("idAnuncio", i.getIntExtra("idAnuncio", -1));
                            intent.putExtra("operac", (byte) 3);
                            k = 1;
                            startActivity(intent);
                        }
                    });
                    txtPuntos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent;
                            intent = new Intent(getApplicationContext(), AnuncioEditar.class);
                            intent.putExtra("idAnuncio", i.getIntExtra("idAnuncio", -1));
                            intent.putExtra("operac", (byte) 4);
                            k = 1;
                            startActivity(intent);
                        }
                    });
                } else {
                    if (i.getByteExtra("oper", (byte) -1) == 2) {
                        ImageButton btnTrueq;
                        btnTrueq = findViewById(R.id.butTrueqAnunc);
                        btnTrueq.setVisibility(View.VISIBLE);
                        btnTrueq.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    SqlThreadSolicitarAnuncio sqlThreadSolicitarAnuncio;
                                    sqlThreadSolicitarAnuncio = new SqlThreadSolicitarAnuncio();
                                    sqlThreadSolicitarAnuncio.start();
                                    sqlThreadSolicitarAnuncio.join();
                                    if (sqlThreadSolicitarAnuncio.getCodErr() == 0) {
                                        Toast.makeText(getApplicationContext(), "Solicitado!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (sqlThreadSolicitarAnuncio.getCodErr() == -1) {
                                            creaDialogosError("Se ha producido un error");
                                        } else {
                                            if (sqlThreadSolicitarAnuncio.getCodErr() == 1) {
                                                creaDialogosError("Ya lo ha solicitado previamente");
                                            } else {
                                                if (sqlThreadSolicitarAnuncio.getCodErr() == 2) {
                                                    creaDialogosError("No tienes puntos suficientes");
                                                }
                                            }
                                        }
                                    }
                                } catch (InterruptedException ie) {
                                    System.err.println(ie.getMessage());
                                    creaDialogosError("Se ha producido un error");
                                }
                            }
                        });
                    }
                }
            } else {
                creaDialogosError("Se ha producido un error");
            }
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

    class SqlThreadRellanaAnuncio extends Thread {
        private Anuncio anuncio;
        private String nom, email, tel;
        private String[] datos;
        int idAnunc;
        private boolean exito;

        public void run() {
            Intent i;
            i = getIntent();
            idAnunc = i.getIntExtra("idAnuncio", -1);
            anuncio = Sesion.getModelo().verAnuncio(idAnunc);
            datos = Sesion.getModelo().obtieneAutorAnuncio(idAnunc);
            nom = datos[1];
            email = datos[2];
            tel = datos[3];
            exito = true;
        }

        public Anuncio getAnuncio() {
            return this.anuncio;
        }

        public String getNom() {
            return nom;
        }

        public String getEmail() {
            return email;
        }

        public String getTel() {
            return tel;
        }

        public boolean getExito() {
            return exito;
        }
    }

    class SqlThreadSolicitarAnuncio extends Thread {
        private byte codErr;

        public void run() {
            try {
                int idAnunc;
                Intent i;
                i = getIntent();
                idAnunc = i.getIntExtra("idAnuncio", -1);
                codErr = 0;
                if (!Sesion.getModelo().check(Integer.toString(idAnunc), (byte) 4)) {
                    codErr = 1;
                } else {
                    if (!Sesion.getModelo().check(Integer.toString(idAnunc), (byte) 3)) {
                        codErr = 2;
                    } else {
                        if (!Sesion.getModelo().solicitar(idAnunc)) {
                            codErr = -1;
                        }
                    }
                }
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            }
        }

        public byte getCodErr() {
            return this.codErr;
        }
    }
}
