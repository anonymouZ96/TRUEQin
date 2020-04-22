package com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil.trueques;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;
import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Sesion;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class ExpListViewAdapterSolicitantes extends BaseExpandableListAdapter {
    private ArrayList<String> listTrueques;
    private Map<String, ArrayList<String>> mapChild;
    private Context context;
    public ActualizarElem actualizarElem;

    public ExpListViewAdapterSolicitantes(ArrayList<String> listTrueques, Map<String, ArrayList<String>> mapChild, Context context) {
        this.listTrueques = listTrueques;
        this.mapChild = mapChild;
        this.context = context;
    }

    public interface ActualizarElem {
        public void actualizar();
    }

    @Override
    public int getGroupCount() {
        return listTrueques.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mapChild.get(listTrueques.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listTrueques.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mapChild.get(listTrueques.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String tituloTrueque;
        TextView txtGroup, txtInfoConf;
        Button btnAcep, btnRech;
        convertView = LayoutInflater.from(context).inflate(R.layout.expand_group_solicitantes, null);

        txtGroup = convertView.findViewById(R.id.txtGroup);
        tituloTrueque = (String) getGroup(groupPosition);
        txtGroup.setText(tituloTrueque);

        btnAcep = convertView.findViewById(R.id.btnAcep);
        btnRech = convertView.findViewById(R.id.btnRech);

        if (FragmentSolicitantes.getVecTruequesSolicitantes()[groupPosition].getEstado() == 1) {
            btnAcep.setVisibility(View.INVISIBLE);
            btnRech.setVisibility(View.INVISIBLE);
            txtInfoConf = convertView.findViewById(R.id.txtInfoConfirmacion);
            txtInfoConf.setVisibility(View.VISIBLE);
            txtInfoConf.setText("Aceptado");
        } else {
            if (FragmentSolicitantes.getVecTruequesSolicitantes()[groupPosition].getEstado() == 2) {
                btnAcep.setVisibility(View.INVISIBLE);
                btnRech.setVisibility(View.INVISIBLE);
                txtInfoConf = convertView.findViewById(R.id.txtInfoConfirmacion);
                txtInfoConf.setVisibility(View.VISIBLE);
                txtInfoConf.setText("Rechazado");
            } else {
                btnAcep.setClickable(true);
                btnRech.setClickable(true);
                final View finalConvertView = convertView;
                btnAcep.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SqlThreadConfSolicitud sqlThreadConfSolicitud;
                        TextView txtInfoConf;
                        try {
                            sqlThreadConfSolicitud = new SqlThreadConfSolicitud(FragmentSolicitantes.getVecTruequesSolicitantes()[groupPosition].getIdAnuncio(), FragmentSolicitantes.getVecTruequesSolicitantes()[groupPosition].getIdUs(), (byte) 1);
                            sqlThreadConfSolicitud.start();
                            sqlThreadConfSolicitud.join();
                            if (!sqlThreadConfSolicitud.getExito()) {
                                creaDialogosError(finalConvertView);
                            } else {
                                Toast.makeText(context, "Aceptado", Toast.LENGTH_SHORT).show();
                                FragmentSolicitantes.operacionesLista();
//                                v.findViewById(R.id.btnAcep).setVisibility(View.INVISIBLE);
//                                v.findViewById(R.id.btnRech).setVisibility(View.INVISIBLE);
//                                txtInfoConf = v.findViewById(R.id.txtInfoConfirmacion);
//                                txtInfoConf.setVisibility(View.VISIBLE);
//                                txtInfoConf.setText("Aceptado");
                            }
                        } catch (InterruptedException ie) {
                            System.err.println(ie.getMessage());
                            creaDialogosError(finalConvertView);
                        }
                    }
                });
                btnRech.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SqlThreadConfSolicitud sqlThreadConfSolicitud;
                        TextView txtInfoConf;
                        try {
                            sqlThreadConfSolicitud = new SqlThreadConfSolicitud(FragmentSolicitantes.getVecTruequesSolicitantes()[groupPosition].getIdAnuncio(), FragmentSolicitantes.getVecTruequesSolicitantes()[groupPosition].getIdUs(), (byte) 2);
                            sqlThreadConfSolicitud.start();
                            sqlThreadConfSolicitud.join();
                            if (!sqlThreadConfSolicitud.getExito()) {
                                creaDialogosError(finalConvertView);
                            } else {
                                Toast.makeText(context, "Rechazado", Toast.LENGTH_SHORT).show();
                                FragmentSolicitantes.operacionesLista();
//                                v.findViewById(R.id.btnAcep).setVisibility(View.INVISIBLE);
                                //v.findViewById(R.id.btnRech).setVisibility(View.INVISIBLE);
//                                txtInfoConf = v.findViewById(R.id.txtInfoConfirmacion);
//                                txtInfoConf.setVisibility(View.VISIBLE);
//                                txtInfoConf.setText("Rechazado");
                                //actualizarElem.actualizar();
                            }
                        } catch (InterruptedException ie) {
                            System.err.println(ie.getMessage());
                            creaDialogosError(finalConvertView);
                        }
                    }
                });
            }
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String item;
        TextView txtChild;
        convertView = LayoutInflater.from(context).inflate(R.layout.expand_child, null);
        txtChild = convertView.findViewById(R.id.txtChild);
        item = (String) getChild(groupPosition, childPosition);
        txtChild.setText(item);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void creaDialogosError(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setMessage("Se ha producido un error").setTitle("ERROR");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    class SqlThreadConfSolicitud extends Thread {
        private boolean exito;
        private byte resp;
        private int idAnunc, idUs;

        public SqlThreadConfSolicitud(int idAnunc, int idUS, byte resp) {
            this.idAnunc = idAnunc;
            this.idUs = idUS;
            this.resp = resp;
        }

        public void run() {
            exito = false;
            try {
                if (Sesion.getModelo().confirmarTrueque(this.idAnunc, this.idUs, this.resp)) {
                    exito = true;
                }
            } catch (IOException ioe) {
                System.err.println(ioe.getMessage());
            }
        }

        public boolean getExito() {
            return this.exito;
        }
    }
}
