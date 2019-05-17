package com.amalgamsoft.contatos;

import android.app.Application;

import com.amalgamsoft.contatos.data.Contacto;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.List;

public class AppContacto extends Application {
    private RequestQueue queue;

    @Override
    public void onCreate() {
        super.onCreate();
        queue = Volley.newRequestQueue(this);
    }

    public RequestQueue getQueue() {
        return queue;
    }

}
