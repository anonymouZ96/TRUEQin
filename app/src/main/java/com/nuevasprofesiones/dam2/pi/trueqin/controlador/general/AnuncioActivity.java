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
import java.util.GregorianCalendar;

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
        TextView txtTitulo, txtDesc, txtUbic, txtPuntos, txtCat;
        final SqlThreadRellanaAnuncio sqlThreadRellanaAnuncio;
        try {
            final Intent i;
            txtTitulo = findViewById(R.id.txtTituloAnunc);
            txtDesc = findViewById(R.id.txtDescAnunc);
            txtUbic = findViewById(R.id.txtUbicAnunc);
            txtPuntos = findViewById(R.id.txtPuntosAnunc);
            txtCat = findViewById(R.id.txtCategAnunc);
            i = getIntent();
            sqlThreadRellanaAnuncio = new SqlThreadRellanaAnuncio();
            sqlThreadRellanaAnuncio.start();
            sqlThreadRellanaAnuncio.join();
            if (sqlThreadRellanaAnuncio.getExito()) {
                anuncio = sqlThreadRellanaAnuncio.getAnuncio();
                txtTitulo.setText(anuncio.getTitulo());
                txtDesc.setText(txtDesc.getText().toString().concat(anuncio.getDescrip()));
                txtUbic.setText(anuncio.getUbicacion());
                txtPuntos.setText(txtPuntos.getText().toString().concat(anuncio.getPuntos()));
                txtCat.setText(txtCat.getText().toString().concat(Sesion.getModelo().getCategorias()[anuncio.getCategoria() - 1]));
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
                        Button btnInfoAutor;
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
                        btnInfoAutor = findViewById(R.id.btnInfoAutor);
                        btnInfoAutor.setVisibility(View.VISIBLE);
                        btnInfoAutor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                creaDialogosInfoAutor(sqlThreadRellanaAnuncio.getNom(),
                                        sqlThreadRellanaAnuncio.getEmail(),
                                        sqlThreadRellanaAnuncio.getTel(),
                                        sqlThreadRellanaAnuncio.getEdad());
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

    private void creaDialogosInfoAutor(String nom, String email, int tel, byte edad) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(
                        nom.concat("\n")
                                .concat(email).concat("\n")
                                .concat(Integer.toString(tel)).concat("\n")
                                .concat(Byte.toString(edad)).concat(" años")
                ).setTitle("Info sobre el vendedor");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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
        private int tel;
        private String nom, email, edad;
        private String[] datos;
        private int idAnunc;
        private boolean exito;

        public void run() {
            Intent i;
            i = getIntent();
            idAnunc = i.getIntExtra("idAnuncio", -1);
            anuncio = Sesion.getModelo().verAnuncio(idAnunc);
            datos = Sesion.getModelo().obtieneAutorAnuncio(idAnunc);
            nom = datos[1];
            email = datos[2];
            tel = Integer.parseInt(datos[3]);
            edad = datos[4];
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

        public int getTel() {
            return tel;
        }

        public byte getEdad() {
            return calculaEdad();
        }

        private byte calculaEdad() {
            byte edad, dia, mes;
            short anio;
            final long MILISANIO = (long) 31536000000.0;
            dia = Byte.parseByte(this.edad.substring(this.edad.lastIndexOf("-") + 1));
            mes = Byte.parseByte(this.edad.substring(this.edad.indexOf("-") + 1, this.edad.lastIndexOf("-")));
            anio = Short.parseShort(this.edad.substring(0, this.edad.indexOf("-")));
            edad = (byte) ((new GregorianCalendar().getTimeInMillis() - new GregorianCalendar(anio, mes - 1, dia).getTimeInMillis()) / MILISANIO);
            return edad;
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
