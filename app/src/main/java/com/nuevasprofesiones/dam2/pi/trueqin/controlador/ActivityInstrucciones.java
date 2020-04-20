package com.nuevasprofesiones.dam2.pi.trueqin.controlador;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.controlador.general.ListaAnuncios;
import com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil.EdMisDatos;
import com.nuevasprofesiones.dam2.pi.trueqin.controlador.ui.main.SectionsPagerAdapter;
import com.nuevasprofesiones.dam2.pi.trueqin.controlador.categorias.FragmentCategorias;
import com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil.NuevoAnuncio;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Sesion;

import java.io.IOException;

public class ActivityInstrucciones extends AppCompatActivity implements FragmentCategorias.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 && getCurrentFocus() == findViewById(R.id.actInst)) {
                Sesion.getModelo().cierraConexion();
                finish();
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        overridePendingTransition(0, 0);
        android.os.Process.killProcess(android.os.Process.myPid());
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        if (item.getItemId() == R.id.menuMisDatos) {
            i = new Intent(this, EdMisDatos.class);
            startActivity(i);
        } else {
            if (item.getItemId() == R.id.menuSalir) {
                finish();
                i = new Intent(this, MainActivity.class);
                startActivity(i);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void nuevoAnuncio(View v) {
        Intent i;
        i = new Intent(this, NuevoAnuncio.class);
        startActivity(i);
    }

    @Override
    public void onListFragmentInteraction(com.nuevasprofesiones.dam2.pi.trueqin.controlador.categorias.dummy.DummyContent.DummyItem item) {
        Intent i;
        i = new Intent(this, ListaAnuncios.class);
        i.putExtra("idCategoria", Byte.parseByte(item.id));
        i.putExtra("operac", (byte) 1);
        startActivity(i);
    }
}