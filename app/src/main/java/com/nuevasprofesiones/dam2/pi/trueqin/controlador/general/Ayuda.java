package com.nuevasprofesiones.dam2.pi.trueqin.controlador.general;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ExpandableListView;

import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil.trueques.ExpListViewAdapterSolicitantes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Ayuda extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayuda);
        operacionesLista();
    }

    private void operacionesLista() {
        ExpandableListView expandListView;
        ExpListViewAdapterAyuda adapter;
        byte i;
        String[] listCasos;
        String contCasos;
        Map<String,String> mapChild;


        expandListView = findViewById(R.id.listaCasosAyuda);
        listCasos = new String[getResources().getStringArray(R.array.opciones_ayuda).length];
        mapChild = new HashMap<>();
        for (i = 0; i <= getResources().getStringArray(R.array.opciones_ayuda).length - 1; i++) {
            listCasos[i] = (getResources().getStringArray(R.array.opciones_ayuda)[i]);
            contCasos = (getResources().getStringArray(R.array.contenido_opciones_ayuda)[i]);
            mapChild.put(listCasos[i], contCasos);
        }
        adapter = new ExpListViewAdapterAyuda(listCasos, mapChild, this);
        expandListView.setAdapter(adapter);
    }
}
