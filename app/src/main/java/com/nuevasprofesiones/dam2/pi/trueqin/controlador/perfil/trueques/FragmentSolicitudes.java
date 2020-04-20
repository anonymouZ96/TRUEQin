package com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil.trueques;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.utils.Trueque;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentSolicitudes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSolicitudes extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Trueque[] vecTruequesSolicitudes;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentSolicitudes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSolicitudes.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSolicitudes newInstance(String param1, String param2) {
        FragmentSolicitudes fragment = new FragmentSolicitudes();
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
        SqlThreadGetSolic sqlThreadGetSolic;
        View view;
        view = inflater.inflate(R.layout.fragment_solicitudes, container, false);
        try {
            sqlThreadGetSolic = new SqlThreadGetSolic(true);
            sqlThreadGetSolic.start();
            sqlThreadGetSolic.join();
            if (sqlThreadGetSolic.getExito()) {
                operacionesLista(view, sqlThreadGetSolic.getVecTrueques());
            } else {
                creaDialogosError("Se ha producido un error de conexión");
            }
        } catch (InterruptedException ie) {
            System.err.println(ie.getMessage());
            creaDialogosError("Se ha producido un error en su dispositivo");
        }
        return view;
    }

    private void operacionesLista(View view, Trueque[] vecTrueques) {
        ExpandableListView expandListView;
        ExpListViewAdapterSolicitudes adapter;
        ArrayList<String> listTrueques;
        ArrayList<String> listSubTrueques;
        Map<String, ArrayList<String>> mapChild;
        int i;
        vecTruequesSolicitudes = vecTrueques;
        expandListView = view.findViewById(R.id.listaSolicitudes);
        listTrueques = new ArrayList<>();
        listSubTrueques = new ArrayList<>();
        mapChild = new HashMap<>();

        for (i = 0; i <= vecTrueques.length - 1; i++) {
            listTrueques.add(vecTrueques[i].getTitulo());
            listSubTrueques.add(vecTrueques[i].getNombre());
            listSubTrueques.add(vecTrueques[i].getTelefono());
            listSubTrueques.add(vecTrueques[i].getEmail());
            if (vecTrueques[i].getEstado() == 1) {
                listSubTrueques.add("Aceptada");
            } else {
                if (vecTrueques[i].getEstado() == 2) {
                    listSubTrueques.add("Rechazada");
                } else {
                    if (vecTrueques[i].getEstado() == 3) {
                        listSubTrueques.add("Pendiente");
                    }
                }
            }
            mapChild.put(listTrueques.get(i), new ArrayList<>(listSubTrueques));
            listSubTrueques.clear();
        }

        adapter = new ExpListViewAdapterSolicitudes(listTrueques, mapChild, getContext());
        expandListView.setAdapter(adapter);
    }

    private void creaDialogosError(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(msg).setTitle("ERROR");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static Trueque[] getVecTruequesSolicitudes() {
        return FragmentSolicitudes.vecTruequesSolicitudes;
    }
}
