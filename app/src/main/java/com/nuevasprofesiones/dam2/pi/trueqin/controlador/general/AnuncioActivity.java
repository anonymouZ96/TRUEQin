package com.nuevasprofesiones.dam2.pi.trueqin.controlador.general;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.nuevasprofesiones.dam2.pi.trueqin.R;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.utils.Anuncio;
import com.nuevasprofesiones.dam2.pi.trueqin.modelo.Sesion;

import java.io.IOException;
import java.util.GregorianCalendar;

public class AnuncioActivity extends AppCompatActivity {

    protected static byte k;
    private int idAnuncio;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anuncio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        k = 0;
        this.idAnuncio = getIntent().getIntExtra("idAnuncio", -1);
        operacAnunucio();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (k == 1) {
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
        }
    }

    private void operacAnunucio() {
        final Anuncio anuncio;
        Drawable imgEdit;
        TextView txtTitulo, txtDesc, txtUbic, txtPuntos, txtCat;
        final SqlThreadRellenaAnuncio sqlThreadRellenaAnuncio;
        try {
            final Intent i;
            txtTitulo = findViewById(R.id.txtTituloAnunc);
            txtDesc = findViewById(R.id.txtDescAnunc);
            txtUbic = findViewById(R.id.txtUbicAnunc);
            txtPuntos = findViewById(R.id.txtPuntosAnunc);
            txtCat = findViewById(R.id.txtCategAnunc);
            i = getIntent();
            sqlThreadRellenaAnuncio = new SqlThreadRellenaAnuncio(getIntent());
            sqlThreadRellenaAnuncio.start();
            sqlThreadRellenaAnuncio.join();
            if (sqlThreadRellenaAnuncio.getExito()) {
                anuncio = sqlThreadRellenaAnuncio.getAnuncio();
                txtTitulo.setText(anuncio.getTitulo());
                txtDesc.setText(txtDesc.getText().toString().concat(anuncio.getDescrip()));
                txtUbic.setText(anuncio.getUbicacion());
                txtPuntos.setText(txtPuntos.getText().toString().concat(anuncio.getPuntos()));
                txtCat.setText(txtCat.getText().toString().concat(Sesion.getModelo().getCategorias()[anuncio.getCategoria() - 1]));
                if (i.getByteExtra("oper", (byte) -1) == 1) {
                    imgEdit = this.getResources().getDrawable(R.drawable.pencil_outline);
                    imgEdit.setBounds(0, 0, 60, 60);
                    txtTitulo.setCompoundDrawables(null, null, imgEdit, null);
                    txtTitulo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            k = 1;
                            creaDialogoEditar((byte) 1);
                        }
                    });
                    txtDesc.setCompoundDrawables(null, null, imgEdit, null);
                    txtDesc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            k = 1;
                            creaDialogoEditar((byte) 2);
                        }
                    });
                    txtUbic.setCompoundDrawables(null, null, imgEdit, null);
                    txtUbic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            k = 1;
                            creaDialogoEditar((byte) 3);
                        }
                    });
                    txtPuntos.setCompoundDrawables(null, null, imgEdit, null);
                    txtPuntos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            k = 1;
                            creaDialogoEditar((byte) 4);
                        }
                    });
                    txtCat.setCompoundDrawables(null, null, imgEdit, null);
                    txtCat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            k = 1;
                            creaDialogoEdCateg((byte) 5, anuncio.getCategoria());
                        }
                    });
                } else {
                    if (i.getByteExtra("oper", (byte) -1) == 2) {
                        ImageButton btnTrueq;
                        Button btnInfoAutor;
                        btnTrueq = findViewById(R.id.butTrueqAnunc);
                        btnTrueq.setVisibility(View.VISIBLE);
                        btnTrueq.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    SqlThreadSolicitarAnuncio sqlThreadSolicitarAnuncio;
                                    sqlThreadSolicitarAnuncio = new SqlThreadSolicitarAnuncio(getIntent());
                                    sqlThreadSolicitarAnuncio.start();
                                    sqlThreadSolicitarAnuncio.join();
                                    if (sqlThreadSolicitarAnuncio.getCodErr() == 0) {
                                        Toast.makeText(getApplicationContext(), R.string.solicitado, Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (sqlThreadSolicitarAnuncio.getCodErr() == -1) {
                                            creaDialogosError(getString(R.string.error_dialogo));
                                        } else {
                                            if (sqlThreadSolicitarAnuncio.getCodErr() == 1) {
                                                creaDialogoCancelar();
                                            } else {
                                                if (sqlThreadSolicitarAnuncio.getCodErr() == 2) {
                                                    creaDialogosError(getString(R.string.nosufipuentos));
                                                }
                                            }
                                        }
                                    }
                                } catch (InterruptedException ie) {
                                    System.err.println(ie.getMessage());
                                    creaDialogosError(getString(R.string.error_dialogo));
                                }
                            }
                        });
                        btnInfoAutor = findViewById(R.id.btnInfoAutor);
                        btnInfoAutor.setVisibility(View.VISIBLE);
                        btnInfoAutor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                creaDialogosInfoAutor(sqlThreadRellenaAnuncio.getNom(),
                                        sqlThreadRellenaAnuncio.getEmail(),
                                        sqlThreadRellenaAnuncio.getTel(),
                                        sqlThreadRellenaAnuncio.getEdad());
                            }
                        });
                    }
                }
            } else {
                creaDialogosError(getString(R.string.error_dialogo));
            }
        } catch (InterruptedException ie) {
            System.err.println(ie.getMessage());
            creaDialogosError(getString(R.string.error_dialogo));
        }
    }

    private void creaDialogoEditar(final byte op) {
        String msg;
        final TextInputLayout layoutIntroTexto;

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final View customLayout = getLayoutInflater().inflate(R.layout.dialogo_custom, null);
        builder.setView(customLayout);

        layoutIntroTexto = customLayout.findViewById(R.id.layoutIntroTexto);

        msg = "";
        if (op == 1) {
            msg = getString(R.string.newtitulo_edanuncio);
            layoutIntroTexto.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
        } else {
            if (op == 2) {
                msg = getString(R.string.newdesc_edanuncio);
                layoutIntroTexto.getEditText().setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            } else {
                if (op == 3) {
                    msg = getString(R.string.newubic_edanuncio);
                    layoutIntroTexto.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
                } else {
                    if (op == 4) {
                        msg = getString(R.string.newpuntos_edanuncio);
                        layoutIntroTexto.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
                    }
                }
            }
        }

        layoutIntroTexto.setHint(msg);

//        builder.setTitle(title);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        CustomListener customListener = new CustomListener(dialog, this.idAnuncio, layoutIntroTexto, op, getIntent(), this);
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(customListener);

    }

    private void creaDialogoEdCateg(final byte op, final byte categ) {
        final byte[] catSelec = new byte[1];
        int checkedItem;
        String[] listItems;
        byte i;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.newcat_edanuncio);

        listItems = new String[Sesion.getModelo().getCategorias().length - 1];
        for (i = 0; i < categ - 1; i++) {
            listItems[i] = Sesion.getModelo().getCategorias()[i];
        }
        for (i = categ; i <= Sesion.getModelo().getCategorias().length - 1; i++) {
            listItems[i - 1] = Sesion.getModelo().getCategorias()[i];
        }

        checkedItem = 0;
        builder.setSingleChoiceItems(listItems, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which < categ - 1) {
                    catSelec[0] = (byte) (which - 1);
                } else {
                    catSelec[0] = (byte) which;
                }
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SqlThreadEditar sqlThreadEditar;
                try {
                    sqlThreadEditar = new SqlThreadEditar(Integer.toString(catSelec[0]), idAnuncio, op);
                    sqlThreadEditar.start();
                    sqlThreadEditar.join();
                    if (sqlThreadEditar.getExito()) {
                        Toast.makeText(AnuncioActivity.this, R.string.actualizado_ed_datos, Toast.LENGTH_SHORT).show();
                        AnuncioActivity.k = 1;
                        ((AnuncioActivity) AnuncioActivity.this).onResume();
                    }
                } catch (InterruptedException ie) {
                    System.err.println(ie.getMessage());
                    creaDialogosError(getString(R.string.error_dialogo));
                }
            }
        });

        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void creaDialogoCancelar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.cancelar_solicitud).setTitle(R.string.yasolicitado);
        builder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SqlThreadCancelaSolicitud sqlThreadCancelaSolicitud;
                try {
                    sqlThreadCancelaSolicitud = new SqlThreadCancelaSolicitud(idAnuncio);
                    sqlThreadCancelaSolicitud.start();
                    sqlThreadCancelaSolicitud.join();
                    if (sqlThreadCancelaSolicitud.getExito()) {
                        Toast.makeText(AnuncioActivity.this, R.string.cancelado, Toast.LENGTH_SHORT).show();
                    }
                } catch (InterruptedException ie) {
                    creaDialogosError(getString(R.string.error_dialogo));
                    System.err.println(ie.getMessage());
                }
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void creaDialogosInfoAutor(String nom, String email, int tel, byte edad) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(
                nom.concat("\n")
                        .concat(email).concat("\n")
                        .concat(Integer.toString(tel)).concat("\n")
                        .concat(Byte.toString(edad)).concat(getString(R.string.anios))
        ).setTitle(R.string.info_vendedor_anuncio);
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void creaDialogosError(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg).setTitle("ERROR");
        builder.setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}

class SqlThreadRellenaAnuncio extends Thread {
    private Anuncio anuncio;
    private int tel;
    private String nom, email, edad;
    private String[] datos;
    private int idAnunc;
    private boolean exito;
    private Intent intent;

    public SqlThreadRellenaAnuncio(Intent intent) {
        this.intent = intent;
    }

    public void run() {
        idAnunc = intent.getIntExtra("idAnuncio", -1);
        anuncio = Sesion.getModelo().verAnuncio(idAnunc);
        datos = Sesion.getModelo().obtieneAutorAnuncio(idAnunc);
        nom = datos[1];
        email = datos[2];
        tel = Integer.parseInt(datos[3]);
        edad = datos[4];
        exito = true;
    }

    public Anuncio getAnuncio() {
        return this.anuncio;
    }

    public String getNom() {
        return nom;
    }

    public String getEmail() {
        return email;
    }

    public int getTel() {
        return tel;
    }

    public byte getEdad() {
        return calculaEdad();
    }

    private byte calculaEdad() {
        byte edad, dia, mes;
        short anio;
        final long MILISANIO = (long) 31536000000.0;
        dia = Byte.parseByte(this.edad.substring(this.edad.lastIndexOf("-") + 1));
        mes = Byte.parseByte(this.edad.substring(this.edad.indexOf("-") + 1, this.edad.lastIndexOf("-")));
        anio = Short.parseShort(this.edad.substring(0, this.edad.indexOf("-")));
        edad = (byte) ((new GregorianCalendar().getTimeInMillis() - new GregorianCalendar(anio, mes - 1, dia).getTimeInMillis()) / MILISANIO);
        return edad;
    }

    public boolean getExito() {
        return exito;
    }
}

class SqlThreadCancelaSolicitud extends Thread {
    private boolean exito;
    private int idAnunc;

    public SqlThreadCancelaSolicitud(int idAnunc) {
        this.idAnunc = idAnunc;
    }

    public void run() {
        this.exito = false;
        try {
            if (Sesion.getModelo().cancelar(this.idAnunc)) {
                this.exito = true;
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    public boolean getExito() {
        return this.exito;
    }
}

class SqlThreadSolicitarAnuncio extends Thread {
    private byte codErr;
    private Intent intent;

    public SqlThreadSolicitarAnuncio(Intent intent) {
        this.intent = intent;
    }

    public void run() {
        try {
            int idAnunc;
            idAnunc = intent.getIntExtra("idAnuncio", -1);
            codErr = 0;
            if (!Sesion.getModelo().check(Integer.toString(idAnunc), (byte) 4)) {
                codErr = 1;
            } else {
                if (!Sesion.getModelo().check(Integer.toString(idAnunc), (byte) 3)) {
                    codErr = 2;
                } else {
                    if (!Sesion.getModelo().solicitar(idAnunc)) {
                        codErr = -1;
                    }
                }
            }
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
    }

    public byte getCodErr() {
        return this.codErr;
    }
}

class SqlThreadEditar extends Thread {
    private String contenidoNuevo;
    private int idAnunc;
    private byte oper;
    private boolean exito;

    SqlThreadEditar(String contenidoNuevo, int idAnunc, byte oper) {
        this.contenidoNuevo = contenidoNuevo;
        this.idAnunc = idAnunc;
        this.oper = oper;
    }

    public void run() {
        this.exito = false;
        try {
            Sesion.getModelo().editarAnuncio(this.contenidoNuevo, this.idAnunc, this.oper);
            this.exito = true;
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.err.println(cnfe.getMessage());
        }
    }

    public boolean getExito() {
        return this.exito;
    }
}

class CustomListener extends AppCompatActivity implements View.OnClickListener {

    private final Dialog dialog;
    private int idAnuncio;
    private boolean exito;
    private TextInputLayout textInputLayout;
    private byte op;
    private Intent intent;
    private Context context;

    public CustomListener(Dialog dialog, int idAnuncio, TextInputLayout textInputLayout, byte op, Intent intent, Context context) {
        this.dialog = dialog;
        this.idAnuncio = idAnuncio;
        this.textInputLayout = textInputLayout;
        this.op = op;
        this.intent = intent;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        actualizaContenido();
    }

    public void actualizaContenido() {
        SqlThreadEditar sqlThreadEditar;
        try {
            sqlThreadEditar = new SqlThreadEditar(textInputLayout.getEditText().getText().toString(), this.idAnuncio, op);
            sqlThreadEditar.start();
            sqlThreadEditar.join();
            if (Sesion.getResultados()[0]) {
                if (!Sesion.getResultados()[1]) {
                    if (op == 1) {
                        this.exito = false;
                        textInputLayout.setError("Título no válido.");
                        textInputLayout.setErrorEnabled(true);
                    } else {
                        if (op == 2) {
                            this.exito = false;
                            textInputLayout.setError("Descripción no válida.");
                            textInputLayout.setErrorEnabled(true);
                        } else {
                            if (op == 3) {
                                this.exito = false;
                                textInputLayout.setError("Ubicación no válida");
                                textInputLayout.setErrorEnabled(true);
                            } else {
                                if (op == 4) {
                                    this.exito = false;
                                    textInputLayout.setError("Puntos necesarios no válidos.");
                                    textInputLayout.setErrorEnabled(true);
                                }
                            }
                        }
                    }
                } else {
                    this.exito = true;
                    AnuncioActivity.k = 1;
                    this.dialog.dismiss();
                    Toast.makeText(this.context, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                    ((AnuncioActivity) this.context).onResume();
                }
            } else {
                this.exito = false;
            }
        } catch (InterruptedException ie) {
            System.err.println(ie.getMessage());
            this.exito = false;
        }
    }
}

