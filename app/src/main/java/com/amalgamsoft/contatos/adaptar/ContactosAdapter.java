package com.amalgamsoft.contatos.adaptar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amalgamsoft.contatos.R;
import com.amalgamsoft.contatos.data.Contacto;

import java.util.List;

public class ContactosAdapter extends ArrayAdapter<Contacto> {
    private Context context;
    private int resource;
    private List<Contacto> listado;
    public ContactosAdapter( @NonNull Context context,  @NonNull List<Contacto> objects) {
        super(context, R.layout.item_contactos, objects);
        this.context = context;
        this.resource = R.layout.item_contactos;
        this.listado = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = li.inflate(resource, null);
        }
        if (listado.get(position) != null) {
            TextView nombre, telefono;
            ImageView foto;
            nombre = v.findViewById(R.id.itemNombre);
            telefono = v.findViewById(R.id.itemTel);
            foto = v.findViewById(R.id.itemFoto);
            nombre.setText(listado.get(position).getNombre());
            telefono.setText(listado.get(position).getTelefono());
            byte[] decodedString = Base64.decode(listado.get(position).getFoto(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            foto.setImageBitmap(decodedByte);
        }
        return v;
    }
}
