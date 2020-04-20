package com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil.trueques;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil.trueques.ui.main.SectionsPagerAdapter;

public class ActivityTrueques extends AppCompatActivity implements ExpListViewAdapterSolicitantes.ActualizarElem {

    private SectionsPagerAdapter sectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trueques);
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void actualizar() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.detach(sectionsPagerAdapter.getItem(1));
        transaction.attach(sectionsPagerAdapter.getItem(1));
        transaction.commit();
    }
}