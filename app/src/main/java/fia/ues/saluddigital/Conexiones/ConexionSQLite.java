package fia.ues.saluddigital.Conexiones;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexionSQLite extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "salud_digital.db";
    // private static final String TABLE_name = "name";
    private SQLiteDatabase database;

    public ConexionSQLite(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Aquí se crea la estructura de las tablas y se ejecutan las sentencias SQL necesarias
        // sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_name);  Define tus tablas y sus columnas aquí
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Aquí se actualiza la estructura de las tablas en caso de que haya cambios en la versión de la base de datos
        // sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_name); Elimina las tablas existentes si es necesario
        // nCreate(sqLiteDatabase);
    }

    public void open() {
        database = getWritableDatabase();
    }

    public void close() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }


}
