package fia.ues.saluddigital.BD_Control;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import fia.ues.saluddigital.Usuario.Usuario;

public class BD_Control {

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;
    ArrayList<Usuario> usuarios;

    String[] camposUsuario = new String[]{"id", "nombre", "clave"};

    public BD_Control(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
    private static class DatabaseHelper extends SQLiteOpenHelper {
        private static final String BASE_DATOS = "saludDigital.s3db";
        private static final int version = 1;

        DatabaseHelper(Context context) {
            super(context, BASE_DATOS, null, version);
        }


        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            try {
                sqLiteDatabase.execSQL("CREATE TABLE if not exists Usuario(" +
                        "id VARCHAR(2) PRIMARY KEY, " +
                        "nombre VARCHAR(30), " +
                        "clave VARCHAR(5));");
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public void abrir() throws SQLException {
        db = DBHelper.getWritableDatabase();
    }

    public void cerrar() {
        DBHelper.close();
    }

    //*******************************LOGIN******************************************************
    public Usuario getUsuario(String u, String p) {
        usuarios = selectUsuarios();
        for (Usuario us : usuarios) {
            if (us.getNombre().equals(u) && us.getContrasenia().equals(p)) {
                return us;
            }
        }
        return null;
    }

    public Usuario getUsuarioById(String nombre) {
        usuarios = selectUsuarios();
        for (Usuario us : usuarios) {
            if (us.getNombre() == nombre) {
                return us;
            }
        }
        return null;
    }

    public ArrayList<Usuario> selectUsuarios() {
        ArrayList<Usuario> lista_usuarios = new ArrayList<>();
        lista_usuarios.clear();
        Cursor cursor = db.rawQuery("SELECT * FROM Usuario", null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Usuario usuario = new Usuario();
                usuario.setId(cursor.getInt(0));
                usuario.setNombre(cursor.getString(1));
                usuario.setContrasenia(cursor.getString(2));
                lista_usuarios.add(usuario);
            } while (cursor.moveToNext());
        }
        return lista_usuarios;
    }

    public int login(String u, String p) {
        int a = 0;
        Cursor cr = db.rawQuery("SELECT * FROM Usuario", null);
        if (cr != null && cr.moveToFirst()) {
            do {
                if (cr.getString(1).equals(u) && cr.getString(2).equals(p)) {
                    a++;
                }
            } while (cr.moveToNext());
        }
        return a;
    }

    //*******************************CRUD USUARIO***********************************************
    public String insertar(Usuario usuario) {
        String regInsertador = "Registro insertado #";
        long contador = 0;
            ContentValues us = new ContentValues();
            us.put("id", usuario.getId());
            us.put("nombre", usuario.getNombre());
            us.put("clave", usuario.getContrasenia());
            contador = db.insert("Usuario", null, us);
            if (contador == 1 || contador == 0) {
                regInsertador = "Error al insertar el registro, Registro duplicado. Verificar Inserción";
            } else {
                regInsertador = regInsertador + contador;
            }
        return regInsertador;
    }

    public Usuario consultar(int id) {
        String[] id_usuario = {String.valueOf(id)};
        Cursor cursor = db.query("Usuario", camposUsuario, "id = ?", id_usuario, null, null, null);
        if (cursor.moveToFirst()) {
            Usuario usuario = new Usuario();
            usuario.setId(cursor.getInt(0));
            usuario.setNombre(cursor.getString(1));
            usuario.setContrasenia(cursor.getString(2));
            return usuario;
        } else {
            return null;
        }
    }

    public String actualizar(Usuario usuario) {
        String[] id = {String.valueOf(usuario.getId())};
        ContentValues cv = new ContentValues();
        cv.put("nombre", usuario.getNombre());
        cv.put("clave", usuario.getContrasenia());
        db.update("Usuario", cv, "id = ?", id);
        return "Registro actualizado correctamente";
    }

    public String eliminar(Usuario usuario) {
        String regAfectados = "filas afectadas= ";
        int contador = 0;
        db.delete("Usuario", "id = " + usuario.getId() + "", null);
        return regAfectados;
    }

    public int cantidad_us(){
        DBHelper = new DatabaseHelper(context);
        abrir();
        ArrayList<Usuario> usuarios = new ArrayList<>();
        Usuario usuario = new Usuario();
        Cursor cursor = db.rawQuery("SELECT id FROM Usuario", null);
        if (cursor!=null){
            if(cursor.moveToFirst()){
                do {
                    usuario.setId(cursor.getInt(0));
                    usuarios.add(usuario);
                }while (cursor.moveToNext());
            }cerrar();
            return usuarios.size();
        }else
            return 0;
    }

}


