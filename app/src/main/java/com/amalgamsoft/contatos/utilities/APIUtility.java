package com.amalgamsoft.contatos.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;

import com.amalgamsoft.contatos.AppContacto;
import com.amalgamsoft.contatos.ListadoContactos;
import com.amalgamsoft.contatos.MainActivity;
import com.amalgamsoft.contatos.data.Contacto;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class APIUtility {
    public static void grabarContacto (final MainActivity activity, final Contacto contacto) {
        StringRequest request = new StringRequest(Request.Method.POST, "http://3.17.128.78/backend/index.php/contactos",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        activity.grabadoExitoso(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        ) {
            @Override
            public byte[] getBody() throws AuthFailureError {
               JSONObject json = new JSONObject();
              byte[] hilera = null;
                try {
                    json.put("nombre", contacto.getNombre());
                    json.put("telefono", contacto.getTelefono());
                    Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "dataC" + File.separator + "foto.jpg");
                    bmp = Bitmap.createScaledBitmap(bmp, 300, 150, false);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    String encodedfile = Base64.encodeToString(byteArray, Base64.DEFAULT);
                    json.put("fotografia", encodedfile);
                    hilera = json.toString().getBytes("UTF-8");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return hilera;
            }
        };
        ((AppContacto)activity.getApplication()).getQueue().add(request);
    }

    public static void getContactos (final ListadoContactos activity) {
        StringRequest request = new StringRequest(Request.Method.GET, "http://3.17.128.78/backend/index.php/contactos",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Contacto> contactos = new ArrayList<Contacto>();
                        Contacto temp;
                        try {
                            JSONObject json = new JSONObject(response);
                            for (int i = 0; i < json.getJSONArray("contactos").length(); i ++) {
                                temp = new Contacto();
                                temp.setId(json.getJSONArray("contactos").getJSONObject(i).getInt("id"));
                                temp.setNombre(json.getJSONArray("contactos").getJSONObject(i).getString("nombre"));
                                temp.setTelefono(json.getJSONArray("contactos").getJSONObject(i).getString("telefono"));
                                temp.setFoto(json.getJSONArray("contactos").getJSONObject(i).getString("fotografia"));
                                contactos.add(temp);
                            }
                            activity.drawContactos(contactos);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        activity.apiError("Error en la carga de datos");
                    }
                }
        );
        ((AppContacto)activity.getApplication()).getQueue().add(request);
    }
 }
