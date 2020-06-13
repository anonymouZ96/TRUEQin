package com.nuevasprofesiones.dam2.pi.trueqin.controlador.general;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Sesion;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.utils.Anuncio;

import java.io.IOException;

public class FragmentGeneral extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public FragmentGeneral() {
    }

    public static FragmentGeneral newInstance(String param1, String param2) {
        FragmentGeneral fragment = new FragmentGeneral();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        Button btnBusc;
        View root;
        root = inflater.inflate(R.layout.fragment_general, container, false);
        btnBusc = root.findViewById(R.id.btnBuscar);
        btnBusc.requestFocus();
        btnBusc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abreBusqueda();
            }
        });
        return root;
    }

    public void abreBusqueda() {
        boolean exito;
        byte i;
        TextView txtNomBusq, txtUbicacionBusq, txtErrorTit, txtErrorUbi;
        TextInputLayout txtInputCadBusq, txtInputLocBusq;
        String nomBusq, ubic;
        SqlThreadBuscaAnuncios sqlThreadBuscaAnuncios;
        Intent intent;
        txtNomBusq = getActivity().findViewById(R.id.edTxtCadBusq);
        txtUbicacionBusq = getActivity().findViewById(R.id.edTxtLoc);
        txtInputCadBusq = getView().findViewById(R.id.txtInputCadBusq);
        txtInputLocBusq = getView().findViewById(R.id.txtInputLoc);
        txtInputCadBusq.setErrorEnabled(false);
        txtInputLocBusq.setErrorEnabled(false);
        try {
            nomBusq = txtNomBusq.getText().toString().trim();
            ubic = txtUbicacionBusq.getText().toString().trim();
            sqlThreadBuscaAnuncios = new SqlThreadBuscaAnuncios(nomBusq, ubic);
            sqlThreadBuscaAnuncios.start();
            sqlThreadBuscaAnuncios.join();
            i = 0;
            if (Sesion.getResultados()[0]) {
                exito = true;
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtInputCadBusq.setErrorEnabled(true);
                    txtInputCadBusq.setError(getString(R.string.cadbusq_error_busq));
                    exito = false;
                }
                i++;
                if (!Sesion.getResultados()[i]) {
                    txtInputCadBusq.setErrorEnabled(true);
                    txtInputCadBusq.setError(getString(R.string.ubic_error_busq));
                    exito = false;
                }
                if (exito) {
                    intent = new Intent(getContext(), ListaAnuncios.class);
                    intent.putExtra("operac", (byte) 2);
                    intent.putExtra("listaDeAnuncios", sqlThreadBuscaAnuncios.getElementos());
                    intent.putExtra("cadBusq", nomBusq);
                    startActivity(intent);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.error_dialogo).setTitle("ERROR");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    class SqlThreadBuscaAnuncios extends Thread {
        private Anuncio[] elementos;
        private String texto, ubicacion;

        SqlThreadBuscaAnuncios(String texto, String ubicacion) {
            this.texto = texto;
            this.ubicacion = ubicacion;
        }

        public void run() {
            try {
                this.elementos = Sesion.getModelo().buscarAnuncios(texto, ubicacion);
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            } catch (ClassNotFoundException cnfe) {
                System.err.println(cnfe.getMessage());
            }
        }

        public Anuncio[] getElementos() {
            return this.elementos;
        }


    }

}
