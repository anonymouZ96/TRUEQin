package com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.controlador.general.AnuncioActivity;
import com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil.trueques.ActivityTrueques;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.utils.Anuncio;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Sesion;

import java.io.IOException;
import java.sql.SQLException;

public class FragmentPerfil extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private static View view;

    public static FragmentPerfil newInstance(String param1, String param2) {
        FragmentPerfil fragment = new FragmentPerfil();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView listaAnunc;
        TextView txtPuntos;
        final SqlThreadLLenaLista sqlThreadLLenaLista;
        try {
            view = inflater.inflate(R.layout.fragment_perfil, container, false);
            txtPuntos = view.findViewById(R.id.txtPuntosPerfil);
            txtPuntos.setText(txtPuntos.getText().toString().concat(Integer.toString(Sesion.getPuntos())));
            sqlThreadLLenaLista = new SqlThreadLLenaLista();
            sqlThreadLLenaLista.start();
            sqlThreadLLenaLista.join();
            if (sqlThreadLLenaLista.getExito()) {
                listaAnunc = view.findViewById(R.id.listaMisAnuncios);
                ArrayAdapter<Anuncio> adaptadorLista = null;
                adaptadorLista = new ArrayAdapter<Anuncio>(view.getContext(), R.layout.row, sqlThreadLLenaLista.getElementos());
                listaAnunc.setAdapter(adaptadorLista);
                listaAnunc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent i;
                        i = new Intent(getContext(), AnuncioActivity.class);
                        i.putExtra("oper", (byte) 1);
                        i.putExtra("idAnuncio", sqlThreadLLenaLista.getElementos()[position].getId());
                        startActivity(i);
                    }
                });
            } else {
                creaDialogosError();
            }
        } catch (InterruptedException ie) {
            System.err.println(ie.getMessage());
            creaDialogosError();
        }

        Button btnRefresh;
        btnRefresh = view.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRefresh();
            }
        });

        Button btnTrueques;
        btnTrueques = view.findViewById(R.id.btnTrueques);
        btnTrueques.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickTrueques();
            }
        });

        return view;
    }

    private void creaDialogosError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Se ha producido un error").setTitle("ERROR");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void clickRefresh() {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.detach(this);
        transaction.attach(this);
        transaction.commit();
    }

    private void clickTrueques() {
        Intent i;
        i = new Intent(getContext(), ActivityTrueques.class);
        startActivity(i);
    }

    class SqlThreadLLenaLista extends Thread {
        private boolean exito;
        private Anuncio[] elementos;

        public void run() {
            try {
                this.exito = false;
                elementos = Sesion.getModelo().buscarAnuncios();
                this.exito = true;
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
            return this.exito;
        }

    }
}
