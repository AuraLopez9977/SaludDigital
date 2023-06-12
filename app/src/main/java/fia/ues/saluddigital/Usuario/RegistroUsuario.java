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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import fia.ues.saluddigital.BD_Control.BD_Control;
import fia.ues.saluddigital.MainActivity;
import fia.ues.saluddigital.R;
import fia.ues.saluddigital.RutinaEjercicios.EjercicioVideo;

public class RegistroUsuario extends AppCompatActivity {

    BD_Control bd_control;
    EditText txtUsuario, txtContrasenia;
    int id = 0;
    String path = "";
    Button btn_cargar_img, btn_crear_usuario;
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
        txtUsuario = findViewById(R.id.txt_usuario);
        txtContrasenia = findViewById(R.id.txt_contra);
        bd_control = new BD_Control(this);

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
                //datos de usuario
               // crearUsuario();
                //Regresar();

            }
        });
    }

    private void Regresar() {
        Intent intent = new Intent(RegistroUsuario.this, MainActivity.class);
        startActivity(intent);
    }

    private void crearUsuario() {
        int id = bd_control.cantidad_us();
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre(txtUsuario.getText().toString());
        usuario.setContrasenia(txtUsuario.getText().toString());
        bd_control.abrir();
        bd_control.insertar(usuario);
        //System.out.println("********************************************************"+path);
        bd_control.cerrar();

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
           // path = imageUri.toString()+"/Telefono/Pictures/saludDigital/"+fileName.toString();
          //  System.out.println("////////////////////////"+imageUri.toString());

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
            path = imageDir+fileName;
            try {
                fos = new FileOutputStream(file);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }

        boolean saved = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        if (saved) {
            Toast.makeText(this, "El usuario se ha creado exitosamente", Toast.LENGTH_SHORT).show();
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
}