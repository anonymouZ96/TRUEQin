package com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil.trueques;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.nuevasprofesiones.dam2.pi.trueqin.R;

import java.util.ArrayList;
import java.util.Map;

public class ExpListViewAdapterSolicitudes extends BaseExpandableListAdapter {

    private ArrayList<String> listTrueques;
    private Map<String, ArrayList<String>> mapChild;

    public ExpListViewAdapterSolicitudes(ArrayList<String> listTrueques, Map<String, ArrayList<String>> mapChild, Context context) {
        this.listTrueques = listTrueques;
        this.mapChild = mapChild;
        this.context = context;
    }

    private Context context;

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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String tituloTrueque;
        TextView txtGroup;
        convertView = LayoutInflater.from(context).inflate(R.layout.expand_group_solicitudes, null);
        txtGroup = convertView.findViewById(R.id.txtGroup);
        if (FragmentSolicitudes.getVecTruequesSolicitudes()[groupPosition].getEstado() == 1) {
            txtGroup.getBackground().setColorFilter(Color.parseColor("#43A047"), PorterDuff.Mode.DARKEN);
        } else {
            if (FragmentSolicitudes.getVecTruequesSolicitudes()[groupPosition].getEstado() == 2) {
                txtGroup.getBackground().setColorFilter(Color.parseColor("#B71C1C"), PorterDuff.Mode.DARKEN);
            } else {
                if (FragmentSolicitudes.getVecTruequesSolicitudes()[groupPosition].getEstado() == 3) {
                    txtGroup.getBackground().setColorFilter(Color.parseColor("#B5B5B5"), PorterDuff.Mode.DARKEN);
                }
            }
        }
        tituloTrueque = (String) getGroup(groupPosition);
        txtGroup.setText(tituloTrueque);
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
}
