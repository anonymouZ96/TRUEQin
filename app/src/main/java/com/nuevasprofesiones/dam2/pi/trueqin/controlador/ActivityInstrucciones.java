package com.nuevasprofesiones.dam2.pi.trueqin.controlador;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.nuevasprofesiones.dam2.pi.trueqin.controlador.general.Ayuda;
import com.nuevasprofesiones.dam2.pi.trueqin.controlador.general.ListaAnuncios;
import com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil.EdMisDatos;
import com.nuevasprofesiones.dam2.pi.trueqin.controlador.ui.main.SectionsPagerAdapter;
import com.nuevasprofesiones.dam2.pi.trueqin.controlador.categorias.FragmentCategorias;
import com.nuevasprofesiones.dam2.pi.trueqin.controlador.perfil.NuevoAnuncio;

public class ActivityInstrucciones extends AppCompatActivity implements FragmentCategorias.OnListFragmentInteractionListener {

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrucciones);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
        SqlThreadClose sqlThreadClose;
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                sqlThreadClose = new SqlThreadClose();
                sqlThreadClose.start();
                sqlThreadClose.join();
                finishAffinity();
            }
        } catch (InterruptedException ie) {
            System.err.println(ie.getMessage());
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
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
            overridePendingTransition(0, 0);
        } else {
            if (item.getItemId() == R.id.menuAyuda) {
                i = new Intent(this, Ayuda.class);
                startActivity(i);
                overridePendingTransition(0, 0);
            } else {
                if (item.getItemId() == R.id.menuSalir) {
                    creaDialogosConf();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void creaDialogosConf() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.cerrarses_dialog).setTitle(R.string.tit_cerrarses_dialog);
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i;
                finish();
                overridePendingTransition(0, 0);
                i = new Intent(ActivityInstrucciones.this, MainActivity.class);
                MainActivity.op = true;
                startActivity(i);
                overridePendingTransition(0, 0);
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void nuevoAnuncio(View v) {
        Intent i;
        i = new Intent(this, NuevoAnuncio.class);
        startActivity(i);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onListFragmentInteraction(com.nuevasprofesiones.dam2.pi.trueqin.controlador.categorias.dummy.DummyContent.DummyItem item) {
        Intent i;
        i = new Intent(this, ListaAnuncios.class);
        i.putExtra("idCategoria", Byte.parseByte(item.id));
        i.putExtra("operac", (byte) 1);
        startActivity(i);
        overridePendingTransition(0, 0);
    }
}