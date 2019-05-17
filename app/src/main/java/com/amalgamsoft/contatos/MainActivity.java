package com.amalgamsoft.contatos;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.amalgamsoft.contatos.data.Contacto;
import com.amalgamsoft.contatos.utilities.APIUtility;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

import locationprovider.davidserrano.com.LocationProvider;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText nombre, numero;
    private Button guardar, consulta;
    private ImageView foto;
    private ProgressDialog dialog;
    private float latitud, longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nombre = findViewById(R.id.txtNombre);
        numero = findViewById(R.id.txtTelefono);
        guardar = findViewById(R.id.btnGrabar);
        consulta = findViewById(R.id.btnConsulta);
        foto = findViewById(R.id.foto);

        guardar.setOnClickListener(this);
        consulta.setOnClickListener(this);
        foto.setOnClickListener(this);

        StrictMode.VmPolicy.Builder newbuilder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(newbuilder.build());

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (!report.areAllPermissionsGranted()) {
                    Toast.makeText(MainActivity.this, "Debe activar todos los permisos", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();
        initLocationProvider();
    }

    private void initLocationProvider() {
        latitud =0;longitud=0;
        LocationProvider.LocationCallback callback = new LocationProvider.LocationCallback() {


            @Override
            public void onNewLocationAvailable(float lat, float lon) {
                //location update
                latitud = lat;
                longitud = lon;
                Toast.makeText(MainActivity.this, "Lat: " + latitud + ", Long: " + longitud, Toast.LENGTH_LONG).show();
            }

            @Override
            public void locationServicesNotEnabled() {
                //failed finding a location
            }

            @Override
            public void updateLocationInBackground(float lat, float lon) {
                //if a listener returns after the main locationAvailable callback, it will go here
                latitud = lat;
                longitud = lon;
                Toast.makeText(MainActivity.this, "Lat: " + latitud + ", Long: " + longitud, Toast.LENGTH_LONG).show();
            }

            @Override
            public void networkListenerInitialised() {
                //when the library switched from GPS only to GPS & network
            }

            @Override
            public void locationRequestStopped() {

            }
        };

        //initialise an instance with the two required parameters
        LocationProvider locationProvider = new LocationProvider.Builder()
                .setContext(this)
                .setListener(callback)
                .create();

        //start getting location
        locationProvider.requestLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        foto.setImageURI(Uri.parse(Environment.getExternalStorageDirectory() + File.separator + "dataC" + File.separator + "foto.jpg"));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.btnGrabar :
                Contacto item;
                dialog = new ProgressDialog(this);
                dialog.setMessage("Grabando item");
                dialog.show();
                item = new Contacto();
                item.setNombre(nombre.getText().toString());
                item.setTelefono(numero.getText().toString());
                APIUtility.grabarContacto(this, item);
                break;
            case R.id.btnConsulta :
                intent = new Intent(this, ListadoContactos.class);
                startActivity(intent);
                break;
            case R.id.foto :
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(Environment.getExternalStorageDirectory() + File.separator + "dataC");
                f.mkdirs();
                f = new File(Environment.getExternalStorageDirectory() + File.separator + "dataC" + File.separator + "foto.jpg");
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, Uri.fromFile(f) );
                startActivityForResult(intent, 11);
                break;
        }
    }

    public void grabadoExitoso(String message) {
        dialog.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11) {
            if (resultCode == RESULT_OK) {
                    //use imageUri here to access the image
                    /*
                    Bitmap bmp = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory() + File.separator + "dataC" + File.separator + "foto.jpg");
                    bmp = Bitmap.createScaledBitmap(bmp, 300, 150, false);
                    */
                    foto.setImageURI(Uri.parse(Environment.getExternalStorageDirectory() + File.separator + "dataC" + File.separator + "foto.jpg"));
            }

        }
    }
}
