package com.amalgamsoft.contatos;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amalgamsoft.contatos.adaptar.ContactosAdapter;
import com.amalgamsoft.contatos.data.Contacto;
import com.amalgamsoft.contatos.utilities.APIUtility;

import java.util.List;

public class ListadoContactos extends AppCompatActivity {
    private List<Contacto> listado;
    private ProgressDialog dialog;
    private ListView list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listado_contactos_layout);
        list = findViewById(R.id.list);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Cargando Contactos");
        dialog.show();
        APIUtility.getContactos(this);
    }

    public void drawContactos(List<Contacto> contactos) {
        dialog.dismiss();
        this.listado = contactos;
        list.setAdapter(new ContactosAdapter(this, listado));
    }

    public void apiError(String message) {
        dialog.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
