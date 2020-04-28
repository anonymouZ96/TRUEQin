package com.nuevasprofesiones.dam2.pi.trueqin.controlador.general;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nuevasprofesiones.dam2.pi.trueqin.R;

import java.util.ArrayList;
import java.util.Map;

public class ExpListViewAdapterAyuda extends BaseExpandableListAdapter {
    private String[] listCasosAyuda;
    private Map<String, String> mapChild;
    private Context context;

    public ExpListViewAdapterAyuda(String[] listCasosAyuda, Map<String, String> mapChild, Context context) {
        this.listCasosAyuda = listCasosAyuda;
        this.mapChild = mapChild;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return listCasosAyuda.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listCasosAyuda[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mapChild.get(listCasosAyuda[groupPosition]);
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
        String tituloCaso;
        TextView txtGroup;

        convertView = LayoutInflater.from(context).inflate(R.layout.expand_group_ayuda, null);
        txtGroup = convertView.findViewById(R.id.txtGroupAyuda);
        tituloCaso = (String) getGroup(groupPosition);
        txtGroup.setText(tituloCaso);

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
        return false;
    }
}
