package fia.ues.saluddigital.Usuario;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.NEARBY_WIFI_DEVICES;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import fia.ues.saluddigital.R;
import fia.ues.saluddigital.RutinaEjercicios.EjercicioVideo;

public class RegistroUsuario extends AppCompatActivity {

    Button btn_cargar_img, btn_crear_usuario;
    private final String carpeta_Raiz ="saludDigital/";
    private final String ruta_img=carpeta_Raiz+"fotos";
    String path ="";
    final int COD_SELECCIONA = 10;
    final int COD_FOTO = 20;
    private static final int REQUES_PERMISSION_CAMERA = 100;
    private static final int TAKE_PICTURE = 100;
    private static final int REQUEST_PERMISSION_WRITE_STORAGE = 100;


    ImageView fotoPerfil;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        btn_cargar_img = findViewById(R.id.btn_foto_perfil);
        btn_crear_usuario = findViewById(R.id.btn_regUx);
        fotoPerfil = findViewById(R.id.fotPerfil);

        btn_cargar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionCamera();
            }
        });

        btn_crear_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //imagen
                checkPermissionStorage();
            }
        });

       /* if (validarPermisos()){
            btn_cargar_img.setEnabled(true);
        }else {
            btn_cargar_img.setEnabled(false);
        }

        btn_cargar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargarImg();
            }
        });*/
    }
    private void checkPermissionCamera() {

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if (ActivityCompat.checkSelfPermission(this, CAMERA)==PackageManager.PERMISSION_GRANTED){
                takePicture();
            }else {
                ActivityCompat.requestPermissions(this, new String[]{CAMERA},REQUES_PERMISSION_CAMERA);
            }
        }else {
            takePicture();
        }
    }

    private void checkPermissionStorage() {
        if (Build.VERSION.SDK_INT<=Build.VERSION_CODES.P){
            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.M){
                if (ActivityCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
                    saveImage();
                }else {
                    ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE_STORAGE);
                }
            }else{
                saveImage();
            }
        }else {
            saveImage();
        }
    }

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager())!=null){
            startActivityForResult(intent, TAKE_PICTURE);
        }

    }

    private void videorep(){
        Intent intent = new Intent(RegistroUsuario.this, EjercicioVideo.class);
       startActivity(intent);

    }

    private void saveImage(){
        OutputStream fos = null;
        File file = null;

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
            ContentResolver resolver = getContentResolver();
            ContentValues values = new ContentValues();

            String fileName = System.currentTimeMillis()+"image_example";
            values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
            values.put(MediaStore.Images.Media.MIME_TYPE,"image/jpeg");
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/saludDigital");
            values.put(MediaStore.Images.Media.IS_PENDING, 1);

            Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri imageUri = resolver.insert(collection, values);

            try {
                fos = resolver.openOutputStream(imageUri);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }

            values.clear();
            values.put(MediaStore.Images.Media.IS_PENDING,0);
            resolver.update(imageUri,values, null, null);
        }else {
            String imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            String fileName = System.currentTimeMillis()+".jpg";
            file = new File(imageDir, fileName);
            try {
                fos = new FileOutputStream(file);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }

        boolean saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        if (saved) {
            Toast.makeText(this, "La imagen se ha guardado exitosamente", Toast.LENGTH_SHORT).show();
        }

        if (fos !=null){
            try {
                fos.flush();
                fos.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        if (file !=null){ //API <29
            MediaScannerConnection.scanFile(this,new String[]{file.toString()}, null, null);
        }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE){
            if (resultCode == Activity.RESULT_OK && data == null);
            bitmap = (Bitmap) data.getExtras().get("data");
            fotoPerfil.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUES_PERMISSION_CAMERA){
            if (permissions.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                takePicture();
            }
        }else if (requestCode == REQUEST_PERMISSION_WRITE_STORAGE){
            if (permissions.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                saveImage();
            }
        }
    }

    /* private boolean validarPermisos() {

        if (Build.VERSION.SDK_INT< Build.VERSION_CODES.M){
            return true;
        }if ((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED)&&(checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)&&checkSelfPermission(READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            return true;
        }if ((shouldShowRequestPermissionRationale(CAMERA))||(shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))||(shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE))){
            cargarDialogoRecomendacion();
        }else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA,READ_EXTERNAL_STORAGE}, 100);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100){
            if (grantResults.length==2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED){
                btn_cargar_img.setEnabled(true);
            }else {
                solicitarPermisosManual();
            }
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"Si", "No"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(RegistroUsuario.this);
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(opciones[which].equals("Si")){
                    Intent config = new Intent();
                    config.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    config.setData(uri);
                    startActivity(config);
                }else{
                    Toast.makeText(getApplicationContext(), "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        alertOpciones.show();
    }

    private void cargarDialogoRecomendacion() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(RegistroUsuario.this);
        dialog.setTitle("Permisos Desactivados");
        dialog.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la APP");
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, CAMERA}, 100);
            }
        });
        dialog.show();
    }

    private void cargarImg(){
        final CharSequence[] opciones = {"Tomar foto","Cargar Imagen", "Cancelar"};
        final AlertDialog.Builder alertOpciones = new AlertDialog.Builder(RegistroUsuario.this);
        alertOpciones.setTitle("Seleccione una opcion");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(opciones[which].equals("Tomar foto")){
                    tomarFoto();
                }else{
                    if (opciones[which].equals("Cargar Imagen")){
                        Intent cargar = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        cargar.setType("image/");
                        startActivityForResult(cargar.createChooser(cargar,"Seleccione la aplicacion"), COD_SELECCIONA);
                    }else {
                        dialog.dismiss();
                    }
                }
            }
        });
        alertOpciones.show();
     }


    private void tomarFoto(){
        File fileImagen = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), ruta_img);
        boolean isCreateImg = fileImagen.exists();
        String nombreImg = "";
        if (isCreateImg == true){
            isCreateImg = fileImagen.mkdirs();
        }
        if (isCreateImg == false){
            nombreImg = (System.currentTimeMillis()/1000)+".jpg";
        }
        path = Environment.getExternalStorageDirectory()+File.separator+ruta_img+File.separator+nombreImg;
        System.out.println(path);
        File img = new File(path);
        //Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(img));
        //camera.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(createImageFile()));
        //startActivityForResult(camera, COD_FOTO);

        Intent tomar = null;
        tomar = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.N){
            String authorities = getApplicationContext().getPackageName()+".provider";
            Uri imageUri = FileProvider.getUriForFile(this, authorities, img);
            tomar.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }else{
            tomar.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(img));
        }
        startActivityForResult(tomar, COD_FOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case COD_FOTO:
                    MediaScannerConnection.scanFile(this, new String[]{path}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                        @Override
                        public void onMediaScannerConnected() {
                        }

                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("Ruta de almacenamiento", "Path: "+path);
                        }
                    });
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    fotoPerfil.setImageBitmap(bitmap);
                    break;
                case COD_SELECCIONA:
                    Uri mipath = data.getData();
                    fotoPerfil.setImageURI(mipath);
                    break;
                default:
                    break;
            }
        }
    }*/
}