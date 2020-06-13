package com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil.trueques;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.SurfaceControl;
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
 * Use the {@link FragmentSolicitantes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentSolicitantes extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Trueque[] vecTruequesSolicitantes;
    private static View view;
    private static Context context;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentSolicitantes() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentSolicitantes.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentSolicitantes newInstance(String param1, String param2) {
        FragmentSolicitantes fragment = new FragmentSolicitantes();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_solicitantes, container, false);
        context = view.getContext();
        operacionesLista();
        return view;
    }

    protected static void operacionesLista() {
        ExpandableListView expandListView;
        SqlThreadGetSolic sqlThreadGetSolic;
        Trueque[] vecTrueques;
        ExpListViewAdapterSolicitantes adapter;
        ArrayList<String> listTrueques;
        ArrayList<String> listSubTrueques;
        Map<String, ArrayList<String>> mapChild;
        int i;
        try {
            sqlThreadGetSolic = new SqlThreadGetSolic(false);
            sqlThreadGetSolic.start();
            sqlThreadGetSolic.join();
            if (sqlThreadGetSolic.getExito()) {
                vecTrueques = sqlThreadGetSolic.getVecTrueques();
                vecTruequesSolicitantes = vecTrueques;
                expandListView = view.findViewById(R.id.listaSolicitantes);
                listTrueques = new ArrayList<>();
                listSubTrueques = new ArrayList<>();
                mapChild = new HashMap<>();
                for (i = 0; i <= vecTrueques.length - 1; i++) {
                    listTrueques.add(vecTrueques[i].getTitulo());
                    listSubTrueques.add(vecTrueques[i].getNombre());
                    listSubTrueques.add(vecTrueques[i].getTelefono());
                    listSubTrueques.add(vecTrueques[i].getEmail());
                    mapChild.put(listTrueques.get(i), new ArrayList<>(listSubTrueques));
                    listSubTrueques.clear();
                }
                adapter = new ExpListViewAdapterSolicitantes(listTrueques, mapChild, context);
                expandListView.setAdapter(adapter);
            } else {
                creaDialogosError();
            }
        } catch (InterruptedException ie) {
            System.err.println(ie.getMessage());
            creaDialogosError();
        }
    }

    private static void creaDialogosError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage(R.string.error_dialogo).setTitle("ERROR");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static Trueque[] getVecTruequesSolicitantes() {
        return FragmentSolicitantes.vecTruequesSolicitantes;
    }
}