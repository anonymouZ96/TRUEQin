package com.nuevasprofesiones.dam2.pi.trueqin.controlador.general;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.utils.Anuncio;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Sesion;

import java.io.IOException;
import java.sql.SQLException;

public class ListaAnuncios extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView txtNomCat;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_anuncios);
        txtNomCat = findViewById(R.id.txtNomCateg);
        txtNomCat.setText(txtNomCat.getText().toString().concat(" ").concat(Sesion.getModelo().getCategorias()[getIntent().getByteExtra("idCategoria", (byte) -1) - 1]));
        operacionesLista();
    }

    private void operacionesLista() {
        final Anuncio[] elementos;
        SqlThreadListaAnunc sqlThreadListaAnunc;
        ListView listaAnunc;
        TextView txtNoResults;
        try {
            sqlThreadListaAnunc = new SqlThreadListaAnunc();
            sqlThreadListaAnunc.start();
            sqlThreadListaAnunc.join();
            if (sqlThreadListaAnunc.getExito()) {
                elementos = sqlThreadListaAnunc.getElementos();
                if (elementos.length > 0) {
                    listaAnunc = findViewById(R.id.listaAnuncioAct);
                    ArrayAdapter<Anuncio> adaptadorLista = null;
//                    adaptadorLista = new ArrayAdapter<Anuncio>(this, android.R.layout.simple_list_item_1, elementos);
                    adaptadorLista = new ArrayAdapter<Anuncio>(this, R.layout.row, elementos);
                    listaAnunc.setAdapter(adaptadorLista);
                    listaAnunc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent i;
                            i = new Intent(getApplicationContext(), AnuncioActivity.class);
                            i.putExtra("oper", (byte) 2);
                            i.putExtra("idAnuncio", elementos[position].getId());
                            startActivity(i);
                        }
                    });
                } else {
                    txtNoResults = findViewById(R.id.txtNoResults);
                    txtNoResults.setVisibility(View.VISIBLE);
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

    class SqlThreadListaAnunc extends Thread {
        private boolean exito;
        private Anuncio[] elementos;

        public void run() {
            try {
                Intent i;
                i = getIntent();
                if (i.getByteExtra("operac", (byte) -1) == 1) {
                    elementos = Sesion.getModelo().buscarAnuncios(i.getByteExtra("idCategoria", (byte) -1));
                } else {
                    if (i.getByteExtra("operac", (byte) -1) == 2) {
                        elementos = (Anuncio[]) i.getSerializableExtra("listaDeAnuncios");
                    }
                }
                exito = true;
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            } catch (ClassNotFoundException cnfe) {
                System.err.println(cnfe.getMessage());
            }
        }

        public Anuncio[] getElementos() {
            return this.elementos;
        }

        public boolean getExito() {
            return exito;
        }
    }
}
