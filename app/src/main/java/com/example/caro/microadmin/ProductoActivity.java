package com.example.caro.microadmin;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;
import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class ProductoActivity extends AppCompatActivity {

    private String APP_DIRECTORY="MicroAdmin/";
    private String MEDIA_DIRECTORY= APP_DIRECTORY+"Imagenes";
    private final int MY_PERMISSIONS = 100;
    private final int PHOTO_CODE = 200;
    private final int SELECT_PICTURE = 300;
    private ImageView mSetimageView;
    private FloatingActionButton mbuttonImage;
    private RelativeLayout mRlView;
    private Button btGuardar;
    private EditText codigo;
    private EditText nombre;
    private EditText cantidad;
    private EditText precioUnidad;
    private EditText costoManufactura;
    private int PICK_IMAGE_REQUEST = 1;
    private String UPLOAD_URL ="http://microadmin.000webhostapp.com/AgregarProducto.php";
    private Bitmap bitmap;
    private String KEY_IMAGE = "imagenCodificada";


    private String mpath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_producto);


        mSetimageView = (ImageView) findViewById(R.id.producto_placeholder);
        mbuttonImage =(FloatingActionButton) findViewById(R.id.bt_subir_imagen);
        mRlView = (RelativeLayout) findViewById(R.id.layout_img);
        btGuardar = (Button) findViewById(R.id.bt_guardar);
        codigo = (EditText) findViewById(R.id.tf_codigo);
        nombre = (EditText) findViewById(R.id.tf_nombre_producto);
        cantidad = (EditText) findViewById(R.id.tf_cantifdad);
        precioUnidad = (EditText) findViewById(R.id.tf_precio_unidad);
        costoManufactura = (EditText) findViewById(R.id.tf_costo_manufactura);

        if(myRequestStoragePermission())
            mbuttonImage.setEnabled(true);
        else
            mbuttonImage.setEnabled(false);


        mbuttonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOptions();
            }
        });

        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean cancel = false;
                View focusView = null;
                if(bitmap == null){
                    cancel = true;
                }
                if (TextUtils.isEmpty(codigo.getText().toString().trim()) ){
                    codigo.setError("Este campo es requerido");
                    focusView = codigo;
                    cancel = true;
                } if(TextUtils.isEmpty(nombre.getText().toString().trim())) {
                    nombre.setError("Este campo es requerido");
                    focusView = nombre;
                    cancel = true;
                } if(TextUtils.isEmpty(precioUnidad.getText().toString().trim())) {
                    precioUnidad.setError("Este campo es requerido");
                    focusView = precioUnidad;
                    cancel = true;
                } if(TextUtils.isEmpty(costoManufactura.getText().toString().trim())) {
                    costoManufactura.setError("Este campo es requerido");
                    focusView = costoManufactura;
                    cancel = true;
                } if(TextUtils.isEmpty(cantidad.getText().toString().trim())) {
                    cantidad.setError("Este campo es requerido");
                    focusView = cantidad;
                    cancel = true;
                } if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    if(focusView != null){
                        focusView.requestFocus();
                    }else{
                        Toast.makeText(ProductoActivity.this,"Seleccione una imagen para el producto ",Toast.LENGTH_SHORT).show();
                    }

                }else {
                uploadImage();
                }
            }
        });
    }

    private void showOptions() {
        final CharSequence[] options = {"Tomar Foto","Elegir de la galeria","Cancelar"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(ProductoActivity.this);
        builder.setTitle("Elegir Opción");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(options[which]=="Tomar Foto"){
                    OpenCamera();
                }else if(options[which]=="Elegir de la galeria"){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent,"Selecciona app de imagen"),SELECT_PICTURE);
                }else if(options[which]=="Cancelar"){
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    private void OpenCamera() {
        File file = new File(Environment.getExternalStorageDirectory(), MEDIA_DIRECTORY);
        boolean thisDirectoryExist = file.exists();

        if (!thisDirectoryExist) {

            thisDirectoryExist = file.mkdirs();
        }
        if (thisDirectoryExist) {

            Long timestamp = System.currentTimeMillis() / 1000;
            String pictureName = timestamp.toString() + ".jpg";
            mpath = Environment.getExternalStorageDirectory() + File.separator
                    + MEDIA_DIRECTORY + File.separator + pictureName;
            File newFile = new File(mpath);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
            startActivityForResult(intent, PHOTO_CODE);
        }
    }


    private boolean myRequestStoragePermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)&&
                (checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED))
            return true;
        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) ||
                (shouldShowRequestPermissionRationale(CAMERA))){
            Snackbar.make(mRlView,"Los permisos son necesarios para usar la app",
                    Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok,new View.OnClickListener(){
                @TargetApi(Build.VERSION_CODES.M)
                @Override
                public void onClick(View v){
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA}, MY_PERMISSIONS);
                }
            }).show();

        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA}, MY_PERMISSIONS);
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PHOTO_CODE:
                    MediaScannerConnection.scanFile(this, new String[]{mpath}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                @Override
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned" + path + ":");
                                    Log.i("ExternalStorage","-> Uri"+uri );
                                }
                            });

                    //Bitmap bitmap = BitmapFactory.decodeFile(mpath);
                    BitmapFactory.Options opts = new BitmapFactory.Options ();
                    opts.inSampleSize = 2;   // for 1/2 the image to be loaded
                     bitmap = Bitmap.createScaledBitmap (BitmapFactory.decodeFile(mpath, opts), 400, 400, false);
                    mSetimageView.setImageBitmap(bitmap);
                    break;
                case SELECT_PICTURE:
                    Uri path = data.getData();

                    mSetimageView.setImageURI(path);
                    bitmap = reduceBitmap(this, path.toString(), 400,400);
                    mSetimageView.setImageBitmap(bitmap);

                    break;

            }
        }
    }

    public static Bitmap reduceBitmap(Context contexto, String uri,
                                      int maxAncho, int maxAlto) {
        System.out.println(uri);
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(contexto.getContentResolver()
                    .openInputStream(Uri.parse(uri)), null, options);
            options.inSampleSize = (int) Math.max(
                    Math.ceil(options.outWidth / maxAncho),
                    Math.ceil(options.outHeight / maxAlto));
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(contexto.getContentResolver()
                    .openInputStream(Uri.parse(uri)), null, options);
        } catch (FileNotFoundException e) {
            Toast.makeText(contexto, "Fichero/recurso no encontrado",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_PERMISSIONS){
            if(grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(ProductoActivity.this,"Permisos aceptados",Toast.LENGTH_SHORT).show();
                mbuttonImage.setEnabled(true);
            }else{
                showExplanation();
            }
        }
    }


    private void showExplanation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProductoActivity.this);
        builder.setTitle("Permisos denegados");
        builder.setMessage("Para usar las funciones de la app, necesita aceptar los permisos");
        builder.setPositiveButton("Aceptar",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog,int which){
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("file_path",mpath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mpath = savedInstanceState.getString("file_path");
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Guardando...","Por favor espere...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        if(!s.isEmpty()) {
                            Toast.makeText(ProductoActivity.this, "Se ha guardado correctamente", Toast.LENGTH_LONG).show();
                            nombre.setText("");
                            codigo.setText("");
                            precioUnidad.setText("");
                            costoManufactura.setText("");
                            cantidad.setText("");
                            mSetimageView.setImageResource(android.R.drawable.ic_menu_camera);
                        }else{
                            Toast.makeText(ProductoActivity.this, "No se ha podido guardar correctamente", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(ProductoActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String

                    String imagen = getStringImage(bitmap);
                    //Creating parameters
                    Map<String, String> params = new Hashtable<String, String>();


                    //Adding parameters
                    params.put("codigo", codigo.getText().toString().trim());
                    params.put("nombre", nombre.getText().toString().trim());
                    params.put("preciounidad", precioUnidad.getText().toString().trim());
                    params.put("costomanufactura", costoManufactura.getText().toString().trim());
                    params.put(KEY_IMAGE, imagen);
                    params.put("cantidad", cantidad.getText().toString().trim());
                    return params;
                }

                    //returning parameters



        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(ProductoActivity.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }



}

